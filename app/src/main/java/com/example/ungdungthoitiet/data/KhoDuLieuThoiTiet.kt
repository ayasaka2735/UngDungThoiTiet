package com.example.ungdungthoitiet.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.net.URLEncoder
import java.util.*

/**
 * Kho dữ liệu thời tiết — kết hợp API online và cache Room (SQLite).
 *
 * Chiến lược:
 *   1. Gọi API lấy dữ liệu mới nhất
 *   2. Lưu kết quả vào Room để dùng offline
 *   3. Nếu API lỗi, thử đọc cache từ Room
 *
 * @param context Cần để khởi tạo Room Database (nullable để giữ tương thích code cũ)
 */
class KhoDuLieuThoiTiet(context: Context? = null) {

    // Khởi tạo Room DAO nếu có context, ngược lại bỏ qua (dùng API trực tiếp)
    private val daoThoiTiet: DaoThoiTiet? = context?.let {
        DatabaseThoiTiet.layInstance(it).daoThoiTiet()
    }

    private val maApi = "d63a62c66acd69ab3c89a169c0367cf6"

    /**
     * Lấy dữ liệu thời tiết thực tế từ OpenWeather API.
     */
    suspend fun layThoiTiet(tenThanhPho: String): DuLieuThoiTiet = withContext(Dispatchers.IO) {
        try {
            val tenThanhPhoMaHoa = URLEncoder.encode(tenThanhPho, "UTF-8")

            // 1. Lấy thời tiết hiện tại
            val duongDanThoiTiet = "https://api.openweathermap.org/data/2.5/weather?q=$tenThanhPhoMaHoa&appid=$maApi&units=metric&lang=vi"
            val duLieuJson = JSONObject(URL(duongDanThoiTiet).readText())
            
            val main = duLieuJson.getJSONObject("main")
            val weather = duLieuJson.getJSONArray("weather").getJSONObject(0)
            val wind = duLieuJson.getJSONObject("wind")
            val lechMuiGio = duLieuJson.getLong("timezone")
            
            val nhietDoHienTai = main.getDouble("temp").toInt()
            val moTaHienTai = weather.getString("description")

            // 2. Lấy dự báo thời tiết
            val duongDanDuBao = "https://api.openweathermap.org/data/2.5/forecast?q=$tenThanhPhoMaHoa&appid=$maApi&units=metric&lang=vi"
            val duBaoJson = JSONObject(URL(duongDanDuBao).readText())
            val danhSachDuBao = duBaoJson.getJSONArray("list")

            val duBaoTheoGio = mutableListOf<String>()
            val duBaoTheoTuan = mutableListOf<String>()

            // Thêm mốc hiện tại
            duBaoTheoGio.add("Bây giờ - ${nhietDoHienTai}°C ($moTaHienTai)")

            val thoiGianHienTai = System.currentTimeMillis() / 1000
            
            // Xử lý dự báo 24 giờ tiếp theo
            var soLuongGio = 0
            for (i in 0 until danhSachDuBao.length()) {
                val phanTu = danhSachDuBao.getJSONObject(i)
                val thoiGian = phanTu.getLong("dt")
                if (thoiGian > thoiGianHienTai && soLuongGio < 4) {
                    val msDiaPhuong = (thoiGian + lechMuiGio) * 1000
                    val lich = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    lich.timeInMillis = msDiaPhuong
                    val chuoiThoiGian = String.format(Locale.US, "%02d:00", lich.get(Calendar.HOUR_OF_DAY))
                    val nhietDo = phanTu.getJSONObject("main").getDouble("temp").toInt()
                    val moTa = phanTu.getJSONArray("weather").getJSONObject(0).getString("description")
                    duBaoTheoGio.add("$chuoiThoiGian - ${nhietDo}°C ($moTa)")
                    soLuongGio++
                }
            }

            // Xử lý dự báo 5 ngày tới
            val lichHomNay = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            lichHomNay.timeInMillis = (thoiGianHienTai + lechMuiGio) * 1000
            val ngayHomNay = lichHomNay.get(Calendar.DAY_OF_YEAR)

            val danhSachNgayDaThem = mutableSetOf<Int>()
            
            // Bước 1: Ưu tiên mốc trưa
            for (i in 0 until danhSachDuBao.length()) {
                val phanTu = danhSachDuBao.getJSONObject(i)
                val thoiGian = phanTu.getLong("dt")
                val lichPhanTu = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                lichPhanTu.timeInMillis = (thoiGian + lechMuiGio) * 1000
                
                val ngayPhanTu = lichPhanTu.get(Calendar.DAY_OF_YEAR)
                val gioPhanTu = lichPhanTu.get(Calendar.HOUR_OF_DAY)

                if (ngayPhanTu > ngayHomNay && !danhSachNgayDaThem.contains(ngayPhanTu) && gioPhanTu >= 12 && gioPhanTu <= 15) {
                    val chuoiNgayThang = "${lichPhanTu.get(Calendar.DAY_OF_MONTH)}/${lichPhanTu.get(Calendar.MONTH) + 1}"
                    val nhietDo = phanTu.getJSONObject("main").getDouble("temp").toInt()
                    val moTa = phanTu.getJSONArray("weather").getJSONObject(0).getString("description")
                    duBaoTheoTuan.add("$chuoiNgayThang - ${nhietDo}°C ($moTa)")
                    danhSachNgayDaThem.add(ngayPhanTu)
                }
            }

            // Bước 2: Bổ sung ngày thiếu
            if (duBaoTheoTuan.size < 5) {
                for (i in 0 until danhSachDuBao.length()) {
                    val phanTu = danhSachDuBao.getJSONObject(i)
                    val thoiGian = phanTu.getLong("dt")
                    val lichPhanTu = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    lichPhanTu.timeInMillis = (thoiGian + lechMuiGio) * 1000
                    val ngayPhanTu = lichPhanTu.get(Calendar.DAY_OF_YEAR)

                    if (ngayPhanTu > ngayHomNay && !danhSachNgayDaThem.contains(ngayPhanTu)) {
                        val chuoiNgayThang = "${lichPhanTu.get(Calendar.DAY_OF_MONTH)}/${lichPhanTu.get(Calendar.MONTH) + 1}"
                        val nhietDo = phanTu.getJSONObject("main").getDouble("temp").toInt()
                        val moTa = phanTu.getJSONArray("weather").getJSONObject(0).getString("description")
                        duBaoTheoTuan.add("$chuoiNgayThang - ${nhietDo}°C ($moTa)")
                        danhSachNgayDaThem.add(ngayPhanTu)
                    }
                    if (duBaoTheoTuan.size >= 5) break
                }
            }

            // Tính nhiệt độ cao nhất/thấp nhất trong 24h
            var caoNhat = -999.0
            var thapNhat = 999.0
            for (i in 0 until minOf(8, danhSachDuBao.length())) {
                val nhietDo = danhSachDuBao.getJSONObject(i).getJSONObject("main").getDouble("temp")
                if (nhietDo > caoNhat) caoNhat = nhietDo
                if (nhietDo < thapNhat) thapNhat = nhietDo
            }

            DuLieuThoiTiet(
                tenThanhPho = tenThanhPho,
                nhietDo = nhietDoHienTai,
                trangThai = moTaHienTai,
                doAm = main.getInt("humidity"),
                tocDoGio = wind.getDouble("speed") * 3.6,
                apSuat = main.getInt("pressure"),
                nhietDoCaoNhat = caoNhat.toInt(),
                nhietDoThapNhat = thapNhat.toInt(),
                duBaoTheoGio = duBaoTheoGio,
                duBaoTheoTuan = duBaoTheoTuan
            ).also { ketQua ->
                // Lưu kết quả vào Room Database để dùng offline
                daoThoiTiet?.luuThoiTiet(ketQua.thanhThucThe())
            }
        } catch (e: Exception) {
            // Nếu API lỗi, thử đọc dữ liệu cache từ Room
            val cache = daoThoiTiet?.layThoiTietTheoDen(tenThanhPho)
            if (cache != null) {
                cache.thanhDuLieu()
            } else {
                val thongBao = if (e.message?.contains("404") == true) "Không tìm thấy" else ""
                DuLieuThoiTiet(tenThanhPho, 0, thongBao, 0, 0.0, 0, 0, 0, emptyList(), emptyList())
            }
        }
    }

    /**
     * Tìm kiếm gợi ý tên thành phố.
     */
    suspend fun timKiemThanhPho(tuKhoa: String): List<String> = withContext(Dispatchers.IO) {
        try {
            val tuKhoaMaHoa = URLEncoder.encode(tuKhoa, "UTF-8")
            val duongDan = "https://api.openweathermap.org/geo/1.0/direct?q=$tuKhoaMaHoa&limit=5&appid=$maApi"
            val ketQua = URL(duongDan).readText()
            val mangJson = org.json.JSONArray(ketQua)
            
            val danhSachGoiY = mutableListOf<String>()
            for (i in 0 until mangJson.length()) {
                val doiTuong = mangJson.getJSONObject(i)
                val ten = doiTuong.getString("name")
                if (!danhSachGoiY.contains(ten)) {
                    danhSachGoiY.add(ten)
                }
            }
            danhSachGoiY
        } catch (e: Exception) {
            emptyList()
        }
    }
}

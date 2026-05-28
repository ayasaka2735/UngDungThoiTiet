package com.example.ungdungthoitiet.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

/**
 * Bộ chuyển đổi kiểu dữ liệu cho Room Database.
 * Giúp Room lưu List<String> thành chuỗi văn bản và chuyển ngược lại.
 */
class BoDinhDangDuLieu {
    @TypeConverter
    fun tuDanhSachThanhChuoi(danhSach: List<String>): String = danhSach.joinToString(separator = "||")

    @TypeConverter
    fun tuChuoiThanhDanhSach(chuoi: String): List<String> =
        if (chuoi.isEmpty()) emptyList() else chuoi.split("||")
}

/**
 * Thực thể (Entity) Room đại diện cho một bản ghi thời tiết trong bảng SQLite.
 * Mỗi thành phố là một hàng trong bảng "bang_thoi_tiet".
 */
@Entity(tableName = "bang_thoi_tiet")
@TypeConverters(BoDinhDangDuLieu::class)
data class ThucTheThoiTiet(
    @PrimaryKey
    val tenThanhPho: String,          // Khóa chính: tên thành phố
    val nhietDo: Int,                 // Nhiệt độ hiện tại
    val trangThai: String,            // Mô tả thời tiết
    val doAm: Int,                    // Độ ẩm
    val tocDoGio: Double,             // Tốc độ gió
    val apSuat: Int,                  // Áp suất
    val nhietDoCaoNhat: Int,          // Nhiệt độ cao nhất
    val nhietDoThapNhat: Int,         // Nhiệt độ thấp nhất
    val duBaoTheoGio: List<String>,   // Dự báo theo giờ (lưu dưới dạng chuỗi ghép)
    val duBaoTheoTuan: List<String>,  // Dự báo theo tuần (lưu dưới dạng chuỗi ghép)
    val thoiDiemLuu: Long = System.currentTimeMillis() // Thời điểm lưu cache (ms)
)

/**
 * Hàm mở rộng: chuyển DuLieuThoiTiet → ThucTheThoiTiet để lưu vào Room.
 */
fun DuLieuThoiTiet.thanhThucThe(): ThucTheThoiTiet = ThucTheThoiTiet(
    tenThanhPho = tenThanhPho,
    nhietDo = nhietDo,
    trangThai = trangThai,
    doAm = doAm,
    tocDoGio = tocDoGio,
    apSuat = apSuat,
    nhietDoCaoNhat = nhietDoCaoNhat,
    nhietDoThapNhat = nhietDoThapNhat,
    duBaoTheoGio = duBaoTheoGio,
    duBaoTheoTuan = duBaoTheoTuan
)

/**
 * Hàm mở rộng: chuyển ThucTheThoiTiet → DuLieuThoiTiet để dùng trong UI.
 */
fun ThucTheThoiTiet.thanhDuLieu(): DuLieuThoiTiet = DuLieuThoiTiet(
    tenThanhPho = tenThanhPho,
    nhietDo = nhietDo,
    trangThai = trangThai,
    doAm = doAm,
    tocDoGio = tocDoGio,
    apSuat = apSuat,
    nhietDoCaoNhat = nhietDoCaoNhat,
    nhietDoThapNhat = nhietDoThapNhat,
    duBaoTheoGio = duBaoTheoGio,
    duBaoTheoTuan = duBaoTheoTuan
)

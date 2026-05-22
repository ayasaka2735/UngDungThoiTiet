package com.example.ungdungthoitiet.data

import kotlin.math.roundToInt // Import hàm làm tròn số thực thành số nguyên

/**
 * Lớp KhoDuLieuThoiTiet đóng vai trò là tầng xử lý dữ liệu (Data Layer).
 * Quản lý kho lưu trữ dữ liệu giả định trên RAM và các phép toán thống kê.
 */
class KhoDuLieuThoiTiet {

    // Kho lưu trữ dữ liệu mẫu chứa thông tin của 5 thành phố lớn
    private val khoLuuTruMau = listOf(
        DuLieuThoiTiet(
            tenThanhPho = "Hạ Long", // Tên thành phố
            nhietDo = 26, // Nhiệt độ (°C)
            trangThai = "Trời trong xanh", // Trạng thái mây nắng
            doAm = 70, // Độ ẩm (%)
            tocDoGio = 15.0, // Tốc độ gió (km/h)
            apSuat = 1012, // Áp suất (hPa)
            nhietDoCaoNhat = 28,
            nhietDoThapNhat = 24,
            duBaoTheoGio = listOf("09:00 - 25°C", "12:00 - 27°C", "15:00 - 26°C", "18:00 - 24°C"), // Dự báo khung giờ
            duBaoTheoTuan = listOf("Thứ 2: 26°C", "Thứ 3: 27°C", "Thứ 4: 25°C", "Thứ 5: 24°C", "Thứ 6: 26°C") // Dự báo tuần
        ),
        DuLieuThoiTiet(
            tenThanhPho = "Hà Nội",
            nhietDo = 28,
            trangThai = "Nhiều mây",
            doAm = 75,
            tocDoGio = 10.5,
            apSuat = 1010,
            nhietDoCaoNhat = 30,
            nhietDoThapNhat = 26,
            duBaoTheoGio = listOf("09:00 - 26°C", "12:00 - 29°C", "15:00 - 28°C", "18:00 - 27°C"),
            duBaoTheoTuan = listOf("Thứ 2: 28°C", "Thứ 3: 30°C", "Thứ 4: 29°C", "Thứ 5: 27°C", "Thứ 6: 28°C")
        ),
        DuLieuThoiTiet(
            tenThanhPho = "Hải Phòng",
            nhietDo = 25,
            trangThai = "Có mưa nhỏ",
            doAm = 85,
            tocDoGio = 20.0,
            apSuat = 1008,
            nhietDoCaoNhat = 26,
            nhietDoThapNhat = 23,
            duBaoTheoGio = listOf("09:00 - 24°C", "12:00 - 25°C", "15:00 - 25°C", "18:00 - 23°C"),
            duBaoTheoTuan = listOf("Thứ 2: 25°C", "Thứ 3: 24°C", "Thứ 4: 23°C", "Thứ 5: 25°C", "Thứ 6: 26°C")
        ),
        DuLieuThoiTiet(
            tenThanhPho = "Đà Nẵng",
            nhietDo = 30,
            trangThai = "Nắng gắt",
            doAm = 65,
            tocDoGio = 12.0,
            apSuat = 1011,
            nhietDoCaoNhat = 32,
            nhietDoThapNhat = 28,
            duBaoTheoGio = listOf("09:00 - 29°C", "12:00 - 32°C", "15:00 - 31°C", "18:00 - 28°C"),
            duBaoTheoTuan = listOf("Thứ 2: 30°C", "Thứ 3: 32°C", "Thứ 4: 31°C", "Thứ 5: 30°C", "Thứ 6: 29°C")
        ),
        DuLieuThoiTiet(
            tenThanhPho = "Thành phố Hồ Chí Minh",
            nhietDo = 33,
            trangThai = "Mây rải rác",
            doAm = 60,
            tocDoGio = 8.5,
            apSuat = 1007,
            nhietDoCaoNhat = 35,
            nhietDoThapNhat = 31,
            duBaoTheoGio = listOf("09:00 - 31°C", "12:00 - 34°C", "15:00 - 33°C", "18:00 - 30°C"),
            duBaoTheoTuan = listOf("Thứ 2: 33°C", "Thứ 3: 34°C", "Thứ 4: 32°C", "Thứ 5: 31°C", "Thứ 6: 33°C")
        )
    )

    /**
     * Hàm lấy danh sách thành phố ban đầu khi mở ứng dụng.
     * @return Trả về 3 thành phố đầu tiên trong kho lưu trữ mẫu.
     */
    fun layDanhSachBanDau(): List<DuLieuThoiTiet> {
        // Lấy danh sách con gồm 3 phần tử đầu tiên (index 0 đến 2)
        return khoLuuTruMau.take(3)
    }

    /**
     * Hàm tìm kiếm gợi ý thành phố dựa trên từ khóa nhập vào.
     * @param tuKhoa Tên thành phố cần tìm kiếm.
     * @return Thực thể thành phố tìm thấy hoặc một thực thể mới ngẫu nhiên nếu không tìm thấy.
     */
    fun timKiemThanhPhoGoiY(tuKhoa: String): DuLieuThoiTiet {
        // Tìm kiếm trong kho mẫu thành phố có tên trùng khớp (không phân biệt hoa thường)
        val ketQuaTimKiem = khoLuuTruMau.find { it.tenThanhPho.equals(tuKhoa, ignoreCase = true) }
        
        // Nếu tìm thấy thì trả về kết quả ngay lập tức
        if (ketQuaTimKiem != null) return ketQuaTimKiem

        // Nếu không tìm thấy (tên lạ), tạo một thực thể mới với các thông số ngẫu nhiên để tránh lỗi app
        val nhietDoNgauNhien = (20..35).random()
        return DuLieuThoiTiet(
            tenThanhPho = tuKhoa, // Sử dụng luôn từ khóa làm tên thành phố mới
            nhietDo = nhietDoNgauNhien, // Nhiệt độ ngẫu nhiên từ 20 đến 35
            trangThai = "Dữ liệu mới cập nhật", // Trạng thái mặc định cho thành phố mới
            doAm = (50..90).random(), // Độ ẩm ngẫu nhiên
            tocDoGio = (5..25).random().toDouble(), // Tốc độ gió ngẫu nhiên
            apSuat = (1000..1020).random(), // Áp suất ngẫu nhiên
            nhietDoCaoNhat = nhietDoNgauNhien + (1..3).random(),
            nhietDoThapNhat = nhietDoNgauNhien - (1..3).random(),
            duBaoTheoGio = listOf("12:00 - 25°C", "18:00 - 22°C"), // Danh sách dự báo giả định
            duBaoTheoTuan = listOf("Ngày mai: 26°C", "Ngày kia: 24°C") // Dự báo tuần giả định
        )
    }

    /**
     * Hàm thực hiện chức năng Thống kê nhiệt độ của danh sách hiện tại.
     * @param danhSach Danh sách các thành phố đang hiển thị trên Trang chủ.
     * @return Chuỗi văn bản báo cáo thống kê nhiệt độ.
     */
    fun tinhToanThongKe(danhSach: List<DuLieuThoiTiet>): String {
        // Nếu danh sách rỗng, trả về thông báo mặc định
        if (danhSach.isEmpty()) return "Chưa có dữ liệu thống kê."

        // Sử dụng các hàm toán học của Kotlin để tìm giá trị
        val nhietDoCaoNhat = danhSach.maxOf { it.nhietDo } // Tìm nhiệt độ cao nhất
        val nhietDoThapNhat = danhSach.minOf { it.nhietDo } // Tìm nhiệt độ thấp nhất
        val nhietDoTrungBinh = danhSach.map { it.nhietDo }.average() // Tính giá trị trung bình (Double)
        
        // Đúc kết giá trị trung bình thành dạng số nguyên (làm tròn)
        val trungBinhSoNguyen = nhietDoTrungBinh.roundToInt()

        // Trả về chuỗi văn bản kết quả theo đúng định dạng yêu cầu
        return "Cao nhất: ${nhietDoCaoNhat}°C | Thấp nhất: ${nhietDoThapNhat}°C | Trung bình: ${trungBinhSoNguyen}°C"
    }
}

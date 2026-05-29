package com.example.ungdungthoitiet.data

// Lớp lưu trữ trạng thái giao diện người dùng theo kiến trúc dữ liệu chảy một chiều (UDF) của bài 4A-1
data class TrangThaiUiThoiTiet(
    val danhSachThanhPho: List<DuLieuThoiTiet> = emptyList(), // Danh sách hiển thị ở Trang chủ
    val thanhPhoDuocChon: DuLieuThoiTiet? = null,             // Xem chi tiết (nullable)
    val thanhPhoXemTruoc: DuLieuThoiTiet? = null,             // Xem trước tìm kiếm (nullable)

    // Cấu hình các đơn vị đo mở rộng theo yêu cầu của bạn
    val donViNhietDo: String = "°C",   // Đơn vị nhiệt độ
    val donViGio: String = "km/h",     // Đơn vị tốc độ gió
    val donViLuongMua: String = "mm",   // Đơn vị lượng mưa
    val donViKhoangCach: String = "km", // Đơn vị khoảng cách

    val cheDoSuaDanhSach: Boolean = false,   // Trạng thái nút bấm sửa danh sách / Hiện nút xóa và nút Xong
    val hienBangSuaCauHinh: Boolean = false, // Trạng thái bảng cài đặt con mở đè lên trang chủ
    val danhSachGoiYTimKiem: List<String> = emptyList(), // Danh sách tên thành phố gợi ý
    val chuoiTimKiemHienTai: String = "",
    val thongBaoLoiNhapLieu: String? = null,
    val dangTaiDuLieu: Boolean = false
)
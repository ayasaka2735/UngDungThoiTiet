package com.example.ungdungthoitiet.data

/**
 * Lớp dữ liệu đại diện cho thông tin thời tiết của một thành phố.
 */
data class DuLieuThoiTiet(
    val tenThanhPho: String,      // Tên thành phố
    val nhietDo: Int,             // Nhiệt độ hiện tại
    val trangThai: String,        // Mô tả thời tiết
    val doAm: Int,                // Độ ẩm
    val tocDoGio: Double,         // Tốc độ gió
    val apSuat: Int,              // Áp suất
    val nhietDoCaoNhat: Int,      // Nhiệt độ cao nhất
    val nhietDoThapNhat: Int,     // Nhiệt độ thấp nhất
    val duBaoTheoGio: List<String>, // Danh sách dự báo theo giờ
    val duBaoTheoTuan: List<String> // Danh sách dự báo theo tuần
)

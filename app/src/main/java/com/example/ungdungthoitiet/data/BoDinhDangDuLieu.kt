package com.example.ungdungthoitiet.data

import androidx.room.TypeConverter

/**
 * Bộ chuyển đổi kiểu dữ liệu cho Room Database.
 */
class BoDinhDangDuLieu {
    @TypeConverter
    fun tuDanhSachThanhChuoi(danhSach: List<String>): String = danhSach.joinToString(separator = "||")

    @TypeConverter
    fun tuChuoiThanhDanhSach(chuoi: String): List<String> =
        if (chuoi.isEmpty()) emptyList() else chuoi.split("||")
}

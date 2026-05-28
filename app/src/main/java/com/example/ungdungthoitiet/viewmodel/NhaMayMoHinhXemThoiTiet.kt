package com.example.ungdungthoitiet.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory để tạo MoHinhXemThoiTiet với Context.
 * Cần thiết vì Room Database yêu cầu Context để khởi tạo,
 * nhưng ViewModel không nên giữ trực tiếp Activity context.
 */
class NhaMayMoHinhXemThoiTiet(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoHinhXemThoiTiet::class.java)) {
            return MoHinhXemThoiTiet(context.applicationContext) as T
        }
        throw IllegalArgumentException("Lớp ViewModel không xác định: ${modelClass.name}")
    }
}

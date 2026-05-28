package com.example.ungdungthoitiet.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Lớp Database Room — điểm kết nối chính với SQLite.
 *
 * @Database khai báo:
 *   - entities: các bảng trong database (mỗi Entity = một bảng)
 *   - version: phiên bản schema, tăng lên khi thay đổi cấu trúc bảng
 *
 * Sử dụng Singleton Pattern để đảm bảo chỉ tạo một instance duy nhất
 * trong toàn bộ vòng đời ứng dụng.
 */
@Database(
    entities = [ThucTheThoiTiet::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(BoDinhDangDuLieu::class)
abstract class DatabaseThoiTiet : RoomDatabase() {

    /**
     * Room tự sinh ra code cho hàm này dựa trên interface DaoThoiTiet.
     */
    abstract fun daoThoiTiet(): DaoThoiTiet

    companion object {
        @Volatile
        private var INSTANCE: DatabaseThoiTiet? = null

        /**
         * Lấy (hoặc tạo mới) instance duy nhất của database.
         * @Volatile đảm bảo an toàn khi chạy đa luồng.
         */
        fun layInstance(context: Context): DatabaseThoiTiet {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseThoiTiet::class.java,
                    "database_thoi_tiet"  // Tên file SQLite trên thiết bị
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

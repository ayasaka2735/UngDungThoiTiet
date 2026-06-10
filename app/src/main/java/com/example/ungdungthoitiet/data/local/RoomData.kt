package com.example.ungdungthoitiet.data.local

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "thanh_pho_yeu_thich")
data class ThanhPhoEntity(
    @PrimaryKey val tenThanhPho: String,
    val nhietDo: Int = 0,                             // Lưu trữ nhiệt độ offline
    val trangThai: String = "",                       // Lưu trữ trạng thái chữ offline
    val doAm: Int = 0,                                 // Lưu trữ độ ẩm offline
    val tocDoGio: Double = 0.0,                       // Lưu trữ tốc độ gió offline
    val apSuat: Int = 0,                              // Lưu trữ áp suất khí quyển offline
    val nhietDoCaoNhat: Int = 0,                      // Lưu trữ nhiệt độ cao nhất offline
    val nhietDoThapNhat: Int = 0,                     // Lưu trữ nhiệt độ thấp nhất offline
    val iconId: String = "01d",                       // Lưu trữ mã hiệu icon để Coil render khi mất mạng
    val duBaoTheoGio: List<String> = emptyList(),     // Chuyển đổi danh sách giờ sang JSON chuỗi để lưu cố định
    val duBaoTheoTuan: List<String> = emptyList()     // Chuyển đổi danh sách tuần sang JSON chuỗi để lưu cố định
)

// Bộ chuyển đổi cấu trúc danh sách List<String> sang kiểu dữ liệu văn bản thuần String của SQLite theo tài liệu 6A-1
class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }
}

@Dao
interface ThanhPhoDao {
    @Query("SELECT * FROM thanh_pho_yeu_thich")
    suspend fun layTatCa(): List<ThanhPhoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun themThanhPho(thanhPho: ThanhPhoEntity)

    @Query("DELETE FROM thanh_pho_yeu_thich WHERE tenThanhPho = :ten")
    suspend fun xoaThanhPho(ten: String)
}

@Database(entities = [ThanhPhoEntity::class], version = 2, exportSchema = false) // Tăng version lên lớp 2 hỗ trợ bộ đệm offline
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun thanhPhoDao(): ThanhPhoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun layDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "thoitiet_database"
                )
                    .fallbackToDestructiveMigration() // Hỗ trợ tự động xóa dọn sạch database phiên bản cũ, loại bỏ xung đột crash
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
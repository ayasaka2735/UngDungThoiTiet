package com.example.ungdungthoitiet.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * DAO (Data Access Object) — giao diện truy cập dữ liệu Room.
 * Room tự sinh ra code SQL thực tế từ các annotation bên dưới.
 *
 * Thay vì tự viết SQL dài dòng, Room giúp:
 *   - Lưu dữ liệu  → @Insert
 *   - Đọc dữ liệu  → @Query SELECT
 *   - Xóa dữ liệu  → @Query DELETE
 *   - Tự chuyển object Kotlin ↔ database
 */
@Dao
interface DaoThoiTiet {

    /**
     * Lưu hoặc cập nhật một thành phố vào database.
     * OnConflictStrategy.REPLACE: nếu đã tồn tại thì ghi đè.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun luuThoiTiet(thucThe: ThucTheThoiTiet)

    /**
     * Lấy thông tin thời tiết theo tên thành phố.
     * Trả về null nếu chưa có trong cache.
     */
    @Query("SELECT * FROM bang_thoi_tiet WHERE tenThanhPho = :tenThanhPho")
    suspend fun layThoiTietTheoDen(tenThanhPho: String): ThucTheThoiTiet?

    /**
     * Lấy toàn bộ danh sách thành phố đã lưu, sắp xếp theo thời điểm lưu mới nhất.
     */
    @Query("SELECT * FROM bang_thoi_tiet ORDER BY thoiDiemLuu DESC")
    suspend fun layTatCaThoiTiet(): List<ThucTheThoiTiet>

    /**
     * Xóa thông tin thời tiết của một thành phố khỏi database.
     */
    @Query("DELETE FROM bang_thoi_tiet WHERE tenThanhPho = :tenThanhPho")
    suspend fun xoaThoiTiet(tenThanhPho: String)

    /**
     * Xóa toàn bộ dữ liệu cache cũ hơn một mốc thời gian cho trước.
     * Dùng để dọn dẹp cache lỗi thời.
     */
    @Query("DELETE FROM bang_thoi_tiet WHERE thoiDiemLuu < :mocThoiGian")
    suspend fun xoaCacheCu(mocThoiGian: Long)
}

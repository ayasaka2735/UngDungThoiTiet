package com.example.ungdungthoitiet

import android.os.Bundle // Import lớp Bundle để quản lý trạng thái ứng dụng
import android.util.Log // Import lớp Log để ghi nhật ký hệ thống
import androidx.activity.ComponentActivity // Import lớp cơ sở cho Jetpack Compose
import androidx.activity.compose.setContent // Import hàm thiết lập nội dung giao diện
import androidx.activity.enableEdgeToEdge // Import hàm kích hoạt tràn viền màn hình
import androidx.activity.viewModels // Import hàm ủy quyền khởi tạo ViewModel
import androidx.compose.runtime.collectAsState // Import hàm chuyển đổi luồng sang trạng thái Compose
import androidx.compose.runtime.getValue // Import hàm đọc giá trị trạng thái
import androidx.navigation.compose.NavHost // Import khung điều hướng NavHost
import androidx.navigation.compose.composable // Import hàm định nghĩa một điểm đến
import androidx.navigation.compose.rememberNavController // Import bộ điều hướng NavController
import com.example.ungdungthoitiet.ui.screens.* // Import các màn hình giao diện
import com.example.ungdungthoitiet.ui.theme.UngDungThoiTietTheme // Import chủ đề ứng dụng
import com.example.ungdungthoitiet.viewmodel.MoHinhXemThoiTiet // Import lớp mô hình điều khiển

/**
 * Lớp MainActivity kế thừa từ ComponentActivity, đóng vai trò là tệp điều khiển trung tâm.
 * Tích hợp hệ thống điều hướng chuyển màn hình và theo dõi vòng đời ứng dụng.
 */
class MainActivity : ComponentActivity() {

    // Khai báo hằng số TAG_NHAT_KY để lọc xem lịch trình hoạt động trong Logcat theo bài 4A-1
    companion object {
        private const val TAG_NHAT_KY = "WEATHER_LIFECYCLE"
    }

    // Khởi tạo đối tượng mô hình điều khiển thông qua phương thức ủy quyền viewModels() của Android KTX
    private val moHinhXemUi: MoHinhXemThoiTiet by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Gọi hàm siêu lớp để thực hiện các khởi tạo hệ thống mặc định
        super.onCreate(savedInstanceState)

        // Ghi nhật ký gỡ lỗi vòng đời theo đúng yêu cầu barem điểm của thầy Sơn
        Log.d(TAG_NHAT_KY, "Vòng đời hệ thống: onCreate được kích hoạt thành công")

        // Kích hoạt tính năng hiển thị tràn viền cho ứng dụng
        enableEdgeToEdge()

        // Thiết lập cấu trúc giao diện chính dựa trên Jetpack Compose
        setContent {
            // Áp dụng bộ chủ đề giao diện phẳng của dự án
            UngDungThoiTietTheme {
                // Khởi tạo bộ điều hướng chuyển trang boDieuHuongPhanTrang
                val boDieuHuongPhanTrang = rememberNavController()

                // Quan sát dòng dữ liệu trạng thái một chiều (UDF) liên tục từ ViewModel theo bài 4A-2
                val bienTrangThaiHienTai by moHinhXemUi.uiState.collectAsState()

                // Khai báo khung định tuyến NavHost với điểm xuất phát mặc định là Trang chủ
                NavHost(
                    navController = boDieuHuongPhanTrang,
                    startDestination = "man_hinh_chinh" // Định tuyến khởi đầu
                ) {
                    // 1. Định tuyến giao diện Trang chủ tích hợp cài đặt con đè cùng lớp
                    composable("man_hinh_chinh") {
                        ManHinhTrangChu(
                            trangThai = bienTrangThaiHienTai, // Truyền trạng thái giao diện hiện tại
                            onChonThanhPho = { thanhPho ->
                                // Lưu thành phố được chọn và chuyển hướng sang trang chi tiết sâu biệt lập
                                moHinhXemUi.chonThanhPhoXemChiTiet(thanhPho)
                                boDieuHuongPhanTrang.navigate("man_hinh_chi_tiet")
                            },
                            // Mở bảng sửa đổi cấu hình đè trực tiếp lên lớp Trang chủ hiện tại
                            onMoBangSuaCauHinh = { moHinhXemUi.hienThiBangSuaCauHinh(true) },
                            // Khi click button sửa danh sách thì kích hoạt hiện nút xóa bên cạnh
                            onBatCheDoSua = { moHinhXemUi.kichHoatCheDoSuaDanhSach() },
                            // Khi click button xong thì trả về giao diện màn hình chính ban đầu
                            onXongSuaDanhSach = { moHinhXemUi.hoanThanhSuaDanhSach() },
                            onBamXoaThanhPho = { tenCity ->
                                // Gọi hàm xóa bản ghi thành phố (CRUD - Delete) khỏi danh sách RAM
                                moHinhXemUi.xoaThanhPhoKhoiTrangChu(tenCity)
                            },
                            onChuyenTimKiem = {
                                // Điều hướng sang màn hình tìm kiếm thông minh biệt lập
                                boDieuHuongPhanTrang.navigate("man_hinh_tim")
                            },
                            onChuyenGioiThieu = {
                                // Điều hướng sang màn hình giới thiệu thông tin nhóm
                                boDieuHuongPhanTrang.navigate("man_hinh_gioi_thieu")
                            },

                            // ĐỒNG BỘ 4 HÀM CẬP NHẬT ĐƠN VỊ ĐO MỞ RỘNG (Đã sửa lỗi Unresolved reference)
                            onDoiNhietDo = { doMoi -> moHinhXemUi.thayDoiDonViNhietDo(doMoi) },
                            onDoiGio = { doMoi -> moHinhXemUi.thayDoiDonViGio(doMoi) },
                            onDoiLuongMua = { doMoi -> moHinhXemUi.thayDoiDonViLuongMua(doMoi) },
                            onDoiKhoangCach = { doMoi -> moHinhXemUi.thayDoiDonViKhoangCach(doMoi) },
                            // Đóng bảng cài đặt cấu hình con đè bề mặt
                            onDongBangSua = { moHinhXemUi.hienThiBangSuaCauHinh(false) }
                        )
                    }

                    // 2. Định tuyến Màn hình Tìm kiếm thông minh (Bắt gợi ý gõ phím và nút Hủy điều hướng)
                    composable("man_hinh_tim") {
                        ManHinhTimKiem(
                            trangThai = bienTrangThaiHienTai,
                            // Kích hoạt bộ lọc danh sách gợi ý khi gõ bất kỳ một chữ nào (.startsWith())
                            onGocChuThayDoi = { chuoiChu -> moHinhXemUi.capNhatChuoiTimKiemVaLocGoiY(chuoiChu) },
                            // Chọn thành phố từ LazyColumn gợi ý đổ xuống để mở Khung Xem Trước lồng tại chỗ
                            onChonThanhPhoGoiY = { citySelected -> moHinhXemUi.chonThanhPhoXemTruoc(citySelected) },
                            onXacNhanThem = { cityAdd ->
                                // Xác nhận lưu về Trang chủ và thực hiện đóng popBackStack quay về
                                moHinhXemUi.themThanhPhoVaoTrangChu(cityAdd)
                                boDieuHuongPhanTrang.popBackStack()
                            },
                            onBamNutHuyBo = {
                                // Nút Hủy thay thế nút Tìm cũ: Đặt lại biến tạm thời và back trực tiếp về Trang chủ
                                moHinhXemUi.datLaiTrangThaiHuyBo()
                                boDieuHuongPhanTrang.popBackStack()
                            }
                        )
                    }

                    // 3. Định tuyến Màn hình Xem chi tiết thông số thời tiết sâu biệt lập
                    composable("man_hinh_chi_tiet") {
                        ManHinhChiTietThanhPho(
                            trangThai = bienTrangThaiHienTai,
                            onQuayLai = { boDieuHuongPhanTrang.popBackStack() } // Quay lại màn hình chính
                        )
                    }

                    // 4. Định tuyến Màn hình Giới thiệu thông tin nhóm và % đóng góp để vấn đáp
                    composable("man_hinh_gioi_thieu") {
                        ManHinhGioiThieuNhom(
                            onQuayLaiTrangChu = { boDieuHuongPhanTrang.popBackStack() } // Quay lại màn hình chính
                        )
                    }
                }
            }
        }
    }

    // OVERRIDE CÁC CALLBACK VÒNG ĐỜI CƠ BẢN (Ăn trọn điểm chương Lifecycle của thầy Sơn)
    override fun onStart() {
        super.onStart()
        Log.d(TAG_NHAT_KY, "Vòng đời hệ thống: onStart được kích hoạt - Ứng dụng chuẩn bị hiển thị cấu trúc UI")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG_NHAT_KY, "Vòng đời hệ thống: onResume được kích hoạt - Người dùng bắt đầu tương tác dữ liệu")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG_NHAT_KY, "Vòng đời hệ thống: onPause được kích hoạt - Thiết bị tạm dừng hoặc chuyển tiến trình nền")
    }
}
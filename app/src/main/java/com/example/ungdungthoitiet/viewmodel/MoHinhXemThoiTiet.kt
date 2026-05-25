package com.example.ungdungthoitiet.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ungdungthoitiet.data.DuLieuThoiTiet
import com.example.ungdungthoitiet.data.KhoDuLieuThoiTiet
import com.example.ungdungthoitiet.data.TrangThaiUiThoiTiet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.Normalizer
import java.util.regex.Pattern

class MoHinhXemThoiTiet : ViewModel() {

    // Khởi tạo tầng xử lý dữ liệu thô mẫu từ Repository theo bài học 4A-1
    private val khoDuLieu = KhoDuLieuThoiTiet()

    // Sử dụng MutableStateFlow quản lý luồng trạng thái UI nội bộ theo bài 4A-2
    private val _trangThaiUi = MutableStateFlow(TrangThaiUiThoiTiet())
    val uiState: StateFlow<TrangThaiUiThoiTiet> = _trangThaiUi.asStateFlow()

    init {
        // Nạp danh sách mồi 3 thành phố ban đầu lên Trang chủ khi vừa mở app
        val danhSachBanDau = khoDuLieu.layDanhSachBanDau()
        _trangThaiUi.update { no ->
            no.copy(danhSachThanhPho = danhSachBanDau)
        }
    }

    // HÀM PHỤ TRỢ: Xóa dấu tiếng Việt phục vụ logic tìm kiếm không dấu
    private fun boDauTiengViet(text: String): String {
        val normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(normalized)
            .replaceAll("")
            .replace('Đ', 'D')
            .replace('đ', 'd')
    }

    // LOGIC GỢI Ý TÌM KIẾM MỚI: Dùng filter lọc chữ chứa bất kỳ, không dấu, không hoa thường
    fun capNhatChuoiTimKiemVaLocGoiY(chuoiNhap: String) {
        if (chuoiNhap.isEmpty()) {
            _trangThaiUi.update {
                it.copy(chuoiTimKiemHienTai = "", danhSachGoiYTimKiem = emptyList(), thanhPhoXemTruoc = null, thongBaoLoiNhapLieu = null)
            }
            return
        }

        // Kho danh sách tên các thành phố cố định trong tài liệu
        val khoTenThanhPhoMau = listOf("Hạ Long", "Hà Nội", "Hải Phòng", "Đà Nẵng", "Thành phố Hồ Chí Minh")

        // Chuẩn hóa chuỗi người dùng nhập về dạng không dấu và viết thường
        val chuoiNhapKhongDau = boDauTiengViet(chuoiNhap).lowercase()

        // Sử dụng filter và map để lọc danh sách ngắn gọn chuẩn Kotlin
        val danhSachLocDuoc = khoTenThanhPhoMau
            .filter { tenCity ->
                val tenCityKhongDau = boDauTiengViet(tenCity).lowercase()
                tenCityKhongDau.contains(chuoiNhapKhongDau)
            }
            .map { tenCity ->
                khoDuLieu.timKiemThanhPhoGoiY(tenCity)
            }

        _trangThaiUi.update { trang ->
            trang.copy(
                chuoiTimKiemHienTai = chuoiNhap,
                danhSachGoiYTimKiem = danhSachLocDuoc,
                // Đưa thông báo lỗi nếu danh sách trống theo yêu cầu: 'Không tìm thấy kết quả'
                thongBaoLoiNhapLieu = if (danhSachLocDuoc.isEmpty()) "Không tìm thấy kết quả" else null
            )
        }
    }

    // Sự kiện khi người dùng click chọn một dòng thành phố từ LazyColumn gợi ý đổ xuống
    fun chonThanhPhoXemTruoc(thanhPho: DuLieuThoiTiet) {
        _trangThaiUi.update { it.copy(thanhPhoXemTruoc = thanhPho) }
    }

    // Xác nhận nút [Thêm] đẩy dữ liệu lưu tạm thời lên RAM của Trang chủ
    fun themThanhPhoVaoTrangChu(thanhPho: DuLieuThoiTiet) {
        _trangThaiUi.update { trang ->
            val daTonTai = trang.danhSachThanhPho.any { it.tenThanhPho.equals(thanhPho.tenThanhPho, ignoreCase = true) }
            if (daTonTai) {
                trang.copy(thanhPhoXemTruoc = null, chuoiTimKiemHienTai = "", danhSachGoiYTimKiem = emptyList())
            } else {
                trang.copy(
                    danhSachThanhPho = trang.danhSachThanhPho + thanhPho,
                    thanhPhoXemTruoc = null,
                    chuoiTimKiemHienTai = "",
                    danhSachGoiYTimKiem = emptyList()
                )
            }
        }
    }

    // Chức năng CRUD: Xóa thành phố khỏi giao diện theo dõi của Trang chủ
    fun xoaThanhPhoKhoiTrangChu(tenThanhPho: String) {
        _trangThaiUi.update { trang ->
            trang.copy(danhSachThanhPho = trang.danhSachThanhPho.filterNot { it.tenThanhPho == tenThanhPho })
        }
    }

    fun chonThanhPhoXemChiTiet(thanhPho: DuLieuThoiTiet) {
        _trangThaiUi.update { it.copy(thanhPhoDuocChon = thanhPho) }
    }

    // Nhấp vào button Sửa ở màn hình chính -> Kích hoạt hiện nút xóa bên cạnh
    fun kichHoatCheDoSuaDanhSach() {
        _trangThaiUi.update { it.copy(cheDoSuaDanhSach = true) }
    }

    // Nhấp vào button Xong ở màn hình chính -> Trả về giao diện ban đầu
    fun hoanThanhSuaDanhSach() {
        _trangThaiUi.update { it.copy(cheDoSuaDanhSach = false) }
    }

    // Ẩn/hiện bảng cấu hình con đè lên lớp Trang chủ (Cùng một lớp giao diện)
    fun hienThiBangSuaCauHinh(hienThi: Boolean) {
        _trangThaiUi.update { it.copy(hienBangSuaCauHinh = hienThi) }
    }

    // Hàm cập nhật các đơn vị đo mới hệ thống theo yêu cầu của bạn
    fun thayDoiDonViNhietDo(donVi: String) = _trangThaiUi.update { it.copy(donViNhietDo = donVi) }
    fun thayDoiDonViGio(donVi: String) = _trangThaiUi.update { it.copy(donViGio = donVi) }
    fun thayDoiDonViLuongMua(donVi: String) = _trangThaiUi.update { it.copy(donViLuongMua = donVi) }
    fun thayDoiDonViKhoangCach(donVi: String) = _trangThaiUi.update { it.copy(donViKhoangCach = donVi) }

    // Đặt lại các biến khi bấm nút Hủy điều hướng ở trang Tìm kiếm
    fun datLaiTrangThaiHuyBo() {
        _trangThaiUi.update {
            it.copy(thanhPhoXemTruoc = null, chuoiTimKiemHienTai = "", danhSachGoiYTimKiem = emptyList(), thongBaoLoiNhapLieu = null)
        }
    }
}
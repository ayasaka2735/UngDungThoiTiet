package com.example.ungdungthoitiet.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ungdungthoitiet.data.DuLieuThoiTiet
import com.example.ungdungthoitiet.data.KhoDuLieuThoiTiet
import com.example.ungdungthoitiet.data.TrangThaiUiThoiTiet
import com.example.ungdungthoitiet.data.local.AppDatabase
import com.example.ungdungthoitiet.data.local.QuanLyCaiDat
import com.example.ungdungthoitiet.data.local.ThanhPhoEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoHinhXemThoiTiet(application: Application) : AndroidViewModel(application) {

    private val khoDuLieu = KhoDuLieuThoiTiet()
    private val quanLyCaiDat = QuanLyCaiDat(application)
    private val database = AppDatabase.layDatabase(application)
    private val thanhPhoDao = database.thanhPhoDao()

    private val _trangThaiUi = MutableStateFlow(TrangThaiUiThoiTiet())
    val uiState: StateFlow<TrangThaiUiThoiTiet> = _trangThaiUi.asStateFlow()

    private var congViecTimKiem: Job? = null

    init {
        khoiTaoCaiDatVaDuLieu()
    }

    /**
     * Khởi tạo cài đặt và danh sách thành phố yêu thích từ Room Database.
     */
    private fun khoiTaoCaiDatVaDuLieu() {
        viewModelScope.launch {
            // Đọc toàn bộ 4 thông số từ DataStore khi mở ứng dụng
            val donViNhietDoDaLuu = quanLyCaiDat.donViNhietDo.first()
            val donViGioDaLuu = quanLyCaiDat.donViGio.first()
            val donViLuongMuaDaLuu = quanLyCaiDat.donViLuongMua.first()
            val donViKhoangCachDaLuu = quanLyCaiDat.donViKhoangCach.first()

            _trangThaiUi.update {
                it.copy(
                    donViNhietDo = donViNhietDoDaLuu,
                    donViGio = donViGioDaLuu,
                    donViLuongMua = donViLuongMuaDaLuu,
                    donViKhoangCach = donViKhoangCachDaLuu
                )
            }

            val danhSachLuuTrongRoom = thanhPhoDao.layTatCa()
            if (danhSachLuuTrongRoom.isEmpty()) {
                taiDuLieuThoiTietMacDinh()
            } else {
                // HIỂN THỊ DỮ LIỆU CŨ TỪ ROOM TRƯỚC (CHẾ ĐỘ NGOẠI TUYẾN TỨC THÌ)
                val danhSachBanDau = danhSachLuuTrongRoom.map { entity ->
                    DuLieuThoiTiet(
                        tenThanhPho = entity.tenThanhPho,
                        nhietDo = entity.nhietDo,
                        trangThai = entity.trangThai,
                        doAm = entity.doAm,
                        tocDoGio = entity.tocDoGio,
                        apSuat = entity.apSuat,
                        nhietDoCaoNhat = entity.nhietDoCaoNhat,
                        nhietDoThapNhat = entity.nhietDoThapNhat,
                        duBaoTheoGio = entity.duBaoTheoGio,
                        duBaoTheoTuan = entity.duBaoTheoTuan,
                        iconId = entity.iconId
                    )
                }
                _trangThaiUi.update { it.copy(danhSachThanhPho = danhSachBanDau) }

                // Sau đó mới thử cập nhật từ mạng để đồng bộ dữ liệu mới nhất
                val danhSachTen = danhSachLuuTrongRoom.map { it.tenThanhPho }
                taiDuLieuTheoDanhSach(danhSachTen)
            }
        }
    }

    private fun taiDuLieuThoiTietMacDinh() {
        val macDinh = listOf("Hà Nội", "Hải Phòng", "Đà Nẵng")
        viewModelScope.launch {
            macDinh.forEach { ten ->
                thanhPhoDao.themThanhPho(ThanhPhoEntity(ten))
            }
            taiDuLieuTheoDanhSach(macDinh)
        }
    }

    private fun taiDuLieuTheoDanhSach(danhSachTen: List<String>) {
        viewModelScope.launch {
            try {
                _trangThaiUi.update { it.copy(dangTaiDuLieu = true) }
                val danhSachMoi = danhSachTen.map { ten ->
                    val data = khoDuLieu.layThoiTiet(ten)
                    // Đồng bộ dữ liệu mới nhất từ mạng xuống SQLite cục bộ làm bộ nhớ đệm ngoại tuyến
                    thanhPhoDao.themThanhPho(ThanhPhoEntity(
                        tenThanhPho = data.tenThanhPho,
                        nhietDo = data.nhietDo,
                        trangThai = data.trangThai,
                        doAm = data.doAm,
                        tocDoGio = data.tocDoGio,
                        apSuat = data.apSuat,
                        nhietDoCaoNhat = data.nhietDoCaoNhat,
                        nhietDoThapNhat = data.nhietDoThapNhat,
                        iconId = data.iconId,
                        duBaoTheoGio = data.duBaoTheoGio,
                        duBaoTheoTuan = data.duBaoTheoTuan
                    ))
                    data
                }
                _trangThaiUi.update {
                    it.copy(danhSachThanhPho = danhSachMoi, dangTaiDuLieu = false)
                }
            } catch (_: Exception) {
                // CHẾ ĐỘ NGOẠI TUYẾN (MẤT MẠNG): Trả ngược toàn bộ thông số lịch sử từ Room DB lên giao diện người dùng theo mục IV-B
                val danhSachEntity = thanhPhoDao.layTatCa()
                val danhSachOffline = danhSachEntity.map { entity ->
                    DuLieuThoiTiet(
                        tenThanhPho = entity.tenThanhPho,
                        nhietDo = entity.nhietDo,
                        trangThai = entity.trangThai,
                        doAm = entity.doAm,
                        tocDoGio = entity.tocDoGio,
                        apSuat = entity.apSuat,
                        nhietDoCaoNhat = entity.nhietDoCaoNhat,
                        nhietDoThapNhat = entity.nhietDoThapNhat,
                        duBaoTheoGio = entity.duBaoTheoGio,
                        duBaoTheoTuan = entity.duBaoTheoTuan,
                        iconId = entity.iconId
                    )
                }
                _trangThaiUi.update {
                    it.copy(danhSachThanhPho = danhSachOffline, dangTaiDuLieu = false)
                }
            }
        }
    }

    fun taiDuLieuThanhPhoChiTiet(tenThanhPho: String) {
        viewModelScope.launch {
            try {
                _trangThaiUi.update { it.copy(dangTaiDuLieu = true) }
                val duLieu = khoDuLieu.layThoiTiet(tenThanhPho)
                _trangThaiUi.update {
                    it.copy(
                        thanhPhoXemTruoc = duLieu,
                        danhSachGoiYTimKiem = emptyList(),
                        dangTaiDuLieu = false
                    )
                }
            } catch (_: Exception) {
                _trangThaiUi.update { it.copy(dangTaiDuLieu = false) }
            }
        }
    }

    fun capNhatChuoiTimKiemVaLocGoiY(chuoiNhap: String) {
        _trangThaiUi.update { it.copy(chuoiTimKiemHienTai = chuoiNhap, thanhPhoXemTruoc = null) }
        congViecTimKiem?.cancel()

        if (chuoiNhap.isNotEmpty()) {
            congViecTimKiem = viewModelScope.launch {
                delay(300)
                _trangThaiUi.update { it.copy(dangTaiDuLieu = true) }
                val goiY = khoDuLieu.timKiemThanhPho(chuoiNhap)
                _trangThaiUi.update {
                    it.copy(danhSachGoiYTimKiem = goiY, dangTaiDuLieu = false)
                }
            }
        } else {
            _trangThaiUi.update { it.copy(danhSachGoiYTimKiem = emptyList(), thongBaoLoiNhapLieu = null) }
        }
    }

    /**
     * Thêm thành phố vào danh sách và lưu vào Room Database.
     */
    fun themThanhPhoVaoTrangChu(thanhPho: DuLieuThoiTiet) {
        viewModelScope.launch {
            val danhSachHienTai = _trangThaiUi.value.danhSachThanhPho
            val daTonTai = danhSachHienTai.any {
                it.tenThanhPho.trim().equals(thanhPho.tenThanhPho.trim(), ignoreCase = true)
            }

            if (!daTonTai) {
                // Đóng gói lưu trữ toàn bộ các thông tin chi tiết vào Room hỗ trợ chế độ xem offline
                thanhPhoDao.themThanhPho(ThanhPhoEntity(
                    tenThanhPho = thanhPho.tenThanhPho,
                    nhietDo = thanhPho.nhietDo,
                    trangThai = thanhPho.trangThai,
                    doAm = thanhPho.doAm,
                    tocDoGio = thanhPho.tocDoGio,
                    apSuat = thanhPho.apSuat,
                    nhietDoCaoNhat = thanhPho.nhietDoCaoNhat,
                    nhietDoThapNhat = thanhPho.nhietDoThapNhat,
                    iconId = thanhPho.iconId,
                    duBaoTheoGio = thanhPho.duBaoTheoGio,
                    duBaoTheoTuan = thanhPho.duBaoTheoTuan
                ))

                _trangThaiUi.update { trang ->
                    trang.copy(
                        danhSachThanhPho = trang.danhSachThanhPho + thanhPho,
                        thanhPhoXemTruoc = null,
                        chuoiTimKiemHienTai = "",
                        danhSachGoiYTimKiem = emptyList()
                    )
                }
            } else {
                _trangThaiUi.update {
                    it.copy(thanhPhoXemTruoc = null, chuoiTimKiemHienTai = "")
                }
            }
        }
    }

    /**
     * Xóa thành phố và cập nhật Room Database.
     */
    fun xoaThanhPhoKhoiTrangChu(tenThanhPho: String) {
        viewModelScope.launch {
            // Xóa khỏi Room
            thanhPhoDao.xoaThanhPho(tenThanhPho)

            _trangThaiUi.update { trang ->
                trang.copy(danhSachThanhPho = trang.danhSachThanhPho.filterNot { it.tenThanhPho == tenThanhPho })
            }
        }
    }

    fun chonThanhPhoXemChiTiet(thanhPho: DuLieuThoiTiet) {
        _trangThaiUi.update { it.copy(thanhPhoDuocChon = thanhPho) }
    }

    fun hienThiBangSuaCauHinh(hienThi: Boolean) = _trangThaiUi.update { it.copy(hienBangSuaCauHinh = hienThi) }
    fun kichHoatCheDoSuaDanhSach() = _trangThaiUi.update { it.copy(cheDoSuaDanhSach = true) }
    fun hoanThanhSuaDanhSach() = _trangThaiUi.update { it.copy(cheDoSuaDanhSach = false) }

    fun thayDoiDonViNhietDo(donVi: String) {
        viewModelScope.launch {
            quanLyCaiDat.luuDonViNhietDo(donVi)
            _trangThaiUi.update { it.copy(donViNhietDo = donVi) }
        }
    }

    fun thayDoiDonViGio(donVi: String) {
        viewModelScope.launch {
            quanLyCaiDat.luuDonViGio(donVi)
            _trangThaiUi.update { it.copy(donViGio = donVi) }
        }
    }

    fun thayDoiDonViLuongMua(donVi: String) {
        viewModelScope.launch {
            quanLyCaiDat.luuDonViLuongMua(donVi)
            _trangThaiUi.update { it.copy(donViLuongMua = donVi) }
        }
    }

    fun thayDoiDonViKhoangCach(donVi: String) {
        viewModelScope.launch {
            quanLyCaiDat.luuDonViKhoangCach(donVi)
            _trangThaiUi.update { it.copy(donViKhoangCach = donVi) }
        }
    }

    fun datLaiTrangThaiHuyBo() {
        _trangThaiUi.update {
            it.copy(thanhPhoXemTruoc = null, chuoiTimKiemHienTai = "", danhSachGoiYTimKiem = emptyList(), thongBaoLoiNhapLieu = null)
        }
    }
}
package com.example.ungdungthoitiet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ungdungthoitiet.data.DuLieuThoiTiet
import com.example.ungdungthoitiet.data.TrangThaiUiThoiTiet
import androidx.compose.ui.tooling.preview.Preview
@Composable
fun ManHinhTrangChu(
    trangThai: TrangThaiUiThoiTiet,
    onChonThanhPho: (DuLieuThoiTiet) -> Unit,
    onMoBangSuaCauHinh: () -> Unit,
    onBatCheDoSua: () -> Unit,
    onXongSuaDanhSach: () -> Unit,
    onBamXoaThanhPho: (String) -> Unit,
    onChuyenTimKiem: () -> Unit,
    onChuyenGioiThieu: () -> Unit,

    // Cặp tham số hàm xử lý đơn vị đo mở rộng cho bảng sửa con cùng lớp
    onDoiNhietDo: (String) -> Unit,
    onDoiGio: (String) -> Unit,
    onDoiLuongMua: (String) -> Unit,
    onDoiKhoangCach: (String) -> Unit,
    onDongBangSua: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // --- LỚP NỀN: GIAO DIỆN TRANG CHỦ CHÍNH (THIẾT KẾ PHẲNG) ---
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

            // Khối đầu trang xếp ngang: Đưa nút Tìm kiếm lên cùng hàng với nút Sửa (Đã xóa tiêu đề)
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, top = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onChuyenTimKiem, shape = MaterialTheme.shapes.small) {
                    Text(text = "Tìm kiếm thành phố")
                }

                // Logic hoán đổi tự động giữa nút Sửa đổi danh sách và nút Xong theo yêu cầu mới
                if (trangThai.cheDoSuaDanhSach) {
                    Button(onClick = onXongSuaDanhSach, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
                        Text(text = "Xong", color = Color.White)
                    }
                } else {
                    Row {
                        Button(onClick = onMoBangSuaCauHinh, shape = MaterialTheme.shapes.small) {
                            Text(text = "Cài đặt")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Button(onClick = onBatCheDoSua, shape = MaterialTheme.shapes.small) {
                            Text(text = "Sửa")
                        }
                    }
                }
            }

            // Danh sách hiển thị các khối thành phố
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(trangThai.danhSachThanhPho) { thanhPho ->

                    // --- ĐỒNG BỘ GIÁ TRỊ SỐ NHIỆT ĐỘ TRÊN TRANG CHỦ BẰNG IF - ELSE ---
                    var tempHienTai = thanhPho.nhietDo
                    var tempMax = thanhPho.nhietDoCaoNhat
                    var tempMin = thanhPho.nhietDoThapNhat

                    if (trangThai.donViNhietDo == "°F") {
                        tempHienTai = (thanhPho.nhietDo * 9 / 5) + 32
                        tempMax = (thanhPho.nhietDoCaoNhat * 9 / 5) + 32
                        tempMin = (thanhPho.nhietDoThapNhat * 9 / 5) + 32
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onChonThanhPho(thanhPho) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Triệt tiêu đổ bóng theo ảnh thô
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Vị trí: ${thanhPho.tenThanhPho}", fontSize = 16.sp, fontWeight = FontWeight.Medium)

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "Hiện tại", fontSize = 14.sp, color = Color.Gray)

                                    // Vẫn hiện nút xóa bên cạnh tên thành phố khi nhấn nút Sửa danh sách
                                    if (trangThai.cheDoSuaDanhSach) {
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Button(
                                            onClick = { onBamXoaThanhPho(thanhPho.tenThanhPho) },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.height(28.dp)
                                        ) {
                                            Text(text = "Xóa", fontSize = 12.sp, color = Color.White)
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Tình trạng thời tiết hiện tại: ${thanhPho.trangThai}", fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(8.dp))

                            // Sử dụng giá trị số đã quy đổi thực tế
                            Text(text = "Nhiệt độ hiện tại: ${tempHienTai}${trangThai.donViNhietDo}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text(text = "Nhiệt độ cao nhất: ${tempMax}°", fontSize = 13.sp, color = Color(0xFFD32F2F))
                                Text(text = "Nhiệt độ thấp nhất: ${tempMin}°", fontSize = 13.sp, color = Color(0xFF1976D2))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(1.dp))
            Button(onClick = onChuyenGioiThieu, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                Text(text = "Về chúng tôi")
            }
        }

        // --- LỚP NỔI BỀ MẶT: BẢNG SỬA CẤU HÌNH CON HIỂN THỊ ĐÈ LÊN TRANG CHỦ ---
        if (trangThai.hienBangSuaCauHinh) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)).clickable { onDongBangSua() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f).background(Color.White, shape = MaterialTheme.shapes.medium).padding(16.dp).clickable(enabled = false) { }
                ) {
                    Text(text = "Bảng tùy chỉnh thông số hệ thống", fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

                    ThanhChonTieuChuan(tieuDe = "Tùy chọn Đơn vị Nhiệt độ:", chon1 = "°C", chon2 = "°F", giaTriGoc = trangThai.donViNhietDo, onKichHoat = onDoiNhietDo)
                    ThanhChonTieuChuan(tieuDe = "Tùy chọn Đơn vị Tốc độ gió:", chon1 = "km/h", chon2 = "mph", giaTriGoc = trangThai.donViGio, onKichHoat = onDoiGio)
                    ThanhChonTieuChuan(tieuDe = "Tùy chọn Đơn vị Lượng mưa:", chon1 = "mm", chon2 = "inch", giaTriGoc = trangThai.donViLuongMua, onKichHoat = onDoiLuongMua)
                    ThanhChonTieuChuan(tieuDe = "Tùy chọn Đơn vị Khoảng cách:", chon1 = "km", chon2 = "mi", giaTriGoc = trangThai.donViKhoangCach, onKichHoat = onDoiKhoangCach)

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onDongBangSua, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Xác nhận và Đóng cấu hình")
                    }
                }
            }
        }
    }
}

@Composable
private fun ThanhChonTieuChuan(tieuDe: String, chon1: String, chon2: String, giaTriGoc: String, onKichHoat: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = tieuDe, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.Gray)
        Row(modifier = Modifier.fillMaxWidth().selectableGroup(), verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.selectable(selected = (giaTriGoc == chon1), onClick = { onKichHoat(chon1) }, role = Role.RadioButton)) {
                RadioButton(selected = (giaTriGoc == chon1), onClick = null)
                Text(text = chon1, modifier = Modifier.padding(start = 4.dp), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(32.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.selectable(selected = (giaTriGoc == chon2), onClick = { onKichHoat(chon2) }, role = Role.RadioButton)) {
                RadioButton(selected = (giaTriGoc == chon2), onClick = null)
                Text(text = chon2, modifier = Modifier.padding(start = 4.dp), fontSize = 14.sp)
            }
        }
    }
}

@Preview(showBackground = true, name = "Xem trước Trang chủ Phẳng")
@Composable
fun XemTruocManHinhTrangChuPhang() {
    val thanhPhoMau = DuLieuThoiTiet(
        tenThanhPho = "Hạ Long",
        nhietDo = 26,
        trangThai = "Trời trong xanh",
        doAm = 70,
        tocDoGio = 15.0,
        apSuat = 1012,
        nhietDoCaoNhat = 28,
        nhietDoThapNhat = 24,
        duBaoTheoGio = emptyList(),
        duBaoTheoTuan = emptyList()
    )

    ManHinhTrangChu(
        trangThai = TrangThaiUiThoiTiet(
            danhSachThanhPho = listOf(thanhPhoMau),
            donViNhietDo = "°F",
            cheDoSuaDanhSach = false
        ),
        onChonThanhPho = {},
        onMoBangSuaCauHinh = {},
        onBatCheDoSua = {},
        onXongSuaDanhSach = {},
        onBamXoaThanhPho = {},
        onChuyenTimKiem = {},
        onChuyenGioiThieu = {},
        onDoiNhietDo = {},
        onDoiGio = {},
        onDoiLuongMua = {},
        onDoiKhoangCach = {},
        onDongBangSua = {}
    )
}
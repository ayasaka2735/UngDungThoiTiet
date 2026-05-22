package com.example.ungdungthoitiet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ungdungthoitiet.data.DuLieuThoiTiet
import com.example.ungdungthoitiet.data.TrangThaiUiThoiTiet

@Composable
fun ManHinhTimKiem(
    trangThai: TrangThaiUiThoiTiet,
    onGocChuThayDoi: (String) -> Unit,           // Tự động kích hoạt bắt gợi ý khi nhấn bất kỳ một chữ nào
    onChonThanhPhoGoiY: (DuLieuThoiTiet) -> Unit, // Bấm vào dòng chữ gợi ý để bung màn hình xem trước
    onXacNhanThem: (DuLieuThoiTiet) -> Unit,
    onBamNutHuyBo: () -> Unit                    // Nút hủy thay thế hoàn toàn để back về màn hình chính
) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp)) {
        Text(text = "Tìm kiếm & Xem trước thành phố", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))

        // Thanh tìm kiếm mới: Xếp hàng ngang với nút Hủy để quay về nhanh
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = trangThai.chuoiTimKiemHienTai,
                onValueChange = { onGocChuThayDoi(it) }, // Lắng nghe bắt ký tự gõ phím liên tục
                label = { Text("Tìm Kiếm Tên thành phố") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onBamNutHuyBo) {
                Text(text = "Hủy")
            }
        }

        // BẮT LỖI HOÀN TOÀN TRONG TÀI LIỆU: Nếu gõ bừa gán chữ 'Không tìm thấy kết quả' văn bản màu đỏ
        if (trangThai.thongBaoLoiNhapLieu != null) {
            Text(text = trangThai.thongBaoLoiNhapLieu, color = Color.Red, fontSize = 14.sp, modifier = Modifier.fillMaxWidth().padding(top = 8.dp, start = 8.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- KHU VỰC DANH SÁCH GỢI Ý ĐỔ XUỐNG KHI PHÍM ĐƯỢC NHẤN CHỮ (Giải pháp co giãn chuẩn bài 3A-1) ---
        if (trangThai.thanhPhoXemTruoc == null && trangThai.danhSachGoiYTimKiem.isNotEmpty()) {
            Text(text = "Gợi ý khớp từ khóa trong hệ thống học tập:", fontSize = 13.sp, color = Color.Gray)

            // Sử dụng thuộc tính co giãn .weight(1f) và .fillMaxWidth() thay thế cho heightIn ngoại luồng
            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 4.dp)) {
                items(trangThai.danhSachGoiYTimKiem) { goiY ->
                    Column(
                        modifier = Modifier.fillMaxWidth().clickable { onChonThanhPhoGoiY(goiY) }.padding(vertical = 12.dp, horizontal = 4.dp)
                    ) {
                        Text(text = "📍 Khớp tìm thấy: ${goiY.tenThanhPho}", fontSize = 16.sp, fontWeight = FontWeight.Normal)
                        HorizontalDivider(modifier = Modifier.padding(top = 10.dp), thickness = 0.5.dp, color = Color.LightGray)
                    }
                }
            }
        }

        // Khung Xem trước phẳng nằm lồng cố định bên dưới ô tìm kiếm khi click chọn từ danh sách gợi ý
        if (trangThai.thanhPhoXemTruoc != null) {
            val ketQua = trangThai.thanhPhoXemTruoc
            Column(
                modifier = Modifier.fillMaxWidth().background(Color(0xFFF5F5F5)).padding(16.dp)
            ) {
                Text(text = "CHẾ ĐỘ XEM TRƯỚC (CHƯA LƯU HỆ THỐNG)", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(bottom = 12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Vị trí thành phố: ${ketQua.tenThanhPho}", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Text(text = "Thời gian: Hiện tại", fontSize = 14.sp, color = Color.DarkGray)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Tình trạng thời tiết: ${ketQua.trangThai}", fontSize = 15.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Nhiệt độ hiện tại: ${ketQua.nhietDo}${trangThai.donViNhietDo}", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = "Nhiệt độ cao nhất: ${ketQua.nhietDoCaoNhat}°", fontSize = 13.sp, color = Color(0xFFD32F2F))
                    Text(text = "Nhiệt độ thấp nhất: ${ketQua.nhietDoThapNhat}°", fontSize = 13.sp, color = Color(0xFF1976D2))
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(onClick = onBamNutHuyBo, modifier = Modifier.weight(1f)) {
                        Text(text = "Hủy")
                    }
                    Button(onClick = { onXacNhanThem(ketQua) }, modifier = Modifier.weight(1f)) {
                        Text(text = "Thêm")
                    }
                }
            }
        } else if (trangThai.danhSachGoiYTimKiem.isEmpty() && trangThai.thongBaoLoiNhapLieu == null) {
            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(text = "Nhập bất kỳ một chữ nào để kích hoạt khung gợi ý thông minh.", color = Color.Gray, textAlign = TextAlign.Center, fontSize = 15.sp)
            }
        }
    }
}
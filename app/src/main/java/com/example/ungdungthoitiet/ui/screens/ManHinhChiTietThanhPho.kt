package com.example.ungdungthoitiet.ui.screens

import androidx.compose.foundation.background // Import để thiết lập nền màu
import androidx.compose.foundation.layout.* // Import các thành phần bố cục như Column, Row, Spacer
import androidx.compose.foundation.lazy.LazyColumn // Import danh sách cuộn dọc tối ưu theo bài 3A-1
import androidx.compose.foundation.lazy.LazyRow // Import danh sách cuộn ngang tối ưu theo bài 3A-1
import androidx.compose.foundation.lazy.items // Import hàm hỗ trợ duyệt danh sách
import androidx.compose.material3.* // Import các thành phần giao diện Material Design 3
import androidx.compose.runtime.Composable // Import annotation định nghĩa hàm giao diện
import androidx.compose.ui.Alignment // Import để căn chỉnh các thành phần
import androidx.compose.ui.Modifier // Import để tùy chỉnh thuộc tính của thành phần
import androidx.compose.ui.graphics.Color // Import lớp quản lý màu sắc
import androidx.compose.ui.text.font.FontWeight // Import định dạng chữ đậm
import androidx.compose.ui.text.style.TextAlign // Import để căn lề văn bản
import androidx.compose.ui.unit.dp // Import đơn vị đo mật độ điểm ảnh
import androidx.compose.ui.unit.sp // Import đơn vị đo kích thước chữ
import androidx.compose.ui.tooling.preview.Preview // Import chuẩn sửa lỗi Unresolved reference theo bài 1_3
import com.example.ungdungthoitiet.data.DuLieuThoiTiet // Import thực thể dữ liệu thành phố
import com.example.ungdungthoitiet.data.TrangThaiUiThoiTiet // Import trạng thái giao diện tổng quát

/**
 * Hàm giao diện màn hình Chi tiết thành phố chỉnh sửa cấu trúc phẳng.
 * Bố trí 24 giờ dạng khối dọc cuộn ngang, 7 ngày hiện icon kèm khoảng nhiệt độ chạy từ thấp đến cao,
 * BỐ TRÍ MỚI: 4 thông số phụ (Độ ẩm, Gió, Mưa, Tầm nhìn) được tách hẳn thành 4 ô riêng biệt, xếp 2 ô một hàng ngang cân đối.
 */
@Composable
fun ManHinhChiTietThanhPho(
    trangThai: TrangThaiUiThoiTiet, // Biến trạng thái chứa toàn bộ dữ liệu hiển thị
    onQuayLai: () -> Unit // Sự kiện khi người dùng nhấn nút quay lại
) {
    // Lấy thực thể thành phố đang được chọn từ trạng thái UI
    val thanhPho = trangThai.thanhPhoDuocChon

    // Chỉ thực hiện vẽ giao diện nếu có dữ liệu thành phố khác null (Bảo vệ hệ thống không bị crash)
    if (thanhPho != null) {

        // --- KHU VỰC LOGIC QUY ĐỔI GIÁ TRỊ NHIỆT ĐỘ SỬ DỤNG IF - ELSE CƠ BẢN ---
        var nhietDoHienTaiHienThi = thanhPho.nhietDo
        var nhietDoCaoNhatHienThi = thanhPho.nhietDoCaoNhat
        var nhietDoThapNhatHienThi = thanhPho.nhietDoThapNhat

        if (trangThai.donViNhietDo == "°F") {
            // Công thức vật lý quy đổi từ độ C sang độ F cơ bản: (C * 9/5) + 32
            nhietDoHienTaiHienThi = (thanhPho.nhietDo * 9 / 5) + 32
            nhietDoCaoNhatHienThi = (thanhPho.nhietDoCaoNhat * 9 / 5) + 32
            nhietDoThapNhatHienThi = (thanhPho.nhietDoThapNhat * 9 / 5) + 32
        }

        // --- KHU VỰC LOGIC QUY ĐỔI GIÁ TRỊ TỐC ĐỘ GIÓ SỬ DỤNG IF - ELSE CƠ BẢN ---
        var tocDoGioHienThi = thanhPho.tocDoGio
        if (trangThai.donViGio == "mph") {
            // Công thức quy đổi tốc độ gió từ km/h sang mph hệ số 0.62137
            tocDoGioHienThi = thanhPho.tocDoGio * 0.62137
        }

        // --- KHU VỰC LOGIC QUY ĐỔI GIÁ TRỊ LƯỢNG MƯA SỬ DỤNG IF - ELSE CƠ BẢN ---
        var luongMuaHienThi = 5.0 // Sử dụng dữ liệu mồi mặc định hệ thống là 5mm
        if (trangThai.donViLuongMua == "inch") {
            // Quy đổi từ lượng mưa mm sang inch: chia cho hệ số 25.4
            luongMuaHienThi = 5.0 / 25.4
        }

        // --- KHU VỰC LOGIC QUY ĐỔI GIÁ TRỊ KHOẢNG CÁCH TẦM NHÌN SỬ DỤNG IF - ELSE CƠ BẢN ---
        var khoangCachHienThi = 10.0 // Sử dụng dữ liệu tầm nhìn xa mồi mặc định là 10km
        if (trangThai.donViKhoangCach == "mi") {
            // Quy đổi khoảng cách tầm nhìn từ km sang dặm (mi): nhân với hệ số 0.62137
            khoangCachHienThi = 10.0 * 0.62137
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // --- Tiêu đề đầu trang văn bản phẳng ---
            Text(
                text = "Giao diện trang chi tiết",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // --- Khối thông tin thời tiết tổng quát ở phần trên cùng ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.medium)
                    .padding(12.dp)
            ) {
                Text(
                    text = "Vị trí hiện tại: ${thanhPho.tenThanhPho}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${nhietDoHienTaiHienThi}${trangThai.donViNhietDo}",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tình trạng thời tiết: ${thanhPho.trangThai}",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Sắp xếp hiển thị từ nhiệt độ Thấp nhất lên Cao nhất trong ngày theo đúng yêu cầu
                    Text(text = "Thấp nhất: ${nhietDoThapNhatHienThi}°", color = Color(0xFF1976D2), fontSize = 13.sp)
                    Text(text = "Cao nhất: ${nhietDoCaoNhatHienThi}°", color = Color(0xFFD32F2F), fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 1. KHU VỰC DỰ BÁO THỜI TIẾT TRONG 24 GIỜ (CUỘN NGANG - KHỐI DỌC ICON THỰC TẾ) ---
            Text(
                text = "Dự báo thời tiết trong 24 giờ",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(thanhPho.duBaoTheoGio) { dongDuBaoGio ->

                    var iconThoiTietThucTe = "☀️"
                    if (thanhPho.trangThai == "Nhiều mây" || thanhPho.trangThai == "Mây rải rác") {
                        iconThoiTietThucTe = "☁️"
                    }
                    if (thanhPho.trangThai == "Có mưa nhỏ" || thanhPho.trangThai == "Có mưa dông") {
                        iconThoiTietThucTe = "🌧️"
                    }

                    val mốcGioHienThi = dongDuBaoGio.substringBefore(" -")

                    Column(
                        modifier = Modifier
                            .background(Color(0xFFFAFAFA), shape = MaterialTheme.shapes.small)
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = mốcGioHienThi, fontSize = 13.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = iconThoiTietThucTe, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))

                        var nhietDoGioHienThi = thanhPho.nhietDo
                        if (trangThai.donViNhietDo == "°F") {
                            nhietDoGioHienThi = (thanhPho.nhietDo * 9 / 5) + 32
                        }
                        Text(text = "${nhietDoGioHienThi}${trangThai.donViNhietDo}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 2. KHU VỰC DỰ BÁO THỜI TIẾT 7 NGÀY (CUỘN DỌC - CHÈN ICON VÀO GIỮA - THẤP ĐẾN CAO) ---
            Text(
                text = "Dự báo thời tiết 7 ngày",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(thanhPho.duBaoTheoTuan) { dongDuBaoTuan ->
                    val tenNgayTrongTuan = dongDuBaoTuan.substringBefore(":")

                    var icon7NgayThucTe = "☀️"
                    if (thanhPho.trangThai == "Nhiều mây" || thanhPho.trangThai == "Mây rải rác") {
                        icon7NgayThucTe = "☁️"
                    }
                    if (thanhPho.trangThai == "Có mưa nhỏ" || thanhPho.trangThai == "Có mưa dông") {
                        icon7NgayThucTe = "🌧️"
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFAFAFA))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = tenNgayTrongTuan, modifier = Modifier.weight(1f), fontSize = 14.sp)
                        Text(text = icon7NgayThucTe, modifier = Modifier.padding(horizontal = 8.dp), fontSize = 16.sp)
                        Text(
                            text = "${thanhPho.nhietDoThapNhat}${trangThai.donViNhietDo} - ${thanhPho.nhietDoCaoNhat}${trangThai.donViNhietDo}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- 3. KHU VỰC 4 THÔNG SỐ PHỤ DƯỚI CÙNG (SỬA ĐỔI: TÁCH BIỆT THÀNH 4 Ô PHẲNG RIÊNG BIỆT, 2 Ô 1 ROW) ---
            Text(
                text = "Thông số bổ trợ môi trường quy đổi:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )

            // Định dạng lưới phẳng bằng cách lồng ghép Column x Row cơ bản theo tài liệu
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách dọc giữa 2 hàng ngang
            ) {
                // HÀNG NGANG THỨ NHẤT (Row 1): Chứa 2 ô (Ô Độ ẩm - Ô Tốc độ gió)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách ngang giữa 2 ô
                ) {
                    // Ô số 1: Độ ẩm không khí độc lập
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.small)
                            .padding(10.dp)
                    ) {
                        Text(text = "Độ ẩm", fontSize = 11.sp, color = Color.Gray)
                        Text(text = "${thanhPho.doAm}%", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    // Ô số 2: Tốc độ gió độc lập
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.small)
                            .padding(10.dp)
                    ) {
                        val chuoiGioSua = String.format(java.util.Locale.US, "%.1f", tocDoGioHienThi)
                        Text(text = "Tốc độ gió", fontSize = 11.sp, color = Color.Gray)
                        Text(text = "${chuoiGioSua} ${trangThai.donViGio}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // HÀNG NGANG THỨ HAI (Row 2): Chứa 2 ô (Ô Lượng mưa - Ô Khoảng cách tầm nhìn)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // Khoảng cách ngang giữa 2 ô
                ) {
                    // Ô số 3: Lượng mưa độc lập
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.small)
                            .padding(10.dp)
                    ) {
                        val chuoiMuaSua = String.format(java.util.Locale.US, "%.2f", luongMuaHienThi)
                        Text(text = "Lượng mưa", fontSize = 11.sp, color = Color.Gray)
                        Text(text = "${chuoiMuaSua} ${trangThai.donViLuongMua}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }

                    // Ô số 4: Khoảng cách tầm nhìn xa độc lập
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.small)
                            .padding(10.dp)
                    ) {
                        val chuoiKhoangCachSua = String.format(java.util.Locale.US, "%.1f", khoangCachHienThi)
                        Text(text = "Tầm nhìn xa", fontSize = 11.sp, color = Color.Gray)
                        Text(text = "${chuoiKhoangCachSua} ${trangThai.donViKhoangCach}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Nút bấm quay lại rộng tối đa ở đáy màn hình ---
            Button(
                onClick = onQuayLai,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Quay lại màn hình chính Dashboard", fontSize = 15.sp)
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Không có dữ liệu thành phố.", color = Color.Gray)
        }
    }
}

/**
 * Hàm hỗ trợ xem trước cấu trúc giao diện màn hình Chi tiết phẳng ngay trong Android Studio.
 */
@Preview(showBackground = true, name = "Xem trước Chi tiết Phẳng")
@Composable
fun XemTruocManHinhChiTietThanhPhoPhang() {
    val thanhPhoMau = DuLieuThoiTiet(
        tenThanhPho = "Hạ Long",
        nhietDo = 26,
        trangThai = "Trời trong xanh",
        doAm = 70,
        tocDoGio = 15.0,
        apSuat = 1012,
        nhietDoCaoNhat = 28,
        nhietDoThapNhat = 24,
        duBaoTheoGio = listOf("09:00 - 25°C", "12:00 - 27°C"),
        duBaoTheoTuan = listOf("Thứ 2: 26°C", "Thứ 3: 27°C")
    )

    ManHinhChiTietThanhPho(
        trangThai = TrangThaiUiThoiTiet(thanhPhoDuocChon = thanhPhoMau, donViNhietDo = "°C", donViGio = "km/h", donViLuongMua = "mm", donViKhoangCach = "km"),
        onQuayLai = {}
    )
}
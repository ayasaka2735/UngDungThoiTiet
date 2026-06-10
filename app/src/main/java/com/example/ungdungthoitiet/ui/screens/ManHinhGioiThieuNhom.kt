package com.example.ungdungthoitiet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.ungdungthoitiet.R

@Composable
fun ManHinhGioiThieuNhom(
    onQuayLaiTrangChu: () -> Unit
) {
    val mauNutPhu = Color.White.copy(alpha = 0.2f)
    val mauTheTrongSuot = Color.Black.copy(alpha = 0.5f)
    val mauChuChinh = Color.White
    val mauChuPhu = Color(0xFFE0E0E0)
    val mauNhan = Color(0xFFBB86FC)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.anhthoitiet),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp).padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Về chúng tôi - WeatherNow",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp),
                color = mauChuChinh
            )

            Text(
                text = "☁️",
                fontSize = 80.sp
            )
            
            Text(
                text = "WeatherNow Application v1.0",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = mauChuChinh
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = mauTheTrongSuot),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "LÝ DO THIẾT KẾ ỨNG DỤNG (REQUIREMENT)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp),
                        color = mauNhan
                    )
                    Text(
                        text = "Thời tiết tại khu vực tỉnh Quảng Ninh nói riêng và cả nước nói chung ngày càng diễn biến phức tạp, biến đổi thất thường do hiện tượng biến đổi khí hậu toàn cầu. Ứng dụng WeatherNow được nghiên cứu phát triển nhằm cung cấp giải pháp tra cứu thông tin thời tiết trực quan, nhanh chóng theo thời gian thực, giúp người dùng chủ động bảo vệ sức khỏe và tối ưu hóa kế hoạch di chuyển, lao động hàng daily hàng ngày.",
                        textAlign = TextAlign.Justify,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = mauChuPhu
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = mauTheTrongSuot)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "HỆ THỐNG PHÂN CHIA THÀNH VIÊN NHÓM & ĐÓNG GÓP",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 12.dp),
                        color = mauNhan
                    )
                    
                    ThongTinThanhVien(hoTen = "Thành viên 01", vaiTro = "Nhóm trưởng - Code Logic", dongGop = "100%", mauChuChinh, mauChuPhu, mauNhan)
                    ThongTinThanhVien(hoTen = "Thành viên 02", vaiTro = "Thiết kế UI/UX - Đồ họa", dongGop = "100%", mauChuChinh, mauChuPhu, mauNhan)
                    ThongTinThanhVien(hoTen = "Thành viên 03", vaiTro = "Xử lý Dữ liệu - API", dongGop = "100%", mauChuChinh, mauChuPhu, mauNhan)
                    ThongTinThanhVien(hoTen = "Thành viên 04", vaiTro = "Kiểm thử - Viết báo cáo", dongGop = "100%", mauChuChinh, mauChuPhu, mauNhan)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "GVHD: Thầy Vũ Duy Sơn | Trường Đại học Hạ Long",
                fontSize = 14.sp,
                color = mauChuPhu,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onQuayLaiTrangChu,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = mauNutPhu)
                ) {
                    Text(
                        text = "Quay lại Màn hình chính",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
        }
    }
}

@Composable
private fun ThongTinThanhVien(hoTen: String, vaiTro: String, dongGop: String, mauChinh: Color, mauPhu: Color, mauNhan: Color) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = hoTen, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = mauChinh)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = vaiTro, fontSize = 12.sp, color = mauPhu)
            Text(text = "Đóng góp: $dongGop", fontSize = 12.sp, color = mauNhan)
        }
        HorizontalDivider(modifier = Modifier.padding(top = 4.dp), thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.3f))
    }
}

@Preview(showBackground = true, name = "Xem trước Giới thiệu")
@Composable
fun XemTruocManHinhGioiThieuNhom() {
    ManHinhGioiThieuNhom(onQuayLaiTrangChu = {})
}

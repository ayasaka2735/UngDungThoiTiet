package com.example.ungdungthoitiet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.ungdungthoitiet.R
import com.example.ungdungthoitiet.data.DuLieuThoiTiet
import com.example.ungdungthoitiet.data.TrangThaiUiThoiTiet
import coil.compose.AsyncImage

@Composable
fun ManHinhChiTietThanhPho(
    trangThai: TrangThaiUiThoiTiet,
    onQuayLai: () -> Unit,
    onMoCaiDat: () -> Unit,
    onDoiNhietDo: (String) -> Unit,
    onDoiGio: (String) -> Unit,
    onDoiLuongMua: (String) -> Unit,
    onDoiKhoangCach: (String) -> Unit,
    onDongBangSua: () -> Unit
) {
    val thanhPho = trangThai.thanhPhoDuocChon
    val mauNutPhu = Color.White.copy(alpha = 0.2f)
    val mauTheTrongSuot = Color.Black.copy(alpha = 0.5f)
    val mauChuChinh = Color.White
    val mauChuPhu = Color(0xFFE0E0E0)

    if (thanhPho != null) {
        val laDoF = trangThai.donViNhietDo == "°F"
        fun doiNhietDo(c: Int) = if (laDoF) (c * 9 / 5) + 32 else c

        fun layNhietDo(s: String): Int {
            return try {
                s.substringAfter("- ").substringBefore("°").trim().toInt()
            } catch (e: Exception) {
                0
            }
        }

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
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Chi tiết thời tiết",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = mauChuChinh
                    )
                    Button(onClick = onMoCaiDat, colors = ButtonDefaults.buttonColors(containerColor = mauNutPhu)) {
                        Text("Cài đặt", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = mauTheTrongSuot)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Vị trí: ${thanhPho.tenThanhPho}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = mauChuChinh)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${doiNhietDo(thanhPho.nhietDo)}${trangThai.donViNhietDo}",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFBB86FC)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            AsyncImage(
                                model = "https://openweathermap.org/img/wn/${thanhPho.iconId}@4x.png",
                                contentDescription = "Mô tả ảnh thời tiết",
                                modifier = Modifier.size(80.dp)
                            )
                        }
                        Text(text = "Trạng thái: ${thanhPho.trangThai}", fontSize = 16.sp, color = mauChuPhu)
                        Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(text = "Thấp nhất: ${doiNhietDo(thanhPho.nhietDoThapNhat)}°", color = Color(0xFF448AFF))
                            Text(text = "Cao nhất: ${doiNhietDo(thanhPho.nhietDoCaoNhat)}°", color = Color(0xFFFF5252))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Dự báo trong 24 giờ", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = mauChuChinh)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(thanhPho.duBaoTheoGio) { item ->
                        val thoiGian = item.substringBefore(" -")
                        val nhietDo = doiNhietDo(layNhietDo(item))
                        Column(
                            modifier = Modifier
                                .background(mauTheTrongSuot, shape = MaterialTheme.shapes.small)
                                .padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = thoiGian, fontSize = 12.sp, color = mauChuPhu)
                            Text(text = "${nhietDo}°", fontWeight = FontWeight.Bold, color = mauChuChinh)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Dự báo 5 ngày tới", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = mauChuChinh)
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    thanhPho.duBaoTheoTuan.forEach { item ->
                        val ngayThang = item.substringBefore(" -")
                        val nhietDo = doiNhietDo(layNhietDo(item))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(mauTheTrongSuot, shape = MaterialTheme.shapes.small)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = ngayThang, modifier = Modifier.weight(1f), color = mauChuChinh)
                            Text(text = "${nhietDo}${trangThai.donViNhietDo}", fontWeight = FontWeight.Bold, color = mauChuChinh)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Thông số bổ trợ", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = mauChuChinh)
                Spacer(modifier = Modifier.height(8.dp))

                val tocDoGioHienThi = if (trangThai.donViGio == "mph") thanhPho.tocDoGio * 0.62137 else thanhPho.tocDoGio
                val luongMuaHienThi = if (trangThai.donViLuongMua == "inch") 5.0 / 25.4 else 5.0
                val khoangCachHienThi = if (trangThai.donViKhoangCach == "mi") 10.0 * 0.62137 else 10.0

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OThongSo("Độ ẩm", "${thanhPho.doAm}%", Modifier.weight(1f), mauTheTrongSuot, mauChuChinh, mauChuPhu)
                        OThongSo("Tốc độ gió", "${String.format(java.util.Locale.US, "%.1f", tocDoGioHienThi)} ${trangThai.donViGio}", Modifier.weight(1f), mauTheTrongSuot, mauChuChinh, mauChuPhu)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OThongSo("Lượng mưa", "${String.format(java.util.Locale.US, "%.2f", luongMuaHienThi)} ${trangThai.donViLuongMua}", Modifier.weight(1f), mauTheTrongSuot, mauChuChinh, mauChuPhu)
                        OThongSo("Áp suất", "${thanhPho.apSuat} hPa", Modifier.weight(1f), mauTheTrongSuot, mauChuChinh, mauChuPhu)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OThongSo("Tầm nhìn xa", "${String.format(java.util.Locale.US, "%.1f", khoangCachHienThi)} ${trangThai.donViKhoangCach}", Modifier.weight(1f), mauTheTrongSuot, mauChuChinh, mauChuPhu)
                        Spacer(Modifier.weight(1f))
                    }
                }

                Button(
                    onClick = onQuayLai,
                    modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 30.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = mauNutPhu)
                ) {
                    Text(text = "Quay lại màn hình chính", fontSize = 15.sp, color = Color.White)
                }
            }

            if (trangThai.hienBangSuaCauHinh) {
                BangCaiDat(
                    trangThai = trangThai,
                    onDoiNhietDo = onDoiNhietDo,
                    onDoiGio = onDoiGio,
                    onDoiLuongMua = onDoiLuongMua,
                    onDoiKhoangCach = onDoiKhoangCach,
                    onDong = onDongBangSua
                )
            }
        }
    }
}

@Composable
private fun OThongSo(nhan: String, giaTri: String, modifier: Modifier = Modifier, mauNen: Color, mauChinh: Color, mauPhu: Color) {
    Column(
        modifier = modifier
            .background(mauNen, shape = MaterialTheme.shapes.small)
            .padding(12.dp)
    ) {
        Text(text = nhan, fontSize = 11.sp, color = mauPhu)
        Text(text = giaTri, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = mauChinh)
    }
}

@Preview(showBackground = true)
@Composable
fun XemTruocChiTiet() {
    val mau = DuLieuThoiTiet("Hà Nội", 34, "Mây rải rác", 58, 5.0, 1005, 35, 27, listOf("Bây giờ - 34°C (mây)"), listOf("Ngày mai - 34°C (mây)"), "01d")
    ManHinhChiTietThanhPho(TrangThaiUiThoiTiet(thanhPhoDuocChon = mau), {}, {}, {}, {}, {}, {}, {})
}

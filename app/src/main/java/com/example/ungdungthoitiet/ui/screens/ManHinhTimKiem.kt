package com.example.ungdungthoitiet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ungdungthoitiet.R
import com.example.ungdungthoitiet.data.DuLieuThoiTiet
import com.example.ungdungthoitiet.data.TrangThaiUiThoiTiet
import coil.compose.AsyncImage

@Composable
fun ManHinhTimKiem(
    trangThai: TrangThaiUiThoiTiet,
    onGocChuThayDoi: (String) -> Unit,
    onChonGoiY: (String) -> Unit,
    onXacNhanThem: (DuLieuThoiTiet) -> Unit,
    onBamNutHuyBo: () -> Unit
) {
    val mauNutPhu = Color.White.copy(alpha = 0.2f)
    val mauTheTrongSuot = Color.Black.copy(alpha = 0.5f)
    val mauChuChinh = Color.White
    val mauChuPhu = Color(0xFFE0E0E0)

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
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {}
                )
                .padding(16.dp)
        ) {
            Text(text = "Tìm kiếm thành phố", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp), color = mauChuChinh)

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = trangThai.chuoiTimKiemHienTai,
                    onValueChange = { onGocChuThayDoi(it) },
                    label = { Text("Nhập tên thành phố...", color = mauChuPhu) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = mauChuChinh,
                        unfocusedTextColor = mauChuChinh,
                        focusedBorderColor = Color(0xFFBB86FC),
                        unfocusedBorderColor = mauChuPhu,
                        focusedContainerColor = Color.Black.copy(alpha = 0.3f),
                        unfocusedContainerColor = Color.Black.copy(alpha = 0.3f)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onBamNutHuyBo, colors = ButtonDefaults.buttonColors(containerColor = mauNutPhu)) {
                    Text("Hủy", color = Color.White)
                }
            }

            if (trangThai.dangTaiDuLieu) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp), color = Color(0xFFBB86FC))
                }
            }

            if (trangThai.danhSachGoiYTimKiem.isNotEmpty() && trangThai.thanhPhoXemTruoc == null) {
                LazyColumn(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                    items(trangThai.danhSachGoiYTimKiem) { ten ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onChonGoiY(ten) },
                            colors = CardDefaults.cardColors(containerColor = mauTheTrongSuot)
                        ) {
                            Text(text = ten, modifier = Modifier.padding(16.dp), fontSize = 16.sp, color = mauChuChinh)
                        }
                    }
                }
            }

            if (trangThai.thanhPhoXemTruoc != null && !trangThai.dangTaiDuLieu) {
                val ketQua = trangThai.thanhPhoXemTruoc
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier.fillMaxWidth().background(mauTheTrongSuot, shape = MaterialTheme.shapes.medium).padding(16.dp)
                ) {
                    Text(text = "THÔNG TIN TÌM THẤY", color = mauChuPhu, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = ketQua.tenThanhPho, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = mauChuChinh)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Nhiệt độ: ${ketQua.nhietDo}${trangThai.donViNhietDo}", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFBB86FC))
                        Spacer(modifier = Modifier.width(16.dp))
                        AsyncImage(
                            model = "https://openweathermap.org/img/wn/${ketQua.iconId}@4x.png",
                            contentDescription = "Icon thời tiết",
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Text(text = "Trạng thái: ${ketQua.trangThai}", fontSize = 16.sp, color = mauChuPhu)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = { onGocChuThayDoi("") }, 
                            modifier = Modifier.weight(1f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.7f))
                        ) {
                            Text("Xóa", color = Color.Red)
                        }
                        Button(
                            onClick = { onXacNhanThem(ketQua) }, 
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = mauNutPhu)
                        ) {
                            Text("Thêm", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

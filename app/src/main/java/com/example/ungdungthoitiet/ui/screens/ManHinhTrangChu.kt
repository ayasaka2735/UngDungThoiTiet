package com.example.ungdungthoitiet.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ungdungthoitiet.R
import com.example.ungdungthoitiet.data.DuLieuThoiTiet
import com.example.ungdungthoitiet.data.TrangThaiUiThoiTiet
import coil.compose.AsyncImage

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
    onDoiNhietDo: (String) -> Unit,
    onDoiGio: (String) -> Unit,
    onDoiLuongMua: (String) -> Unit,
    onDoiKhoangCach: (String) -> Unit,
    onDongBangSua: () -> Unit
) {
    // Giao diện sử dụng ảnh nền sinh động
    val mauTheTrongSuot = Color.Black.copy(alpha = 0.5f)
    val mauChuChinh = Color.White
    val mauChuPhu = Color(0xFFE0E0E0)

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Lớp dưới cùng: Ảnh nền lấy từ thư mục drawable
        Image(
            painter = painterResource(id = R.drawable.anhthoitiet),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Phủ kín màn hình
        )

        // 2. Lớp ở giữa: Phủ một lớp đen mờ để làm nổi bật chữ
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // 3. Lớp trên cùng: Nội dung chính của ứng dụng
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).padding(top = 10.dp)) {
            ThanhCongCuTrangChu(
                dangSua = trangThai.cheDoSuaDanhSach,
                onChuyenTimKiem = onChuyenTimKiem,
                onMoCaiDat = onMoBangSuaCauHinh,
                onBatSua = onBatCheDoSua,
                onXongSua = onXongSuaDanhSach
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(trangThai.danhSachThanhPho) { thanhPho ->
                    TheThoiTiet(
                        thanhPho = thanhPho,
                        donViNhietDo = trangThai.donViNhietDo,
                        donViGio = trangThai.donViGio,
                        dangSua = trangThai.cheDoSuaDanhSach,
                        mauNenCard = mauTheTrongSuot,
                        mauChuChinh = mauChuChinh,
                        mauChuPhu = mauChuPhu,
                        onClick = { onChonThanhPho(thanhPho) },
                        onXoa = { onBamXoaThanhPho(thanhPho.tenThanhPho) }
                    )
                }
            }

            Button(
                onClick = onChuyenGioiThieu,
                modifier = Modifier.fillMaxWidth().padding(bottom = 30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
            ) {
                Text("Về chúng tôi", color = Color.White)
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

@Composable
private fun ThanhCongCuTrangChu(
    dangSua: Boolean,
    onChuyenTimKiem: () -> Unit,
    onMoCaiDat: () -> Unit,
    onBatSua: () -> Unit,
    onXongSua: () -> Unit
) {
    val mauNutPhu = Color.White.copy(alpha = 0.2f)
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = onChuyenTimKiem,
            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
        ) {
            Text("Tìm kiếm thành phố", color = Color.White)
        }

        if (dangSua) {
            Button(
                onClick = onXongSua,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50).copy(alpha = 0.8f))
            ) {
                Text("Xong", color = Color.White)
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Button(
                    onClick = onMoCaiDat, 
                    colors = ButtonDefaults.buttonColors(containerColor = mauNutPhu)
                ) { 
                    Text("Cài đặt", color = Color.White) 
                }
                Button(
                    onClick = onBatSua, 
                    colors = ButtonDefaults.buttonColors(containerColor = mauNutPhu)
                ) { 
                    Text("Sửa", color = Color.White) 
                }
            }
        }
    }
}

@Composable
private fun TheThoiTiet(
    thanhPho: DuLieuThoiTiet,
    donViNhietDo: String,
    donViGio: String,
    dangSua: Boolean,
    mauNenCard: Color,
    mauChuChinh: Color,
    mauChuPhu: Color,
    onClick: () -> Unit,
    onXoa: () -> Unit
) {
    val isFahrenheit = donViNhietDo == "°F"
    fun toDisp(t: Int) = if (isFahrenheit) (t * 9 / 5) + 32 else t

    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = mauNenCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = "https://openweathermap.org/img/wn/${thanhPho.iconId}@4x.png",
                        contentDescription = "Hình minh họa thời tiết",
                        modifier = Modifier.size(45.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Vị trí: ${thanhPho.tenThanhPho}", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = mauChuChinh)
                }
                if (dangSua) {
                    Button(onClick = onXoa, colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.8f))) {
                        Text("Xóa", color = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text("Trạng thái: ${thanhPho.trangThai}", fontSize = 15.sp, color = mauChuPhu)
            Text("Nhiệt độ: ${toDisp(thanhPho.nhietDo)}$donViNhietDo", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFFBB86FC))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Cao nhất: ${toDisp(thanhPho.nhietDoCaoNhat)}°", color = Color(0xFFFF5252))
                Text("Thấp nhất: ${toDisp(thanhPho.nhietDoThapNhat)}°", color = Color(0xFF448AFF))
            }

            Spacer(Modifier.height(4.dp))
            Text("Độ ẩm: ${thanhPho.doAm}% | Gió: ${thanhPho.tocDoGio.toInt()} $donViGio", fontSize = 13.sp, color = mauChuPhu)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun XemTruocManHinhTrangChu() {
    val mau = DuLieuThoiTiet(
        "Hà Nội", 30, "Mây rải rác", 70, 20.0, 1012, 33, 26,
        listOf("Bây giờ - 30°C"),
        listOf("23/5 - 30°C"), "01d"
    )
    ManHinhTrangChu(
        trangThai = TrangThaiUiThoiTiet(danhSachThanhPho = listOf(mau)),
        onChonThanhPho = {}, onMoBangSuaCauHinh = {}, onBatCheDoSua = {}, onXongSuaDanhSach = {},
        onBamXoaThanhPho = {}, onChuyenTimKiem = {}, onChuyenGioiThieu = {}, onDoiNhietDo = {},
        onDoiGio = {}, onDoiLuongMua = {}, onDoiKhoangCach = {}, onDongBangSua = {}
    )
}

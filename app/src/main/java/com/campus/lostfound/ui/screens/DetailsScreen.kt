package com.campus.lostfound.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.campus.lostfound.data.model.Item
import com.campus.lostfound.ui.components.*
import com.campus.lostfound.ui.theme.*
import com.campus.lostfound.viewmodel.MainViewModel

@Composable
fun DetailsScreen(vm: MainViewModel, id: Long, onBack: () -> Unit, onUpdateStatus: (Long) -> Unit) {
    val tick by vm.refresh.collectAsState()
    val user by vm.user.collectAsState()
    val item by produceState<Item?>(null, id, tick) { value = vm.getItem(id) }
    val context = LocalContext.current

    val it = item
    if (it == null) { Box(Modifier.fillMaxSize()) { CircularProgressIndicator(Modifier.align(Alignment.Center)) }; return }

    Column(Modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Box(Modifier.fillMaxWidth().height(240.dp).background(Primary)) {
                if (it.imageUri != null)
                    AsyncImage(it.imageUri, it.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                else Icon(categoryIcon(it.category), null, Modifier.size(96.dp).align(Alignment.Center), tint = Color.White.copy(0.8f))
                IconButton(onClick = onBack, modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                    .background(Color.Black.copy(alpha = 0.3f), CircleShape)) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Back", tint = Color.White)
                }
                Box(Modifier.align(Alignment.BottomEnd).padding(12.dp)) { StatusChip(it.status) }
            }
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(it.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)
                MetaRow(categoryIcon(it.category), it.category, 14)
                MetaRow(Icons.Outlined.LocationOn, it.location, 14)
                MetaRow(Icons.Outlined.CalendarToday, it.date, 14)
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text("Description", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(it.description, fontSize = 13.sp, color = TextGray, lineHeight = 19.sp)
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text("Reported by", fontSize = 12.sp, color = TextGray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Avatar(40.dp); Spacer(Modifier.width(12.dp))
                    Column {
                        Text(it.reporterName, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                        Text(it.reporterPhone, fontSize = 12.sp, color = TextGray)
                    }
                }
            }
        }
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PrimaryButton("Contact", onClick = {
                val number = it.reporterPhone.filter { c -> c.isDigit() || c == '+' }
                context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$number")))
            }, Modifier.weight(1f))
            if (user?.id == it.reporterId)
                PrimaryButton("Update Status", { onUpdateStatus(it.id) }, Modifier.weight(1f), outlined = true)
        }
    }
}

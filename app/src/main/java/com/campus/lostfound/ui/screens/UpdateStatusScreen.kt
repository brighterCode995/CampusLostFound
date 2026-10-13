package com.campus.lostfound.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campus.lostfound.data.model.*
import com.campus.lostfound.ui.components.*
import com.campus.lostfound.ui.theme.*
import com.campus.lostfound.viewmodel.MainViewModel

@Composable
fun UpdateStatusScreen(vm: MainViewModel, id: Long, onBack: () -> Unit) {
    val item by produceState<Item?>(null, id) { value = vm.getItem(id) }
    val it = item
    Column(Modifier.fillMaxSize()) {
        ScreenTopBar("Update Item Status", onBack)
        if (it == null) { Box(Modifier.fillMaxSize()) { CircularProgressIndicator(Modifier.align(Alignment.Center)) }; return@Column }

        var selected by remember(it.id) { mutableStateOf(it.status) }
        val options = listOf(if (it.type == ItemType.LOST) ItemStatus.LOST else ItemStatus.FOUND, ItemStatus.RECOVERED)

        Column(Modifier.padding(16.dp)) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
                border = BorderStroke(1.dp, Border)) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    ItemThumb(it, 80.dp); Spacer(Modifier.width(14.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(it.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text(it.category, fontSize = 12.sp, color = TextGray)
                        MetaRow(Icons.Outlined.LocationOn, it.location)
                        MetaRow(Icons.Outlined.CalendarToday, it.date)
                    }
                }
            }
            Text("Current Status", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(top = 20.dp, bottom = 8.dp))
            StatusChip(it.status)
            Text("New Status", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, modifier = Modifier.padding(top = 20.dp, bottom = 4.dp))
            options.forEach { s ->
                Row(Modifier.fillMaxWidth().selectable(selected == s, role = Role.RadioButton, onClick = { selected = s })
                    .padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected == s, null, colors = RadioButtonDefaults.colors(selectedColor = Primary))
                    Spacer(Modifier.width(12.dp)); Text(s.label, fontSize = 14.sp)
                }
            }
            Spacer(Modifier.height(24.dp))
            PrimaryButton("Update", { vm.updateStatus(it.id, selected, onBack) }, Modifier.fillMaxWidth(),
                enabled = selected != it.status)
        }
    }
}

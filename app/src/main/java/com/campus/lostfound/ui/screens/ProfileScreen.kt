package com.campus.lostfound.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campus.lostfound.ui.components.*
import com.campus.lostfound.ui.theme.*
import com.campus.lostfound.viewmodel.MainViewModel

@Composable
fun ProfileScreen(vm: MainViewModel, onMyReports: () -> Unit, onLogout: () -> Unit) {
    val user by vm.user.collectAsState()
    val context = LocalContext.current
    var showPassword by remember { mutableStateOf(false) }
    fun soon() = Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar("Profile", null) { IconButton(onClick = ::soon) { Icon(Icons.Outlined.Settings, "Settings", tint = TextGray) } }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Border)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Avatar(56.dp); Spacer(Modifier.width(14.dp))
                    Column {
                        Text(user?.fullName ?: "", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Text(user?.email ?: "", fontSize = 12.sp, color = TextGray)
                        Text(user?.phone ?: "", fontSize = 12.sp, color = TextGray)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            MenuRow(Icons.Outlined.Description, "My Reports", onMyReports)
            MenuRow(Icons.Outlined.Settings, "Account Settings", ::soon)
            MenuRow(Icons.Outlined.Lock, "Change Password") { showPassword = true }
            MenuRow(Icons.Outlined.HelpOutline, "Help & Support", ::soon)
            MenuRow(Icons.Outlined.Info, "About", ::soon)
        }
        PrimaryButton("Log Out", onLogout, Modifier.fillMaxWidth().padding(16.dp))
    }

    if (showPassword) {
        var pw by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showPassword = false },
            title = { Text("Change Password") },
            text = { AppTextField(pw, { pw = it }, "New password (min 6 characters)", isPassword = true) },
            confirmButton = {
                TextButton(enabled = pw.length >= 6, onClick = {
                    vm.changePassword(pw) {
                        Toast.makeText(context, "Password updated", Toast.LENGTH_SHORT).show()
                        showPassword = false
                    }
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showPassword = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun MenuRow(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 14.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = TextGray); Spacer(Modifier.width(16.dp))
        Text(label, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Icon(Icons.Outlined.ChevronRight, null, tint = TextHint)
    }
    HorizontalDivider(color = Color(0xFFEEF0F6))
}

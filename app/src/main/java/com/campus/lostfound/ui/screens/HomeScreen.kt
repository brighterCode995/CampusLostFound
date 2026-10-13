package com.campus.lostfound.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campus.lostfound.data.model.ItemType
import com.campus.lostfound.ui.theme.*
import com.campus.lostfound.viewmodel.MainViewModel

@Composable
fun HomeScreen(vm: MainViewModel, onSearch: () -> Unit, onReport: (ItemType) -> Unit, onMyReports: () -> Unit) {
    val user by vm.user.collectAsState()
    val firstName = user?.fullName?.substringBefore(' ') ?: ""

    Column(Modifier.fillMaxSize().background(Color.White)) {
        Column(Modifier.fillMaxWidth().background(Primary, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .padding(horizontal = 16.dp).padding(top = 20.dp, bottom = 28.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Hello, $firstName", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Icon(Icons.Outlined.Notifications, "Notifications", tint = Color.White)
            }
            Text("Find. Report. Reconnect.", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp,
                modifier = Modifier.padding(top = 2.dp, bottom = 16.dp))
            Row(Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(50)).background(Color.White)
                .clickable(onClick = onSearch).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Search, null, tint = Primary)
                Spacer(Modifier.width(10.dp))
                Text("Search for items...", color = TextHint, fontSize = 14.sp)
            }
        }
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                ActionTile("Search Items", "Find lost or found items", Icons.Outlined.Search, LostRed, TilePink, Modifier.weight(1f), onSearch)
                ActionTile("Report Lost Item", "Tell us what you lost", Icons.Outlined.Add, FoundGreen, TileGreen, Modifier.weight(1f)) { onReport(ItemType.LOST) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                ActionTile("Report Found Item", "Help someone get it back", Icons.Outlined.Add, Purple, TilePurple, Modifier.weight(1f)) { onReport(ItemType.FOUND) }
                ActionTile("My Reports", "View your items", Icons.Outlined.Person, Orange, TileOrange, Modifier.weight(1f), onMyReports)
            }
        }
    }
}

@Composable
private fun ActionTile(title: String, subtitle: String, icon: ImageVector, accent: Color, bg: Color,
                       modifier: Modifier, onClick: () -> Unit) {
    Column(modifier.height(150.dp).clip(RoundedCornerShape(16.dp)).background(bg).clickable(onClick = onClick).padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Box(Modifier.size(48.dp).clip(CircleShape).background(accent), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = Color.White)
        }
        Spacer(Modifier.height(12.dp))
        Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextDark)
        Text(subtitle, fontSize = 11.sp, color = TextGray, modifier = Modifier.padding(top = 2.dp))
    }
}

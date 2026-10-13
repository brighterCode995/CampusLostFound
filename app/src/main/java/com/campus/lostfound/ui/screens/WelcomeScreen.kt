package com.campus.lostfound.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campus.lostfound.ui.components.PrimaryButton
import com.campus.lostfound.ui.theme.*

private data class Page(val icon: ImageVector, val title: String, val body: String)

private val pages = listOf(
    Page(Icons.Outlined.Work, "Lost Something?\nFound Something?",
        "Report, search and recover lost and found items within your university campus."),
    Page(Icons.Outlined.Search, "Search Campus-Wide",
        "Browse items reported by other students and staff by name, category or location."),
    Page(Icons.Outlined.Handshake, "Reconnect Safely",
        "Contact the reporter directly and mark items as recovered once they are back with you.")
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WelcomeScreen(onGetStarted: () -> Unit, onLogin: () -> Unit) {
    val pager = rememberPagerState { pages.size }
    Column(Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(pager, Modifier.weight(1f)) { i ->
            val p = pages[i]
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Box(Modifier.size(200.dp).clip(CircleShape).background(TilePurple), contentAlignment = Alignment.Center) {
                    Icon(p.icon, null, Modifier.size(96.dp), tint = Primary)
                }
                Spacer(Modifier.height(32.dp))
                Text(p.title, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = TextDark)
                Spacer(Modifier.height(12.dp))
                Text(p.body, fontSize = 14.sp, textAlign = TextAlign.Center, color = TextGray, lineHeight = 20.sp)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.padding(16.dp)) {
            repeat(pages.size) { i ->
                Box(Modifier.size(7.dp).clip(CircleShape)
                    .background(if (pager.currentPage == i) Primary else Color(0xFFC9CDEB)))
            }
        }
        PrimaryButton("Get Started", onGetStarted, Modifier.fillMaxWidth())
        Row(Modifier.padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("Already have an account?", fontSize = 13.sp, color = TextDark)
            TextButton(onClick = onLogin) { Text("Log In", fontSize = 13.sp, color = Primary) }
        }
    }
}

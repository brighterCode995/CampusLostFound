package com.campus.lostfound.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campus.lostfound.ui.theme.Primary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val done by rememberUpdatedState(onFinished)
    LaunchedEffect(Unit) { delay(1800); done() }

    Box(Modifier.fillMaxSize().background(Primary)) {
        Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
            // Swap for your own vector logo (backpack + magnifier) when available
            Box(Modifier.size(96.dp), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.Work, null, Modifier.size(80.dp), tint = Color.White)
                Icon(Icons.Outlined.Search, null, Modifier.size(40.dp).align(Alignment.BottomEnd), tint = Color.White)
            }
            Spacer(Modifier.height(24.dp))
            Text("Campus\nLost and Found", color = Color.White, fontSize = 32.sp,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, lineHeight = 38.sp)
            Spacer(Modifier.height(12.dp))
            Text("Find what matters", color = Color.White.copy(alpha = 0.85f), fontSize = 16.sp)
        }
        Icon(Icons.Outlined.School, null, Modifier.align(Alignment.BottomCenter).padding(bottom = 64.dp).size(64.dp),
            tint = Color.White.copy(alpha = 0.8f))
    }
}

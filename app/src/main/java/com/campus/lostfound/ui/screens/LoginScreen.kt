package com.campus.lostfound.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.campus.lostfound.ui.components.*
import com.campus.lostfound.ui.theme.*
import com.campus.lostfound.viewmodel.MainViewModel

@Composable
fun LoginScreen(vm: MainViewModel, onRegister: () -> Unit, onSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(56.dp))
        Box(Modifier.size(80.dp), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.Work, null, Modifier.size(68.dp), tint = Primary)
            Icon(Icons.Outlined.Search, null, Modifier.size(32.dp).align(Alignment.BottomEnd), tint = Primary)
        }
        Spacer(Modifier.height(16.dp))
        Text("Campus Lost and Found", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Text("Sign in to continue", fontSize = 14.sp, color = TextGray, modifier = Modifier.padding(top = 6.dp, bottom = 40.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppTextField(email, { email = it }, "Email Address", leadingIcon = Icons.Outlined.Email, keyboardType = KeyboardType.Email)
            AppTextField(password, { password = it }, "Password", leadingIcon = Icons.Outlined.Lock, isPassword = true)
        }
        error?.let { Text(it, color = LostRed, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp)) }
        Spacer(Modifier.height(20.dp))
        PrimaryButton("Log In", onClick = {
            if (email.isBlank() || password.isBlank()) { error = "Enter your email and password"; return@PrimaryButton }
            loading = true; error = null
            vm.login(email, password) { err -> loading = false; if (err == null) onSuccess() else error = err }
        }, Modifier.fillMaxWidth(), enabled = !loading)
        TextButton(onClick = { Toast.makeText(context, "Password reset is not implemented yet", Toast.LENGTH_SHORT).show() }) {
            Text("Forgot Password?", fontSize = 13.sp, color = Primary)
        }
        Spacer(Modifier.height(48.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account?", fontSize = 13.sp)
            TextButton(onClick = onRegister) { Text("Register", fontSize = 13.sp, color = Primary, fontWeight = FontWeight.Medium) }
        }
    }
}

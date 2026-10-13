package com.campus.lostfound.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun RegisterScreen(vm: MainViewModel, onBack: () -> Unit, onLogin: () -> Unit, onSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var sid by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
        ScreenTopBar("", onBack)
        Text("Create Account", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextDark)
        Text("Join the campus community", fontSize = 14.sp, color = TextGray, modifier = Modifier.padding(top = 4.dp, bottom = 24.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppTextField(name, { name = it }, "Full Name", leadingIcon = Icons.Outlined.Person)
            AppTextField(sid, { sid = it }, "Student/Staff ID", leadingIcon = Icons.Outlined.Badge)
            AppTextField(email, { email = it }, "Email Address", leadingIcon = Icons.Outlined.Email, keyboardType = KeyboardType.Email)
            AppTextField(phone, { phone = it }, "Phone Number", leadingIcon = Icons.Outlined.Phone, keyboardType = KeyboardType.Phone)
            AppTextField(password, { password = it }, "Password", leadingIcon = Icons.Outlined.Lock, isPassword = true)
        }
        error?.let { Text(it, color = LostRed, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp)) }
        Spacer(Modifier.height(24.dp))
        PrimaryButton("Register", onClick = {
            error = when {
                listOf(name, sid, email, phone, password).any { it.isBlank() } -> "Please fill in all fields"
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Enter a valid email address"
                password.length < 6 -> "Password must be at least 6 characters"
                else -> null
            }
            if (error == null) {
                loading = true
                vm.register(name, sid, email, phone, password) { err ->
                    loading = false
                    if (err == null) { Toast.makeText(context, "Welcome!", Toast.LENGTH_SHORT).show(); onSuccess() }
                    else error = err
                }
            }
        }, Modifier.fillMaxWidth(), enabled = !loading)
        Row(Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally).padding(top = 16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Text("Already have an account?", fontSize = 13.sp)
            TextButton(onClick = onLogin) { Text("Log In", fontSize = 13.sp, color = Primary) }
        }
    }
}

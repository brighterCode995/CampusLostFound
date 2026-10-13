package com.campus.lostfound.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.campus.lostfound.data.model.ItemType
import com.campus.lostfound.ui.components.*
import com.campus.lostfound.ui.theme.*
import com.campus.lostfound.viewmodel.MainViewModel

/** One screen serves both "Report Lost Item" and "Report Found Item" (toggle at the top). */
@Composable
fun ReportScreen(vm: MainViewModel, initialType: ItemType, onBack: () -> Unit, onSubmitted: () -> Unit) {
    var type by remember(initialType) { mutableStateOf(initialType) }
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var image by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var submitting by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) image = uri.toString()
    }
    val dateLabel = if (type == ItemType.LOST) "Date Lost" else "Date Found"

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar(if (type == ItemType.LOST) "Report Lost Item" else "Report Found Item", onBack)
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
            TypeToggle(type) { type = it }

            FormLabel("Item Name *")
            AppTextField(name, { name = it }, if (type == ItemType.LOST) "e.g. Mobile Phone" else "e.g. Wallet")
            FormLabel("Category *")
            DropdownField(category, "Select category", categories, { category = it })
            FormLabel("Description *")
            AppTextField(description, { description = it }, "Describe the item (color, brand, etc.)",
                singleLine = false, minLines = 3)
            FormLabel("Location *")
            DropdownField(location, "Select location", campusLocations, { location = it }, Icons.Outlined.LocationOn)
            FormLabel("$dateLabel *")
            DateField(date, "Select date") { date = it }
            FormLabel("Add Image (optional)")
            Box(Modifier.fillMaxWidth().height(96.dp).dashedBorder().clip(RoundedCornerShape(10.dp))
                .clickable { picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                contentAlignment = Alignment.Center) {
                if (image != null) AsyncImage(image, null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                else Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.CameraAlt, null, tint = TextGray)
                    Text("Tap to add image", fontSize = 12.sp, color = TextGray)
                }
            }
            error?.let { Text(it, color = LostRed, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp)) }
            Spacer(Modifier.height(20.dp))
            PrimaryButton("Submit", onClick = {
                if (name.isBlank() || category.isBlank() || description.isBlank() || location.isBlank() || date.isBlank()) {
                    error = "Please complete all required fields"; return@PrimaryButton
                }
                submitting = true
                vm.submitItem(type, name, category, description, location, date, image) {
                    submitting = false
                    Toast.makeText(context, "${type.label} item reported", Toast.LENGTH_SHORT).show()
                    onSubmitted()
                }
            }, Modifier.fillMaxWidth(), enabled = !submitting)
            Spacer(Modifier.height(24.dp))
        }
    }
}

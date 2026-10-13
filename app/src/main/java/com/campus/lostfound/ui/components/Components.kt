package com.campus.lostfound.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.campus.lostfound.data.model.*
import com.campus.lostfound.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

val categories = listOf("Electronics", "Bags", "Wallets", "Books", "Keys", "Clothing", "ID Cards", "Other")
val campusLocations = listOf("Library", "Engineering Block", "Cafeteria", "Lecture Hall",
    "Hostel", "Sports Ground", "Admin Block", "Other")

fun categoryIcon(c: String): ImageVector = when (c) {
    "Electronics" -> Icons.Outlined.PhoneAndroid
    "Bags" -> Icons.Outlined.Work
    "Wallets" -> Icons.Outlined.AccountBalanceWallet
    "Books" -> Icons.Outlined.MenuBook
    "Keys" -> Icons.Outlined.VpnKey
    "Clothing" -> Icons.Outlined.Checkroom
    "ID Cards" -> Icons.Outlined.Badge
    else -> Icons.Outlined.Inventory2
}

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = Border, focusedBorderColor = Primary,
    unfocusedContainerColor = Color.White, focusedContainerColor = Color.White
)

@Composable
fun AppTextField(
    value: String, onValueChange: (String) -> Unit, placeholder: String,
    modifier: Modifier = Modifier, leadingIcon: ImageVector? = null,
    isPassword: Boolean = false, keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true, minLines: Int = 1
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, fontSize = 13.sp, color = TextHint) },
        leadingIcon = if (leadingIcon != null) ({ Icon(leadingIcon, null, tint = TextGray) }) else null,
        trailingIcon = if (isPassword) ({
            IconButton(onClick = { visible = !visible }) {
                Icon(if (visible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff, null, tint = TextGray)
            }
        }) else null,
        visualTransformation = if (isPassword && !visible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType),
        singleLine = singleLine, minLines = minLines,
        shape = RoundedCornerShape(10.dp), colors = fieldColors()
    )
}

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier,
                  enabled: Boolean = true, outlined: Boolean = false) {
    if (outlined) {
        OutlinedButton(onClick, modifier.height(48.dp), enabled = enabled, shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Primary)) { Text(text, color = Primary, fontWeight = FontWeight.Medium) }
    } else {
        Button(onClick, modifier.height(48.dp), enabled = enabled, shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Primary)) {
            Text(text, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ScreenTopBar(title: String, onBack: (() -> Unit)?, trailing: @Composable () -> Unit = {}) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        if (onBack != null) IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Outlined.ArrowBack, "Back") }
        else Spacer(Modifier.width(12.dp))
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextDark, modifier = Modifier.weight(1f))
        trailing()
    }
}

@Composable
fun FormLabel(text: String) =
    Text(text, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextDark,
        modifier = Modifier.padding(top = 14.dp, bottom = 6.dp))

@Composable
fun TypeToggle(selected: ItemType, onSelect: (ItemType) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        ItemType.entries.forEach { t ->
            val sel = t == selected
            val color = if (t == ItemType.LOST) LostRed else FoundGreen
            Box(
                Modifier.weight(1f).height(38.dp).clip(RoundedCornerShape(50))
                    .background(if (sel) color else Color(0xFFEFF1F6)).clickable { onSelect(t) },
                contentAlignment = Alignment.Center
            ) { Text(t.label, color = if (sel) Color.White else TextGray, fontWeight = FontWeight.Medium) }
        }
    }
}

@Composable
fun StatusChip(status: ItemStatus) {
    val color = when (status) {
        ItemStatus.LOST -> LostRed; ItemStatus.FOUND -> FoundGreen; ItemStatus.RECOVERED -> RecoveredGray
    }
    Text(status.label, color = color, fontSize = 11.sp, fontWeight = FontWeight.Medium,
        modifier = Modifier.clip(RoundedCornerShape(50)).background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 3.dp))
}

@Composable
fun ItemThumb(item: Item, size: Dp = 56.dp) {
    Box(Modifier.size(size).clip(RoundedCornerShape(8.dp)).background(Color(0xFFEFF1F6)), contentAlignment = Alignment.Center) {
        if (item.imageUri != null)
            AsyncImage(item.imageUri, item.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        else Icon(categoryIcon(item.category), null, tint = TextGray)
    }
}

@Composable
fun MetaRow(icon: ImageVector, text: String, size: Int = 12) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, Modifier.size(14.dp), tint = TextGray)
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = size.sp, color = TextGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
fun ItemRow(item: Item, onClick: () -> Unit) {
    Card(
        onClick = onClick, modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE9EBF2))
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            ItemThumb(item, 64.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()) {
                    Text(item.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextDark,
                        maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, false))
                    Spacer(Modifier.width(8.dp)); StatusChip(item.status)
                }
                Text(item.category, fontSize = 12.sp, color = TextGray)
                MetaRow(Icons.Outlined.LocationOn, item.location)
                Text(item.date, fontSize = 12.sp, color = TextGray)
            }
            Icon(Icons.Outlined.ChevronRight, null, tint = TextGray)
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
        Text(message, color = TextGray, fontSize = 14.sp)
    }
}

fun Modifier.dashedBorder(color: Color = Color(0xFFB8BDD0), radius: Dp = 10.dp) = drawBehind {
    drawRoundRect(color, cornerRadius = CornerRadius(radius.toPx()),
        style = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(value: String, placeholder: String, options: List<String>,
                  onSelect: (String) -> Unit, leadingIcon: ImageVector? = null) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, { expanded = it }) {
        OutlinedTextField(
            value = value, onValueChange = {}, readOnly = true,
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 13.sp, color = TextHint) },
            leadingIcon = if (leadingIcon != null) ({ Icon(leadingIcon, null, tint = TextGray) }) else null,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            shape = RoundedCornerShape(10.dp), colors = fieldColors()
        )
        ExposedDropdownMenu(expanded, { expanded = false }) {
            options.forEach { o -> DropdownMenuItem(text = { Text(o) }, onClick = { onSelect(o); expanded = false }) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(value: String, placeholder: String, onPick: (String) -> Unit) {
    var show by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = value, onValueChange = {}, enabled = false, modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, fontSize = 13.sp, color = TextHint) },
            leadingIcon = { Icon(Icons.Outlined.CalendarToday, null) },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = TextDark, disabledBorderColor = Border,
                disabledLeadingIconColor = TextGray, disabledContainerColor = Color.White,
                disabledPlaceholderColor = TextHint)
        )
        Box(Modifier.matchParentSize().clickable { show = true })
    }
    if (show) {
        val state = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { show = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let {
                        val f = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            .apply { timeZone = TimeZone.getTimeZone("UTC") }
                        onPick(f.format(it))
                    }
                    show = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { show = false }) { Text("Cancel") } }
        ) { DatePicker(state) }
    }
}

@Composable
fun Avatar(size: Dp = 40.dp) {
    Box(Modifier.size(size).clip(CircleShape).background(Color(0xFFD7DCF7)), contentAlignment = Alignment.Center) {
        Icon(Icons.Outlined.Person, null, Modifier.size(size * 0.6f), tint = Primary)
    }
}

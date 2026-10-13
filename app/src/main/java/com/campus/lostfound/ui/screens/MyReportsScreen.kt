package com.campus.lostfound.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.campus.lostfound.ui.components.*
import com.campus.lostfound.viewmodel.MainViewModel

@Composable
fun MyReportsScreen(vm: MainViewModel, onBack: () -> Unit, onItem: (Long) -> Unit) {
    val tab by vm.myTab.collectAsState()
    val reports by vm.myReports.collectAsState()

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar("My Reports", onBack)
        Box(Modifier.padding(16.dp)) { TypeToggle(tab) { vm.myTab.value = it } }
        if (reports.isEmpty()) EmptyState("You haven't reported any ${tab.label.lowercase()} items yet")
        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(reports, key = { it.id }) { ItemRow(it) { onItem(it.id) } }
        }
    }
}

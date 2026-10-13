package com.campus.lostfound.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.campus.lostfound.data.model.ItemType
import com.campus.lostfound.ui.components.*
import com.campus.lostfound.ui.theme.*
import com.campus.lostfound.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(vm: MainViewModel, onBack: () -> Unit, onItem: (Long) -> Unit) {
    val query by vm.query.collectAsState()
    val filter by vm.filter.collectAsState()
    val results by vm.searchResults.collectAsState()

    Column(Modifier.fillMaxSize()) {
        ScreenTopBar("Search Items", onBack)
        Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            AppTextField(query, { vm.query.value = it }, "Search by name, category...",
                modifier = Modifier.weight(1f), leadingIcon = Icons.Outlined.Search)
            IconButton(onClick = { vm.query.value = ""; vm.filter.value = null }) {
                Icon(Icons.Outlined.Tune, "Clear filters", tint = TextGray)
            }
        }
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FilterChip(filter == null, { vm.filter.value = null }, { Text("All") }, colors = chipColors())
            ItemType.entries.forEach { t ->
                FilterChip(filter == t, { vm.filter.value = t }, { Text(t.label) }, colors = chipColors())
            }
        }
        if (results.isEmpty()) EmptyState("No items found")
        LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(results, key = { it.id }) { ItemRow(it) { onItem(it.id) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun chipColors() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = Primary, selectedLabelColor = androidx.compose.ui.graphics.Color.White)

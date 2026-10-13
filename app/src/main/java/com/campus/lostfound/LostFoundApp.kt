package com.campus.lostfound

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.campus.lostfound.data.model.ItemType
import com.campus.lostfound.ui.navigation.AppNavHost
import com.campus.lostfound.ui.navigation.Routes
import com.campus.lostfound.ui.theme.Primary
import com.campus.lostfound.ui.theme.TextHint
import com.campus.lostfound.viewmodel.MainViewModel

private data class Tab(val label: String, val icon: ImageVector, val route: String, val matches: Set<String>)

private val tabs = listOf(
    Tab("Home", Icons.Outlined.Home, Routes.HOME, setOf(Routes.HOME)),
    Tab("Search", Icons.Outlined.Search, Routes.SEARCH, setOf(Routes.SEARCH)),
    Tab("Report", Icons.Outlined.Flag, Routes.report(ItemType.LOST), setOf(Routes.REPORT)),
    Tab("Profile", Icons.Outlined.Person, Routes.PROFILE, setOf(Routes.PROFILE, Routes.MY_REPORTS))
)

@Composable
fun LostFoundApp() {
    val nav = rememberNavController()
    val vm: MainViewModel = viewModel()
    val route = nav.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        containerColor = Color.White,
        bottomBar = { if (route in Routes.bottomBarRoutes) BottomBar(nav, route) }
    ) { padding -> AppNavHost(nav, vm, Modifier.padding(padding)) }
}

@Composable
private fun BottomBar(nav: NavHostController, current: String?) {
    NavigationBar(containerColor = Color.White) {
        tabs.forEach { tab ->
            NavigationBarItem(
                selected = current in tab.matches,
                onClick = {
                    nav.navigate(tab.route) {
                        popUpTo(Routes.HOME) { saveState = false }
                        launchSingleTop = true
                    }
                },
                icon = { Icon(tab.icon, tab.label) },
                label = { Text(tab.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Primary, selectedTextColor = Primary,
                    unselectedIconColor = TextHint, unselectedTextColor = TextHint,
                    indicatorColor = Color.Transparent)
            )
        }
    }
}

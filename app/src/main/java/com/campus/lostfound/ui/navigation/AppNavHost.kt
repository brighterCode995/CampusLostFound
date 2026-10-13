package com.campus.lostfound.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.campus.lostfound.data.model.ItemType
import com.campus.lostfound.ui.screens.*
import com.campus.lostfound.viewmodel.MainViewModel

@Composable
fun AppNavHost(nav: NavHostController, vm: MainViewModel, modifier: Modifier = Modifier) {
    fun goHome() = nav.navigate(Routes.HOME) { popUpTo(0) { inclusive = true } }

    NavHost(nav, startDestination = Routes.SPLASH, modifier = modifier) {
        composable(Routes.SPLASH) {
            SplashScreen {
                val dest = if (vm.isLoggedIn()) Routes.HOME else Routes.WELCOME
                nav.navigate(dest) { popUpTo(Routes.SPLASH) { inclusive = true } }
            }
        }
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onGetStarted = { nav.navigate(Routes.REGISTER) },
                onLogin = { nav.navigate(Routes.LOGIN) })
        }
        composable(Routes.REGISTER) {
            RegisterScreen(vm, onBack = { nav.popBackStack() },
                onLogin = { nav.navigate(Routes.LOGIN) }, onSuccess = ::goHome)
        }
        composable(Routes.LOGIN) {
            LoginScreen(vm, onRegister = { nav.navigate(Routes.REGISTER) }, onSuccess = ::goHome)
        }
        composable(Routes.HOME) {
            HomeScreen(vm,
                onSearch = { nav.navigate(Routes.SEARCH) },
                onReport = { nav.navigate(Routes.report(it)) },
                onMyReports = { nav.navigate(Routes.MY_REPORTS) })
        }
        composable(Routes.SEARCH) {
            SearchScreen(vm, onBack = { nav.popBackStack() }, onItem = { nav.navigate(Routes.details(it)) })
        }
        composable(Routes.REPORT, listOf(navArgument("type") { type = NavType.StringType })) {
            val type = ItemType.valueOf(it.arguments?.getString("type") ?: "LOST")
            ReportScreen(vm, type, onBack = { nav.popBackStack() }, onSubmitted = ::goHome)
        }
        composable(Routes.DETAILS, listOf(navArgument("id") { type = NavType.LongType })) {
            DetailsScreen(vm, it.arguments!!.getLong("id"), onBack = { nav.popBackStack() },
                onUpdateStatus = { id -> nav.navigate(Routes.updateStatus(id)) })
        }
        composable(Routes.MY_REPORTS) {
            MyReportsScreen(vm, onBack = { nav.popBackStack() }, onItem = { nav.navigate(Routes.details(it)) })
        }
        composable(Routes.PROFILE) {
            ProfileScreen(vm, onMyReports = { nav.navigate(Routes.MY_REPORTS) },
                onLogout = { vm.logout(); nav.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } } })
        }
        composable(Routes.UPDATE_STATUS, listOf(navArgument("id") { type = NavType.LongType })) {
            UpdateStatusScreen(vm, it.arguments!!.getLong("id"), onBack = { nav.popBackStack() })
        }
    }
}

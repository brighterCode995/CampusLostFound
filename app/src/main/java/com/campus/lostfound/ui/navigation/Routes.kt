package com.campus.lostfound.ui.navigation

import com.campus.lostfound.data.model.ItemType

object Routes {
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val REGISTER = "register"
    const val LOGIN = "login"
    const val HOME = "home"
    const val SEARCH = "search"
    const val REPORT = "report/{type}"
    const val DETAILS = "details/{id}"
    const val MY_REPORTS = "my_reports"
    const val PROFILE = "profile"
    const val UPDATE_STATUS = "update_status/{id}"

    fun report(type: ItemType) = "report/${type.name}"
    fun details(id: Long) = "details/$id"
    fun updateStatus(id: Long) = "update_status/$id"

    val bottomBarRoutes = setOf(HOME, SEARCH, REPORT, MY_REPORTS, PROFILE)
}

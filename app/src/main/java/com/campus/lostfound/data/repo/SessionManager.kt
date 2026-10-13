package com.campus.lostfound.data.repo

import android.content.Context

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("session", Context.MODE_PRIVATE)
    var userId: Long
        get() = prefs.getLong("user_id", -1L)
        set(value) { prefs.edit().putLong("user_id", value).apply() }
}

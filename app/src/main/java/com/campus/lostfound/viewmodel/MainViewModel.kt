package com.campus.lostfound.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.campus.lostfound.data.model.*
import com.campus.lostfound.data.repo.Repository
import com.campus.lostfound.data.repo.SessionManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = Repository(app)
    private val session = SessionManager(app)

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    /** Bump to re-run queries after any write. */
    val refresh = MutableStateFlow(0)
    val query = MutableStateFlow("")
    val filter = MutableStateFlow<ItemType?>(null)   // null = All
    val myTab = MutableStateFlow(ItemType.LOST)

    init {
        if (session.userId > 0) viewModelScope.launch { _user.value = repo.getUser(session.userId) }
    }

    fun isLoggedIn() = session.userId > 0

    val searchResults: StateFlow<List<Item>> =
        combine(query, filter, refresh) { q, f, _ -> q to f }
            .mapLatest { (q, f) -> repo.search(q, f) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val myReports: StateFlow<List<Item>> =
        combine(user, myTab, refresh) { u, t, _ -> u to t }
            .mapLatest { (u, t) -> if (u == null) emptyList() else repo.myReports(u.id, t) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    suspend fun getItem(id: Long): Item? = repo.getItem(id)

    // ---------- auth ----------
    /** [onResult] receives an error message, or null on success. */
    fun login(email: String, password: String, onResult: (String?) -> Unit) = viewModelScope.launch {
        val u = repo.login(email.trim(), password)
        if (u == null) onResult("Invalid email or password") else {
            session.userId = u.id; _user.value = u; onResult(null)
        }
    }

    fun register(name: String, studentId: String, email: String, phone: String,
                 password: String, onResult: (String?) -> Unit) = viewModelScope.launch {
        val u = repo.register(name.trim(), studentId.trim(), email.trim(), phone.trim(), password)
        if (u == null) onResult("An account with this email already exists") else {
            session.userId = u.id; _user.value = u; onResult(null)
        }
    }

    fun logout() { session.userId = -1; _user.value = null }

    fun changePassword(newPassword: String, onDone: () -> Unit) = viewModelScope.launch {
        _user.value?.let { repo.changePassword(it.id, newPassword) }
        onDone()
    }

    // ---------- items ----------
    fun submitItem(type: ItemType, name: String, category: String, description: String,
                   location: String, date: String, imageUri: String?, onDone: () -> Unit) =
        viewModelScope.launch {
            val reporter = _user.value ?: return@launch
            val saved = imageUri?.let { repo.saveImage(it) }
            repo.addItem(
                Item(name = name.trim(), category = category, description = description.trim(),
                    location = location, date = date, imageUri = saved, type = type,
                    status = if (type == ItemType.LOST) ItemStatus.LOST else ItemStatus.FOUND,
                    reporterId = reporter.id)
            )
            refresh.value++
            onDone()
        }

    fun updateStatus(id: Long, status: ItemStatus, onDone: () -> Unit) = viewModelScope.launch {
        repo.updateStatus(id, status)
        refresh.value++
        onDone()
    }
}

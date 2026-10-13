package com.campus.lostfound.data.repo

import android.content.Context
import android.net.Uri
import com.campus.lostfound.data.db.DatabaseHelper
import com.campus.lostfound.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class Repository(private val context: Context) {
    private val db = DatabaseHelper(context.applicationContext)

    // ---- auth ----
    suspend fun login(email: String, password: String): User? = withContext(Dispatchers.IO) {
        db.findUserByEmail(email)?.takeIf { it.passwordHash == Security.sha256(password) }
    }

    /** @return created user, or null if the email is already registered */
    suspend fun register(name: String, studentId: String, email: String, phone: String, password: String): User? =
        withContext(Dispatchers.IO) {
            val user = User(fullName = name, studentId = studentId, email = email,
                phone = phone, passwordHash = Security.sha256(password))
            val id = db.insertUser(user)
            if (id == -1L) null else user.copy(id = id)
        }

    suspend fun getUser(id: Long) = withContext(Dispatchers.IO) { db.findUserById(id) }

    suspend fun changePassword(userId: Long, newPassword: String) = withContext(Dispatchers.IO) {
        db.updatePassword(userId, Security.sha256(newPassword))
    }

    // ---- items ----
    suspend fun search(query: String, type: ItemType?) = withContext(Dispatchers.IO) { db.searchItems(query, type) }
    suspend fun myReports(userId: Long, type: ItemType) = withContext(Dispatchers.IO) { db.itemsByReporter(userId, type) }
    suspend fun getItem(id: Long) = withContext(Dispatchers.IO) { db.getItem(id) }
    suspend fun addItem(item: Item) = withContext(Dispatchers.IO) { db.insertItem(item) }
    suspend fun updateStatus(id: Long, status: ItemStatus) = withContext(Dispatchers.IO) { db.updateStatus(id, status) }
    suspend fun deleteItem(id: Long) = withContext(Dispatchers.IO) { db.deleteItem(id) }

    /** Photo-picker URIs are temporary, so copy the image into app storage and keep that path. */
    suspend fun saveImage(source: String): String? = withContext(Dispatchers.IO) {
        runCatching {
            val file = File(context.filesDir, "item_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(Uri.parse(source))!!.use { input ->
                file.outputStream().use { input.copyTo(it) }
            }
            Uri.fromFile(file).toString()
        }.getOrNull()
    }
}

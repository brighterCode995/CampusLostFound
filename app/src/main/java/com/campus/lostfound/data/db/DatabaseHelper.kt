package com.campus.lostfound.data.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.campus.lostfound.data.model.*
import com.campus.lostfound.data.repo.Security

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "lostfound.db"
        const val DB_VERSION = 1
    }

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE users(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                full_name TEXT NOT NULL,
                student_id TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                phone TEXT NOT NULL,
                password_hash TEXT NOT NULL)"""
        )
        db.execSQL(
            """CREATE TABLE items(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                category TEXT NOT NULL,
                description TEXT NOT NULL,
                location TEXT NOT NULL,
                date TEXT NOT NULL,
                image_uri TEXT,
                type TEXT NOT NULL,
                status TEXT NOT NULL,
                reporter_id INTEGER NOT NULL,
                created_at INTEGER NOT NULL,
                FOREIGN KEY(reporter_id) REFERENCES users(id) ON DELETE CASCADE)"""
        )
        db.execSQL("CREATE INDEX idx_items_type ON items(type)")
        db.execSQL("CREATE INDEX idx_items_reporter ON items(reporter_id)")
        seed(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS items")
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    /** Demo data so the app is not empty on first launch. Login: mary@example.com / password123 */
    private fun seed(db: SQLiteDatabase) {
        val mary = db.insert("users", null, ContentValues().apply {
            put("full_name", "Mary Chirwa"); put("student_id", "STU-0001")
            put("email", "mary@example.com"); put("phone", "+265 999 123 456")
            put("password_hash", Security.sha256("password123"))
        })
        listOf(
            arrayOf("iPhone 14", "Electronics", "Library", "12 Apr 2025", "FOUND"),
            arrayOf("Blue Backpack", "Bags", "Engineering Block", "10 Apr 2025", "LOST"),
            arrayOf("Wallet", "Wallets", "Cafeteria", "08 Apr 2025", "FOUND"),
            arrayOf("Samsung Galaxy S22", "Electronics", "Lecture Hall", "15 Apr 2025", "LOST"),
            arrayOf("Textbooks", "Books", "Library", "05 Apr 2025", "LOST")
        ).forEach { r ->
            db.insert("items", null, ContentValues().apply {
                put("name", r[0]); put("category", r[1])
                put("description", "Sample item reported on campus.")
                put("location", r[2]); put("date", r[3]); putNull("image_uri")
                put("type", r[4]); put("status", r[4])
                put("reporter_id", mary); put("created_at", System.currentTimeMillis())
            })
        }
    }

    // ---------------- USERS ----------------
    fun insertUser(u: User): Long = writableDatabase.insert("users", null, ContentValues().apply {
        put("full_name", u.fullName); put("student_id", u.studentId)
        put("email", u.email.lowercase()); put("phone", u.phone)
        put("password_hash", u.passwordHash)
    }) // returns -1 on failure (e.g. duplicate email)

    fun findUserByEmail(email: String): User? =
        readableDatabase.query("users", null, "email = ?", arrayOf(email.lowercase()), null, null, null)
            .use { if (it.moveToFirst()) it.toUser() else null }

    fun findUserById(id: Long): User? =
        readableDatabase.query("users", null, "id = ?", arrayOf(id.toString()), null, null, null)
            .use { if (it.moveToFirst()) it.toUser() else null }

    fun updatePassword(userId: Long, hash: String): Int =
        writableDatabase.update("users", ContentValues().apply { put("password_hash", hash) },
            "id = ?", arrayOf(userId.toString()))

    // ---------------- ITEMS ----------------
    fun insertItem(i: Item): Long = writableDatabase.insert("items", null, ContentValues().apply {
        put("name", i.name); put("category", i.category); put("description", i.description)
        put("location", i.location); put("date", i.date); put("image_uri", i.imageUri)
        put("type", i.type.name); put("status", i.status.name)
        put("reporter_id", i.reporterId); put("created_at", i.createdAt)
    })

    fun getItem(id: Long): Item? = queryItems(listOf("i.id = ?"), listOf(id.toString())).firstOrNull()

    fun searchItems(query: String, type: ItemType?): List<Item> {
        val where = mutableListOf<String>()
        val args = mutableListOf<String>()
        if (query.isNotBlank()) {
            where += "(i.name LIKE ? OR i.category LIKE ? OR i.description LIKE ? OR i.location LIKE ?)"
            repeat(4) { args += "%${query.trim()}%" }
        }
        if (type != null) { where += "i.type = ?"; args += type.name }
        return queryItems(where, args)
    }

    fun itemsByReporter(userId: Long, type: ItemType): List<Item> =
        queryItems(listOf("i.reporter_id = ?", "i.type = ?"), listOf(userId.toString(), type.name))

    fun updateStatus(id: Long, status: ItemStatus): Int =
        writableDatabase.update("items", ContentValues().apply { put("status", status.name) },
            "id = ?", arrayOf(id.toString()))

    fun deleteItem(id: Long): Int = writableDatabase.delete("items", "id = ?", arrayOf(id.toString()))

    private fun queryItems(where: List<String>, args: List<String>): List<Item> {
        val sql = "SELECT i.*, u.full_name AS reporter_name, u.phone AS reporter_phone " +
                "FROM items i JOIN users u ON u.id = i.reporter_id" +
                (if (where.isEmpty()) "" else " WHERE " + where.joinToString(" AND ")) +
                " ORDER BY i.created_at DESC, i.id DESC"
        return readableDatabase.rawQuery(sql, args.toTypedArray()).use { c ->
            buildList { while (c.moveToNext()) add(c.toItem()) }
        }
    }

    // ---------------- MAPPERS ----------------
    private fun Cursor.str(col: String): String = getString(getColumnIndexOrThrow(col))
    private fun Cursor.strOrNull(col: String): String? =
        getColumnIndexOrThrow(col).let { if (isNull(it)) null else getString(it) }

    private fun Cursor.toUser() = User(
        id = getLong(getColumnIndexOrThrow("id")), fullName = str("full_name"),
        studentId = str("student_id"), email = str("email"),
        phone = str("phone"), passwordHash = str("password_hash")
    )

    private fun Cursor.toItem() = Item(
        id = getLong(getColumnIndexOrThrow("id")), name = str("name"), category = str("category"),
        description = str("description"), location = str("location"), date = str("date"),
        imageUri = strOrNull("image_uri"), type = ItemType.valueOf(str("type")),
        status = ItemStatus.valueOf(str("status")),
        reporterId = getLong(getColumnIndexOrThrow("reporter_id")),
        reporterName = str("reporter_name"), reporterPhone = str("reporter_phone"),
        createdAt = getLong(getColumnIndexOrThrow("created_at"))
    )
}

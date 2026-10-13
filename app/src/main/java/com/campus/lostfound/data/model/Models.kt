package com.campus.lostfound.data.model

enum class ItemType(val label: String) { LOST("Lost"), FOUND("Found") }

enum class ItemStatus(val label: String) {
    LOST("Lost"), FOUND("Found"), RECOVERED("Recovered")
}

data class User(
    val id: Long = 0,
    val fullName: String,
    val studentId: String,
    val email: String,
    val phone: String,
    val passwordHash: String = ""
)

data class Item(
    val id: Long = 0,
    val name: String,
    val category: String,
    val description: String,
    val location: String,
    val date: String,
    val imageUri: String? = null,
    val type: ItemType,
    val status: ItemStatus,
    val reporterId: Long,
    val reporterName: String = "",
    val reporterPhone: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

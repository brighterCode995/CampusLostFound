package com.campus.lostfound.data.repo

import java.security.MessageDigest

object Security {
    /** Demo-grade hashing. For production use a salted KDF (PBKDF2/Argon2) or a real auth backend. */
    fun sha256(input: String): String =
        MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
            .joinToString("") { "%02x".format(it) }
}

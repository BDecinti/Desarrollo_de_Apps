package com.example.myapplication.domain.security

import java.security.MessageDigest
import java.security.SecureRandom

/**
 * Hash con sal (SHA-256) para no guardar contraseñas en texto plano en el almacenamiento local.
 * Formato almacenado: "sal:hash".
 */
object PasswordHasher {

    fun hash(password: String, salt: String = generateSalt()): String =
        "$salt:${digest(salt + password)}"

    fun verify(password: String, stored: String): Boolean {
        val parts = stored.split(":")
        if (parts.size != 2) return false
        val expected = digest(parts[0] + password).toByteArray()
        val actual = parts[1].toByteArray()
        return MessageDigest.isEqual(expected, actual)
    }

    private fun generateSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return bytes.toHex()
    }

    private fun digest(input: String): String =
        MessageDigest.getInstance("SHA-256").digest(input.toByteArray()).toHex()

    private fun ByteArray.toHex(): String = joinToString("") { "%02x".format(it) }
}

package com.example.myapplication.domain

import com.example.myapplication.domain.security.PasswordHasher
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordHasherTest {

    @Test
    fun `verifica la contrasena correcta y rechaza la incorrecta`() {
        val stored = PasswordHasher.hash("madera2026")
        assertTrue(PasswordHasher.verify("madera2026", stored))
        assertFalse(PasswordHasher.verify("madera2027", stored))
    }

    @Test
    fun `no guarda la contrasena en texto plano y usa sal distinta`() {
        val a = PasswordHasher.hash("madera2026")
        val b = PasswordHasher.hash("madera2026")
        assertFalse(a.contains("madera2026"))
        assertNotEquals(a, b)
    }

    @Test
    fun `rechaza formatos almacenados invalidos`() {
        assertFalse(PasswordHasher.verify("x", "sin-separador"))
    }
}

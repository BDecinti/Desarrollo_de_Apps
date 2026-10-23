package com.example.myapplication.domain

import com.example.myapplication.domain.validation.FormValidators
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FormValidatorsTest {

    @Test
    fun `login exige usuario y contrasena`() {
        assertFalse(FormValidators.loginUsername("  ").isValid)
        assertFalse(FormValidators.loginPassword("").isValid)
        assertTrue(FormValidators.loginUsername("carpintero").isValid)
        assertTrue(FormValidators.loginPassword("maderas123").isValid)
    }

    @Test
    fun `usuario de registro valida largo y caracteres`() {
        assertEquals("El usuario es obligatorio", FormValidators.registerUsername("").message)
        assertEquals("Mínimo 4 caracteres", FormValidators.registerUsername("abc").message)
        assertEquals("Máximo 20 caracteres", FormValidators.registerUsername("a".repeat(21)).message)
        assertEquals("Solo letras, números, punto o guion bajo", FormValidators.registerUsername("juan perez").message)
        assertTrue(FormValidators.registerUsername("juan_perez.1").isValid)
    }

    @Test
    fun `usuario de registro rechaza nombres ya ocupados`() {
        val result = FormValidators.registerUsername("carpintero") { it == "carpintero" }
        assertFalse(result.isValid)
        assertEquals("Este usuario ya está en uso", result.message)
    }

    @Test
    fun `contrasena de registro exige largo, letra y numero`() {
        assertFalse(FormValidators.registerPassword("").isValid)
        assertEquals("Mínimo 6 caracteres", FormValidators.registerPassword("ab1").message)
        assertEquals("Incluye al menos una letra y un número", FormValidators.registerPassword("abcdefgh").message)
        assertEquals("Incluye al menos una letra y un número", FormValidators.registerPassword("12345678").message)
        assertTrue(FormValidators.registerPassword("madera2026").isValid)
    }

    @Test
    fun `confirmacion de contrasena debe coincidir`() {
        assertFalse(FormValidators.confirmPassword("madera2026", "").isValid)
        assertEquals("Las contraseñas no coinciden", FormValidators.confirmPassword("madera2026", "madera2027").message)
        assertTrue(FormValidators.confirmPassword("madera2026", "madera2026").isValid)
    }

    @Test
    fun `nombre completo exige nombre y apellido solo con letras`() {
        assertFalse(FormValidators.fullName("").isValid)
        assertEquals("Ingresa nombre y apellido", FormValidators.fullName("Carlos").message)
        assertEquals("Usa solo letras y espacios", FormValidators.fullName("Carlos 123").message)
        assertTrue(FormValidators.fullName("María José Núñez").isValid)
    }

    @Test
    fun `campos opcionales limitan el largo`() {
        assertTrue(FormValidators.optionalText("").isValid)
        assertTrue(FormValidators.optionalText("Carpintero").isValid)
        assertFalse(FormValidators.optionalText("x".repeat(41)).isValid)
    }

    @Test
    fun `fortaleza de contrasena aumenta con la complejidad`() {
        assertEquals(0, FormValidators.passwordStrength(""))
        assertTrue(FormValidators.passwordStrength("abc123") < FormValidators.passwordStrength("Madera2026!"))
        assertEquals("Débil", FormValidators.passwordStrengthLabel(1))
        assertEquals("Media", FormValidators.passwordStrengthLabel(2))
        assertEquals("Segura", FormValidators.passwordStrengthLabel(4))
    }
}

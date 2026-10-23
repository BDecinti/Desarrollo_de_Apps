package com.example.myapplication.domain.validation

/**
 * Reglas de validación centralizadas para los formularios de la app.
 * Son funciones puras (sin dependencias de Android), por lo que se pueden probar con tests unitarios
 * y reutilizar desde cualquier ViewModel.
 */
object FormValidators {

    const val USERNAME_MIN = 4
    const val USERNAME_MAX = 20
    const val PASSWORD_MIN = 6
    const val OPTIONAL_TEXT_MAX = 40

    private val USERNAME_REGEX = Regex("^[A-Za-z0-9_.]+$")
    private val NAME_REGEX = Regex("^[\\p{L}][\\p{L} .'-]*$")

    // ---------- Inicio de sesión ----------

    fun loginUsername(value: String): ValidationResult =
        if (value.isBlank()) ValidationResult.invalid("Ingresa tu nombre de usuario") else ValidationResult.Valid

    fun loginPassword(value: String): ValidationResult =
        if (value.isBlank()) ValidationResult.invalid("Ingresa tu contraseña") else ValidationResult.Valid

    // ---------- Registro ----------

    fun registerUsername(value: String, isTaken: (String) -> Boolean = { false }): ValidationResult {
        val v = value.trim()
        return when {
            v.isEmpty() -> ValidationResult.invalid("El usuario es obligatorio")
            v.length < USERNAME_MIN -> ValidationResult.invalid("Mínimo $USERNAME_MIN caracteres")
            v.length > USERNAME_MAX -> ValidationResult.invalid("Máximo $USERNAME_MAX caracteres")
            !USERNAME_REGEX.matches(v) -> ValidationResult.invalid("Solo letras, números, punto o guion bajo")
            isTaken(v) -> ValidationResult.invalid("Este usuario ya está en uso")
            else -> ValidationResult.Valid
        }
    }

    fun registerPassword(value: String): ValidationResult = when {
        value.isBlank() -> ValidationResult.invalid("La contraseña es obligatoria")
        value.length < PASSWORD_MIN -> ValidationResult.invalid("Mínimo $PASSWORD_MIN caracteres")
        value.none { it.isLetter() } || value.none { it.isDigit() } ->
            ValidationResult.invalid("Incluye al menos una letra y un número")
        else -> ValidationResult.Valid
    }

    fun confirmPassword(password: String, confirmation: String): ValidationResult = when {
        confirmation.isBlank() -> ValidationResult.invalid("Confirma tu contraseña")
        confirmation != password -> ValidationResult.invalid("Las contraseñas no coinciden")
        else -> ValidationResult.Valid
    }

    fun fullName(value: String): ValidationResult {
        val v = value.trim()
        return when {
            v.isEmpty() -> ValidationResult.invalid("El nombre es obligatorio")
            !NAME_REGEX.matches(v) -> ValidationResult.invalid("Usa solo letras y espacios")
            v.split(Regex("\\s+")).size < 2 -> ValidationResult.invalid("Ingresa nombre y apellido")
            v.length < 5 -> ValidationResult.invalid("El nombre es demasiado corto")
            else -> ValidationResult.Valid
        }
    }

    /** Campos opcionales (ocupación, tema de interés): solo se limita el largo. */
    fun optionalText(value: String): ValidationResult =
        if (value.trim().length > OPTIONAL_TEXT_MAX) {
            ValidationResult.invalid("Máximo $OPTIONAL_TEXT_MAX caracteres")
        } else {
            ValidationResult.Valid
        }

    // ---------- Fortaleza de contraseña ----------

    /** Puntaje de 0 (vacía) a 4 (muy segura). */
    fun passwordStrength(value: String): Int {
        if (value.isEmpty()) return 0
        var score = 0
        if (value.length >= PASSWORD_MIN) score++
        if (value.length >= 10) score++
        if (value.any { it.isUpperCase() } && value.any { it.isLowerCase() }) score++
        if (value.any { !it.isLetterOrDigit() }) score++
        return score
    }

    fun passwordStrengthLabel(score: Int): String = when {
        score <= 1 -> "Débil"
        score == 2 -> "Media"
        else -> "Segura"
    }
}

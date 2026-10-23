package com.example.myapplication.domain.validation

/**
 * Resultado de validar un campo de formulario.
 * Es independiente de Compose: la UI solo lo lee para decidir qué mostrar.
 */
data class ValidationResult(
    val isValid: Boolean,
    val message: String? = null
) {
    companion object {
        val Valid = ValidationResult(isValid = true)
        fun invalid(message: String) = ValidationResult(isValid = false, message = message)
    }
}

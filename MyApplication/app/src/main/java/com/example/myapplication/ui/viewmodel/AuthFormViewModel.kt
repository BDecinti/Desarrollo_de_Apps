package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.domain.validation.FormValidators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AuthMode { LOGIN, REGISTER }

enum class AuthField { USERNAME, PASSWORD, CONFIRM_PASSWORD, FULL_NAME, OCCUPATION, INTEREST }

/**
 * Estado completo del formulario de acceso. La UI solo lo lee: todas las reglas
 * viven en [FormValidators] y se aplican desde [AuthFormViewModel].
 */
data class AuthFormState(
    val mode: AuthMode = AuthMode.LOGIN,
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullName: String = "",
    val occupation: String = "",
    val interest: String = "",
    val passwordVisible: Boolean = false,
    val touched: Set<AuthField> = emptySet(),
    val submitAttempted: Boolean = false,
    /** Todos los errores vigentes (visibles o no). */
    val errors: Map<AuthField, String> = emptyMap(),
    /** Se incrementa en cada envío inválido para disparar la animación de "sacudida". */
    val shakeCount: Int = 0
) {
    private fun revealed(field: AuthField) = submitAttempted || field in touched

    private fun valueOf(field: AuthField): String = when (field) {
        AuthField.USERNAME -> username
        AuthField.PASSWORD -> password
        AuthField.CONFIRM_PASSWORD -> confirmPassword
        AuthField.FULL_NAME -> fullName
        AuthField.OCCUPATION -> occupation
        AuthField.INTEREST -> interest
    }

    /** Error que se debe mostrar: solo después de que el usuario interactuó con el campo o intentó enviar. */
    fun errorFor(field: AuthField): String? = if (revealed(field)) errors[field] else null

    /** Campo completado correctamente (se muestra el ícono de éxito). */
    fun isFieldValid(field: AuthField): Boolean =
        revealed(field) && field !in errors && valueOf(field).isNotBlank()
}

class AuthFormViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository: AuthRepository? =
        runCatching { AuthRepository(application.applicationContext) }.getOrNull()

    private val _state = MutableStateFlow(AuthFormState())
    val state: StateFlow<AuthFormState> = _state.asStateFlow()

    fun onFieldChange(field: AuthField, value: String) {
        _state.update { current ->
            val next = when (field) {
                AuthField.USERNAME -> current.copy(username = value)
                AuthField.PASSWORD -> current.copy(password = value)
                AuthField.CONFIRM_PASSWORD -> current.copy(confirmPassword = value)
                AuthField.FULL_NAME -> current.copy(fullName = value)
                AuthField.OCCUPATION -> current.copy(occupation = value)
                AuthField.INTEREST -> current.copy(interest = value)
            }
            // Se marca como "tocado" al escribir, así el feedback es inmediato y por campo.
            revalidate(next.copy(touched = next.touched + field))
        }
    }

    fun onFieldBlur(field: AuthField) {
        _state.update { revalidate(it.copy(touched = it.touched + field)) }
    }

    fun togglePasswordVisibility() {
        _state.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun setMode(mode: AuthMode) {
        _state.update { current ->
            revalidate(
                current.copy(
                    mode = mode,
                    confirmPassword = "",
                    touched = emptySet(),
                    submitAttempted = false
                )
            )
        }
    }

    /** Valida todo el formulario. Devuelve true si se puede continuar. */
    fun submit(): Boolean {
        val current = revalidate(_state.value)
        val isValid = current.errors.isEmpty()
        _state.value = current.copy(
            submitAttempted = true,
            shakeCount = if (isValid) current.shakeCount else current.shakeCount + 1
        )
        return isValid
    }

    fun reset() {
        _state.value = AuthFormState()
    }

    private fun revalidate(state: AuthFormState): AuthFormState =
        state.copy(errors = collectErrors(state))

    private fun collectErrors(s: AuthFormState): Map<AuthField, String> {
        val results = when (s.mode) {
            AuthMode.LOGIN -> mapOf(
                AuthField.USERNAME to FormValidators.loginUsername(s.username),
                AuthField.PASSWORD to FormValidators.loginPassword(s.password)
            )
            AuthMode.REGISTER -> mapOf(
                AuthField.USERNAME to FormValidators.registerUsername(s.username) { name ->
                    authRepository?.isUsernameTaken(name) ?: false
                },
                AuthField.PASSWORD to FormValidators.registerPassword(s.password),
                AuthField.CONFIRM_PASSWORD to FormValidators.confirmPassword(s.password, s.confirmPassword),
                AuthField.FULL_NAME to FormValidators.fullName(s.fullName),
                AuthField.OCCUPATION to FormValidators.optionalText(s.occupation),
                AuthField.INTEREST to FormValidators.optionalText(s.interest)
            )
        }
        return results
            .filterValues { !it.isValid }
            .mapValues { it.value.message.orEmpty() }
    }
}

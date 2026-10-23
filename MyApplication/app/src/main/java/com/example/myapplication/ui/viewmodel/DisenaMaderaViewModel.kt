package com.example.myapplication.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.PreferencesManager
import com.example.myapplication.data.local.ProfilePhotoStorage
import com.example.myapplication.data.models.FichaTecnica
import com.example.myapplication.data.models.Usuario
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.FichaRepository
import com.example.myapplication.domain.climate.ClimateZone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class DisenaMaderaUiState(
    val searchQuery: String = "",
    val selectedCategoria: String? = null,
    val selectedSistema: String = "Todos",
    val selectedMaterial: String = "Todos",
    val selectedAplicacion: String = "Todas",
    val favoriteIds: Set<String> = emptySet(),
    val selectedFichaDetail: FichaTecnica? = null,
    val currentTab: Int = 0, // 0: Home, 1: Catalog, 2: Construcciones, 3: Normativa, 4: Favorites
    val isFilterDialogOpen: Boolean = false,
    val usuarioActual: Usuario? = null,
    val isLoginDialogOpen: Boolean = false,
    val loginErrorMessage: String? = null,
    val welcomeMessage: String? = null,
    val profilePhoto: Bitmap? = null,
    val climateZone: ClimateZone? = null
)

class DisenaMaderaViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val prefsManager: PreferencesManager? =
        runCatching { PreferencesManager(application.applicationContext) }.getOrNull()

    private val authRepository: AuthRepository? =
        runCatching { AuthRepository(application.applicationContext) }.getOrNull()

    private val photoStorage: ProfilePhotoStorage? =
        runCatching { ProfilePhotoStorage(application.applicationContext) }.getOrNull()

    private val _uiState = MutableStateFlow(DisenaMaderaUiState())
    val uiState: StateFlow<DisenaMaderaUiState> = _uiState.asStateFlow()

    private val _allFichas = MutableStateFlow(FichaRepository.fichasMuestra)

    init {
        // Carga favoritos, sesión y zona climática persistidos localmente
        val initialFavs = prefsManager?.getFavoriteIds() ?: setOf("ft_001", "ft_003")
        val savedUsername = prefsManager?.getLoggedUsername()
        val initialUser = savedUsername?.let { authRepository?.findUser(it) }

        _uiState.value = _uiState.value.copy(
            favoriteIds = initialFavs,
            usuarioActual = initialUser,
            climateZone = ClimateZone.fromLabel(prefsManager?.getClimateZoneLabel())
        )
        initialUser?.let { loadProfilePhoto(it.username) }
    }

    val filteredFichas: StateFlow<List<FichaTecnica>> = combine(
        _uiState,
        _allFichas
    ) { state, list ->
        list.filter { ficha ->
            // Search query filter
            val matchesQuery = if (state.searchQuery.isBlank()) {
                true
            } else {
                val q = state.searchQuery.trim().lowercase()
                ficha.titulo.lowercase().contains(q) ||
                        ficha.codigo.lowercase().contains(q) ||
                        ficha.descripcion.lowercase().contains(q) ||
                        ficha.sistemaConstructivo.lowercase().contains(q) ||
                        ficha.material.lowercase().contains(q) ||
                        ficha.aplicacion.lowercase().contains(q)
            }

            // Category filter
            val matchesCat = state.selectedCategoria == null ||
                    ficha.categoria.equals(state.selectedCategoria, ignoreCase = true)

            // Constructive system filter
            val matchesSistema = state.selectedSistema == "Todos" ||
                    ficha.sistemaConstructivo.equals(state.selectedSistema, ignoreCase = true)

            // Material filter
            val matchesMaterial = state.selectedMaterial == "Todos" ||
                    ficha.material.equals(state.selectedMaterial, ignoreCase = true)

            // Application filter
            val matchesAplicacion = state.selectedAplicacion == "Todas" ||
                    ficha.aplicacion.equals(state.selectedAplicacion, ignoreCase = true)

            matchesQuery && matchesCat && matchesSistema && matchesMaterial && matchesAplicacion
        }.map { ficha ->
            ficha.copy(esFavorito = state.favoriteIds.contains(ficha.id))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FichaRepository.fichasMuestra
    )

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun onCategoriaSelected(categoria: String?) {
        val current = _uiState.value.selectedCategoria
        // Toggle if same category clicked
        val next = if (current == categoria) null else categoria
        _uiState.value = _uiState.value.copy(selectedCategoria = next)
    }

    fun onSistemaSelected(sistema: String) {
        _uiState.value = _uiState.value.copy(selectedSistema = sistema)
    }

    fun onMaterialSelected(material: String) {
        _uiState.value = _uiState.value.copy(selectedMaterial = material)
    }

    fun onAplicacionSelected(aplicacion: String) {
        _uiState.value = _uiState.value.copy(selectedAplicacion = aplicacion)
    }

    fun resetFilters() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            selectedCategoria = null,
            selectedSistema = "Todos",
            selectedMaterial = "Todos",
            selectedAplicacion = "Todas"
        )
    }

    fun toggleFavorite(fichaId: String) {
        val currentFavs = _uiState.value.favoriteIds.toMutableSet()
        if (currentFavs.contains(fichaId)) {
            currentFavs.remove(fichaId)
        } else {
            currentFavs.add(fichaId)
        }

        // Persist favorites locally in SharedPreferences
        prefsManager?.saveFavoriteIds(currentFavs)

        val updatedFichaDetail = _uiState.value.selectedFichaDetail?.let { detail ->
            if (detail.id == fichaId) {
                detail.copy(esFavorito = currentFavs.contains(fichaId))
            } else detail
        }
        _uiState.value = _uiState.value.copy(
            favoriteIds = currentFavs,
            selectedFichaDetail = updatedFichaDetail
        )
    }

    fun selectFichaDetail(ficha: FichaTecnica?) {
        val detailWithFav = ficha?.copy(esFavorito = _uiState.value.favoriteIds.contains(ficha.id))
        _uiState.value = _uiState.value.copy(selectedFichaDetail = detailWithFav)
    }

    fun selectTab(tabIndex: Int) {
        _uiState.value = _uiState.value.copy(currentTab = tabIndex)
    }

    fun setFilterDialogOpen(isOpen: Boolean) {
        _uiState.value = _uiState.value.copy(isFilterDialogOpen = isOpen)
    }

    fun login(usernameInput: String, passwordInput: String): Boolean {
        val userMatch = authRepository?.authenticate(usernameInput, passwordInput)
        return if (userMatch != null) {
            startSession(userMatch)
            true
        } else {
            _uiState.value = _uiState.value.copy(
                loginErrorMessage = "Usuario o contraseña incorrectos"
            )
            false
        }
    }

    fun loginAsUser(user: Usuario) {
        startSession(user)
    }

    fun registerAndLogin(
        username: String,
        pass: String,
        nombre: String,
        perfil: String,
        busquedaInteres: String
    ): Boolean {
        val newUser = authRepository?.register(username, pass, nombre, perfil, busquedaInteres)
        if (newUser == null) {
            _uiState.value = _uiState.value.copy(
                loginErrorMessage = "No se pudo crear la cuenta: el usuario ya existe"
            )
            return false
        }
        startSession(newUser)
        return true
    }

    private fun startSession(user: Usuario) {
        prefsManager?.saveLoggedUsername(user.username)
        _uiState.value = _uiState.value.copy(
            usuarioActual = user,
            profilePhoto = null,
            isLoginDialogOpen = false,
            loginErrorMessage = null,
            welcomeMessage = "Bienvenido, vamos a empezar en grande"
        )
        loadProfilePhoto(user.username)
    }

    // ---------- Recurso nativo: foto de perfil (cámara) ----------

    private fun loadProfilePhoto(username: String) {
        val storage = photoStorage ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = storage.load(username)
            _uiState.update { state ->
                if (state.usuarioActual?.username == username) state.copy(profilePhoto = bitmap) else state
            }
        }
    }

    fun onProfilePhotoCaptured(captured: File) {
        val username = _uiState.value.usuarioActual?.username ?: return
        val storage = photoStorage ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = storage.saveFromCapture(username, captured)
            if (bitmap != null) {
                _uiState.update { state ->
                    if (state.usuarioActual?.username == username) state.copy(profilePhoto = bitmap) else state
                }
            }
        }
    }

    // ---------- Recurso nativo: ubicación -> zona climática ----------

    /** Devuelve false si las coordenadas están fuera de Chile y no se puede determinar la zona. */
    fun onLocationDetected(latitude: Double, longitude: Double): Boolean {
        val zone = ClimateZone.fromCoordinates(latitude, longitude) ?: return false
        prefsManager?.saveClimateZoneLabel(zone.label)
        _uiState.update { it.copy(climateZone = zone) }
        return true
    }

    fun clearWelcomeMessage() {
        _uiState.value = _uiState.value.copy(welcomeMessage = null)
    }

    fun logout() {
        prefsManager?.saveLoggedUsername(null)
        _uiState.value = _uiState.value.copy(
            usuarioActual = null,
            profilePhoto = null
        )
    }

    fun clearLoginError() {
        if (_uiState.value.loginErrorMessage != null) {
            _uiState.value = _uiState.value.copy(loginErrorMessage = null)
        }
    }

    fun setLoginDialogOpen(isOpen: Boolean) {
        _uiState.value = _uiState.value.copy(
            isLoginDialogOpen = isOpen,
            loginErrorMessage = null
        )
    }
}

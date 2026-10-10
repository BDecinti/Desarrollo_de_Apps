package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.models.FichaTecnica
import com.example.myapplication.data.repository.FichaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DisenaMaderaUiState(
    val searchQuery: String = "",
    val selectedCategoria: String? = null,
    val selectedSistema: String = "Todos",
    val selectedMaterial: String = "Todos",
    val selectedAplicacion: String = "Todas",
    val favoriteIds: Set<String> = setOf("ft_001", "ft_003"), // default demo favorites
    val selectedFichaDetail: FichaTecnica? = null,
    val currentTab: Int = 0, // 0: Home, 1: Catalog, 2: Normativa, 3: Favorites
    val isFilterDialogOpen: Boolean = false
)

class DisenaMaderaViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DisenaMaderaUiState())
    val uiState: StateFlow<DisenaMaderaUiState> = _uiState.asStateFlow()

    private val _allFichas = MutableStateFlow(FichaRepository.fichasMuestra)

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
}

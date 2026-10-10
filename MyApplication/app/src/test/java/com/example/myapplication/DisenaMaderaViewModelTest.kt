package com.example.myapplication

import com.example.myapplication.ui.viewmodel.DisenaMaderaViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DisenaMaderaViewModelTest {

    private lateinit var viewModel: DisenaMaderaViewModel

    @Before
    fun setUp() {
        viewModel = DisenaMaderaViewModel()
    }

    @Test
    fun `initial uiState contains default values`() {
        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertNull(state.selectedCategoria)
        assertEquals("Todos", state.selectedSistema)
        assertEquals("Todos", state.selectedMaterial)
        assertEquals("Todas", state.selectedAplicacion)
        assertEquals(0, state.currentTab)
    }

    @Test
    fun `onSearchQueryChanged updates searchQuery state`() {
        viewModel.onSearchQueryChanged("CLT")
        assertEquals("CLT", viewModel.uiState.value.searchQuery)
    }

    @Test
    fun `onCategoriaSelected toggles selection`() {
        viewModel.onCategoriaSelected("Estructuras")
        assertEquals("Estructuras", viewModel.uiState.value.selectedCategoria)

        // Select same category again to deselect
        viewModel.onCategoriaSelected("Estructuras")
        assertNull(viewModel.uiState.value.selectedCategoria)
    }

    @Test
    fun `resetFilters restores initial state`() {
        viewModel.onSearchQueryChanged("Test")
        viewModel.onCategoriaSelected("Cerramientos")
        viewModel.onSistemaSelected("Marco Plataforma")

        viewModel.resetFilters()

        val state = viewModel.uiState.value
        assertEquals("", state.searchQuery)
        assertNull(state.selectedCategoria)
        assertEquals("Todos", state.selectedSistema)
    }

    @Test
    fun `toggleFavorite adds and removes favorite ID`() {
        val testId = "ft_004"
        assertFalse(viewModel.uiState.value.favoriteIds.contains(testId))

        viewModel.toggleFavorite(testId)
        assertTrue(viewModel.uiState.value.favoriteIds.contains(testId))

        viewModel.toggleFavorite(testId)
        assertFalse(viewModel.uiState.value.favoriteIds.contains(testId))
    }
}

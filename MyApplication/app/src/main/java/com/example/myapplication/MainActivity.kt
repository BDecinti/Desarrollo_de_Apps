package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.components.FilterDialog
import com.example.myapplication.ui.screens.CatalogScreen
import com.example.myapplication.ui.screens.DetailScreen
import com.example.myapplication.ui.screens.FavoritesScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.NormativaScreen
import com.example.myapplication.ui.theme.DisenaMaderaTheme
import com.example.myapplication.ui.viewmodel.DisenaMaderaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: DisenaMaderaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DisenaMaderaTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: DisenaMaderaViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredFichas by viewModel.filteredFichas.collectAsState()

    // Filtered list for favorites screen
    val favoriteFichas = filteredFichas.filter { it.esFavorito }

    val selectedFicha = uiState.selectedFichaDetail

    if (selectedFicha != null) {
        // Detail Screen Overlay
        DetailScreen(
            ficha = selectedFicha,
            onBackClick = { viewModel.selectFichaDetail(null) },
            onFavoriteToggle = { viewModel.toggleFavorite(it) }
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = uiState.currentTab == 0,
                        onClick = { viewModel.selectTab(0) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = uiState.currentTab == 1,
                        onClick = { viewModel.selectTab(1) },
                        icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Catálogo") },
                        label = { Text("Catálogo") }
                    )
                    NavigationBarItem(
                        selected = uiState.currentTab == 2,
                        onClick = { viewModel.selectTab(2) },
                        icon = { Icon(Icons.Default.Gavel, contentDescription = "Normativa") },
                        label = { Text("Normativa") }
                    )
                    NavigationBarItem(
                        selected = uiState.currentTab == 3,
                        onClick = { viewModel.selectTab(3) },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
                        label = { Text("Favoritos") }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (uiState.currentTab) {
                    0 -> {
                        HomeScreen(
                            searchQuery = uiState.searchQuery,
                            onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                            onNavigateToCatalog = { viewModel.selectTab(1) },
                            onCategoriaClick = { viewModel.onCategoriaSelected(it) },
                            featuredFichas = filteredFichas.filter { it.esDestacado },
                            onFichaClick = { viewModel.selectFichaDetail(it) },
                            onFavoriteToggle = { viewModel.toggleFavorite(it) }
                        )
                    }
                    1 -> {
                        CatalogScreen(
                            searchQuery = uiState.searchQuery,
                            onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                            selectedCategoria = uiState.selectedCategoria,
                            selectedSistema = uiState.selectedSistema,
                            selectedMaterial = uiState.selectedMaterial,
                            selectedAplicacion = uiState.selectedAplicacion,
                            onCategoriaRemove = { viewModel.onCategoriaSelected(null) },
                            onSistemaRemove = { viewModel.onSistemaSelected("Todos") },
                            onMaterialRemove = { viewModel.onMaterialSelected("Todos") },
                            onAplicacionRemove = { viewModel.onAplicacionSelected("Todas") },
                            onOpenFilterDialog = { viewModel.setFilterDialogOpen(true) },
                            fichas = filteredFichas,
                            onFichaClick = { viewModel.selectFichaDetail(it) },
                            onFavoriteToggle = { viewModel.toggleFavorite(it) },
                            onResetFilters = { viewModel.resetFilters() }
                        )
                    }
                    2 -> {
                        NormativaScreen()
                    }
                    3 -> {
                        FavoritesScreen(
                            favoriteFichas = favoriteFichas,
                            onFichaClick = { viewModel.selectFichaDetail(it) },
                            onFavoriteToggle = { viewModel.toggleFavorite(it) },
                            onExploreClick = { viewModel.selectTab(1) }
                        )
                    }
                }
            }
        }

        // Filter Dialog Overlay
        if (uiState.isFilterDialogOpen) {
            FilterDialog(
                selectedSistema = uiState.selectedSistema,
                selectedMaterial = uiState.selectedMaterial,
                selectedAplicacion = uiState.selectedAplicacion,
                onSistemaSelected = { viewModel.onSistemaSelected(it) },
                onMaterialSelected = { viewModel.onMaterialSelected(it) },
                onAplicacionSelected = { viewModel.onAplicacionSelected(it) },
                onResetFilters = { viewModel.resetFilters() },
                onDismiss = { viewModel.setFilterDialogOpen(false) }
            )
        }
    }
}

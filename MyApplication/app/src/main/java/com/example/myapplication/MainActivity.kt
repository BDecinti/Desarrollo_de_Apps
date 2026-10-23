package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Carpenter
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.myapplication.data.models.FichaTecnica
import com.example.myapplication.device.rememberCameraAction
import com.example.myapplication.ui.components.FilterDialog
import com.example.myapplication.ui.components.LoginDialog
import com.example.myapplication.ui.screens.CatalogScreen
import com.example.myapplication.ui.screens.ConstruccionesBasicasScreen
import com.example.myapplication.ui.screens.DetailScreen
import com.example.myapplication.ui.screens.FavoritesScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.NormativaScreen
import com.example.myapplication.ui.theme.DisenaMaderaTheme
import com.example.myapplication.ui.viewmodel.AuthFormViewModel
import com.example.myapplication.ui.viewmodel.AuthMode
import com.example.myapplication.ui.viewmodel.DisenaMaderaViewModel
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val viewModel: DisenaMaderaViewModel by viewModels()
    private val authFormViewModel: AuthFormViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DisenaMaderaTheme {
                MainAppScreen(viewModel = viewModel, authFormViewModel = authFormViewModel)
            }
        }
    }
}

private data class BottomTab(val label: String, val icon: ImageVector)

private val bottomTabs = listOf(
    BottomTab("Inicio", Icons.Default.Home),
    BottomTab("Catálogo", Icons.AutoMirrored.Filled.MenuBook),
    BottomTab("Muebles", Icons.Default.Carpenter),
    BottomTab("Normativa", Icons.Default.Gavel),
    BottomTab("Favoritos", Icons.Default.Favorite)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: DisenaMaderaViewModel,
    authFormViewModel: AuthFormViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredFichas by viewModel.filteredFichas.collectAsState()
    val authFormState by authFormViewModel.state.collectAsState()
    val context = LocalContext.current

    // Mensaje de bienvenida temporal (4,5 segundos)
    LaunchedEffect(uiState.welcomeMessage) {
        if (uiState.welcomeMessage != null) {
            delay(4500)
            viewModel.clearWelcomeMessage()
        }
    }

    // El formulario de acceso siempre arranca limpio al abrirse o cerrarse
    LaunchedEffect(uiState.isLoginDialogOpen) {
        authFormViewModel.reset()
    }

    // Recurso nativo: cámara para la foto de perfil
    val takeProfilePhoto = rememberCameraAction(
        onPhotoCaptured = { viewModel.onProfilePhotoCaptured(it) },
        onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
    )

    val favoriteFichas = filteredFichas.filter { it.esFavorito }
    val selectedFicha = uiState.selectedFichaDetail

    // Se recuerda la última ficha para poder animar la salida de la pantalla de detalle
    var lastFicha by remember { mutableStateOf<FichaTecnica?>(null) }
    SideEffect { if (selectedFicha != null) lastFicha = selectedFicha }
    val fichaToShow = selectedFicha ?: lastFicha

    // El botón "atrás" del sistema cierra el detalle en lugar de salir de la app
    BackHandler(enabled = selectedFicha != null) { viewModel.selectFichaDetail(null) }

    // Íconos de la barra de estado legibles según el fondo de cada pantalla
    val darkTheme = isSystemInDarkTheme()
    val view = LocalView.current
    val lightStatusBarIcons = !darkTheme && (selectedFicha != null || uiState.currentTab != 0)
    SideEffect {
        val window = (view.context as? Activity)?.window
        if (window != null) {
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = lightStatusBarIcons
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                NavigationBar {
                    bottomTabs.forEachIndexed { index, tab ->
                        NavigationBarItem(
                            selected = uiState.currentTab == index,
                            onClick = { viewModel.selectTab(index) },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = {
                                Text(
                                    text = tab.label,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 11.sp
                                )
                            }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    // La pantalla de inicio dibuja su encabezado detrás de la barra de estado
                    .then(if (uiState.currentTab != 0) Modifier.statusBarsPadding() else Modifier)
            ) {
                AnimatedContent(
                    targetState = uiState.currentTab,
                    transitionSpec = {
                        val forward = targetState > initialState
                        (fadeIn(tween(250)) + slideInHorizontally(tween(250)) { if (forward) it / 8 else -it / 8 }) togetherWith
                                (fadeOut(tween(150)) + slideOutHorizontally(tween(250)) { if (forward) -it / 8 else it / 8 })
                    },
                    label = "TabTransition"
                ) { tabIndex ->
                    when (tabIndex) {
                        0 -> HomeScreen(
                            searchQuery = uiState.searchQuery,
                            onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                            onNavigateToCatalog = { viewModel.selectTab(1) },
                            onCategoriaClick = { viewModel.onCategoriaSelected(it) },
                            featuredFichas = filteredFichas.filter { it.esDestacado },
                            onFichaClick = { viewModel.selectFichaDetail(it) },
                            onFavoriteToggle = { viewModel.toggleFavorite(it) },
                            usuarioActual = uiState.usuarioActual,
                            profilePhoto = uiState.profilePhoto,
                            onTakePhoto = takeProfilePhoto,
                            onLoginClick = { viewModel.setLoginDialogOpen(true) },
                            onLogoutClick = { viewModel.logout() },
                            onNavigateToConstrucciones = { viewModel.selectTab(2) }
                        )

                        1 -> CatalogScreen(
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

                        2 -> ConstruccionesBasicasScreen(
                            climateZone = uiState.climateZone,
                            onLocationDetected = { lat, lon -> viewModel.onLocationDetected(lat, lon) }
                        )

                        3 -> NormativaScreen()

                        else -> FavoritesScreen(
                            favoriteFichas = favoriteFichas,
                            onFichaClick = { viewModel.selectFichaDetail(it) },
                            onFavoriteToggle = { viewModel.toggleFavorite(it) },
                            onExploreClick = { viewModel.selectTab(1) }
                        )
                    }
                }
            }
        }

        // Banner de bienvenida temporal (4,5 s)
        AnimatedVisibility(
            visible = uiState.welcomeMessage != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it }),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        ) {
            uiState.welcomeMessage?.let { msg ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                            Text(
                                text = "Sugerencias personalizadas cargadas correctamente.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }
        }

        // Pantalla de detalle: entra deslizándose desde la derecha sobre la navegación principal
        AnimatedVisibility(
            visible = selectedFicha != null,
            enter = slideInHorizontally(tween(300)) { it } + fadeIn(tween(300)),
            exit = slideOutHorizontally(tween(250)) { it } + fadeOut(tween(250))
        ) {
            // El Surface evita que los toques atraviesen la pantalla hacia la que está debajo
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                fichaToShow?.let { ficha ->
                    DetailScreen(
                        ficha = ficha,
                        onBackClick = { viewModel.selectFichaDetail(null) },
                        onFavoriteToggle = { viewModel.toggleFavorite(it) }
                    )
                }
            }
        }
    }

    // Diálogo de filtros
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

    // Diálogo de inicio de sesión / registro (la validación vive en AuthFormViewModel)
    if (uiState.isLoginDialogOpen) {
        LoginDialog(
            state = authFormState,
            serverError = uiState.loginErrorMessage,
            onModeChange = {
                authFormViewModel.setMode(it)
                viewModel.clearLoginError()
            },
            onFieldChange = { field, value ->
                authFormViewModel.onFieldChange(field, value)
                viewModel.clearLoginError()
            },
            onFieldBlur = { authFormViewModel.onFieldBlur(it) },
            onTogglePasswordVisibility = { authFormViewModel.togglePasswordVisibility() },
            onSubmit = {
                if (authFormViewModel.submit()) {
                    val form = authFormViewModel.state.value
                    if (form.mode == AuthMode.LOGIN) {
                        viewModel.login(form.username, form.password)
                    } else {
                        viewModel.registerAndLogin(
                            form.username,
                            form.password,
                            form.fullName,
                            form.occupation,
                            form.interest
                        )
                    }
                }
            },
            onQuickUserSelect = { viewModel.loginAsUser(it) },
            onDismiss = { viewModel.setLoginDialogOpen(false) }
        )
    }
}

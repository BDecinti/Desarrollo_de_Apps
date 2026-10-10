package com.example.myapplication.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.repository.FichaRepository

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterDialog(
    selectedSistema: String,
    selectedMaterial: String,
    selectedAplicacion: String,
    onSistemaSelected: (String) -> Unit,
    onMaterialSelected: (String) -> Unit,
    onAplicacionSelected: (String) -> Unit,
    onResetFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Filtrar Soluciones Constructivas",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sistema Constructivo
                Text(
                    text = "Sistema Constructivo",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FichaRepository.sistemasConstructivos.forEach { sistema ->
                        FilterChip(
                            selected = (selectedSistema == sistema),
                            onClick = { onSistemaSelected(sistema) },
                            label = { Text(sistema) }
                        )
                    }
                }

                HorizontalDivider()

                // Material
                Text(
                    text = "Material de Estructura",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FichaRepository.materiales.forEach { mat ->
                        FilterChip(
                            selected = (selectedMaterial == mat),
                            onClick = { onMaterialSelected(mat) },
                            label = { Text(mat) }
                        )
                    }
                }

                HorizontalDivider()

                // Aplicacion
                Text(
                    text = "Aplicación / Ubicación",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FichaRepository.aplicaciones.forEach { app ->
                        FilterChip(
                            selected = (selectedAplicacion == app),
                            onClick = { onAplicacionSelected(app) },
                            label = { Text(app) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Aplicar Filtros")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onResetFilters()
                onDismiss()
            }) {
                Text("Limpiar Todo")
            }
        }
    )
}

package com.example.myapplication.data.models

data class FichaTecnica(
    val id: String,
    val codigo: String,
    val titulo: String,
    val categoria: String,
    val sistemaConstructivo: String,
    val material: String,
    val aplicacion: String,
    val descripcion: String,
    val resistenciaFuego: String,
    val aislacionTermica: String,
    val aislacionAcustica: String,
    val dimensionesEstandard: String,
    val normativaRelacionada: List<String>,
    val documentoPdfNombre: String,
    val esDestacado: Boolean = false,
    val esFavorito: Boolean = false
)

data class CategoriaItem(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val cantidadFichas: Int
)

data class NormativaItem(
    val id: String,
    val codigo: String,
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val enlaceDocumento: String
)

data class HerramientaCalculo(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val tipo: String
)

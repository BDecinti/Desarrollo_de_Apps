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

data class RecomendacionMadera(
    val nombreMadera: String,
    val categoriaClima: String, // "Cálido y Seco", "Húmedo y Exterior", "Interior y Templado"
    val categoriaUtilidad: String, // "Estética y Elegancia", "Funcional y Económico", "Alta Resistencia"
    val porqueRecomendada: String,
    val caracteristicasClave: List<String>
)

data class ConstruccionBasica(
    val id: String,
    val titulo: String,
    val categoria: String, // "Muebles de Interior", "Almacenamiento", "Muebles de Exterior", "Estructuras Livianas"
    val descripcion: String,
    val dificultadEjecucion: String, // "Fácil", "Intermedio", "Avanzado"
    val consejosConstruccion: List<String>,
    val recomendacionesMadera: List<RecomendacionMadera>
)

data class SugerenciaPersonalizada(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val motivo: String,
    val tipo: String, // "Mueble", "Ficha Técnica", "Normativa", "Material"
    val badgeTexto: String
)

data class Usuario(
    val id: String,
    val username: String,
    val password: String,
    val nombreCompleto: String,
    val perfilOcupacion: String,
    val avatarIniciales: String,
    val historialBusquedas: List<String>,
    val sugerenciasPersonalizadas: List<SugerenciaPersonalizada>
)

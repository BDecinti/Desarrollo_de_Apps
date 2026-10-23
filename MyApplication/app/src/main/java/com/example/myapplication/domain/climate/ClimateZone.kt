package com.example.myapplication.domain.climate

/**
 * Zonas climáticas simplificadas de Chile, alineadas con las categorías de clima
 * usadas en las recomendaciones de madera ([label] coincide con `RecomendacionMadera.categoriaClima`).
 */
enum class ClimateZone(
    val label: String,
    val region: String,
    val resumen: String
) {
    CALIDO_SECO(
        label = "Cálido y Seco",
        region = "Norte de Chile",
        resumen = "Alta radiación UV y baja humedad: prioriza estabilidad dimensional y protección solar."
    ),
    TEMPLADO(
        label = "Interior y Templado",
        region = "Zona central",
        resumen = "Clima mediterráneo: la mayoría de las maderas funciona bien con un buen sellado."
    ),
    HUMEDO(
        label = "Húmedo y Exterior",
        region = "Sur de Chile",
        resumen = "Lluvia frecuente: elige maderas durables o tratadas y cuida el escurrimiento de agua."
    );

    companion object {
        /**
         * Aproximación por latitud (no reemplaza una zonificación térmica oficial).
         * Devuelve null si las coordenadas quedan fuera del territorio continental de Chile.
         */
        fun fromCoordinates(latitude: Double, longitude: Double): ClimateZone? {
            if (latitude !in -56.0..-17.0 || longitude !in -76.0..-66.0) return null
            return when {
                latitude > -32.0 -> CALIDO_SECO
                latitude > -38.5 -> TEMPLADO
                else -> HUMEDO
            }
        }

        fun fromLabel(label: String?): ClimateZone? = entries.firstOrNull { it.label == label }
    }
}

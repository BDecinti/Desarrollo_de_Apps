package com.example.myapplication.domain

import com.example.myapplication.domain.climate.ClimateZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ClimateZoneTest {

    @Test
    fun `Santiago es zona templada`() {
        assertEquals(ClimateZone.TEMPLADO, ClimateZone.fromCoordinates(-33.45, -70.66))
    }

    @Test
    fun `Antofagasta es zona calida y seca`() {
        assertEquals(ClimateZone.CALIDO_SECO, ClimateZone.fromCoordinates(-23.65, -70.40))
    }

    @Test
    fun `Temuco y Puerto Montt son zona humeda`() {
        assertEquals(ClimateZone.HUMEDO, ClimateZone.fromCoordinates(-38.74, -72.59))
        assertEquals(ClimateZone.HUMEDO, ClimateZone.fromCoordinates(-41.47, -72.94))
    }

    @Test
    fun `coordenadas fuera de Chile no tienen zona`() {
        assertNull(ClimateZone.fromCoordinates(40.41, -3.70)) // Madrid
        assertNull(ClimateZone.fromCoordinates(-34.60, -58.38)) // Buenos Aires
    }

    @Test
    fun `se recupera la zona desde su etiqueta`() {
        assertEquals(ClimateZone.HUMEDO, ClimateZone.fromLabel("Húmedo y Exterior"))
        assertNull(ClimateZone.fromLabel("Inexistente"))
        assertNull(ClimateZone.fromLabel(null))
    }
}

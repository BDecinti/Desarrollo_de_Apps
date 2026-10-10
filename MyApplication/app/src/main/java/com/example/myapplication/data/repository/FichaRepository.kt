package com.example.myapplication.data.repository

import com.example.myapplication.data.models.CategoriaItem
import com.example.myapplication.data.models.FichaTecnica
import com.example.myapplication.data.models.HerramientaCalculo
import com.example.myapplication.data.models.NormativaItem

object FichaRepository {

    val categorias = listOf(
        CategoriaItem("cat_1", "Estructuras", "Elementos soportantes principales de edificación", 3),
        CategoriaItem("cat_2", "Cerramientos", "Muros divisorios, perimetrales y tabiquería", 2),
        CategoriaItem("cat_3", "Entrepisos", "Soluciones de pisos intermedios y losas livianas", 1),
        CategoriaItem("cat_4", "Techumbres", "Estructuras de cubierta y cielos aislados", 1),
        CategoriaItem("cat_5", "Revestimientos", "Soluciones de fachada ventilada y acabado exterior", 1)
    )

    val sistemasConstructivos = listOf(
        "Todos",
        "Marco Plataforma",
        "CLT (Madera Contralaminada)",
        "Madera Laminada Encolada (MLE)",
        "Poste y Viga"
    )

    val materiales = listOf(
        "Todos",
        "Pino Radiata Impregnado",
        "CLT 5 Capas",
        "CLT 3 Capas",
        "MLE Pino Radiata",
        "Viguetas I-Joist / Pino",
        "Pino Radiata Termotratado"
    )

    val aplicaciones = listOf(
        "Todas",
        "Muro Exterior",
        "Muro Divisorio",
        "Muro Estructural / Muro Exterior",
        "Techumbre / Vigas",
        "Entrepiso",
        "Cubierta",
        "Tabique Interior"
    )

    val fichasMuestra = listOf(
        FichaTecnica(
            id = "ft_001",
            codigo = "FT-EST-001",
            titulo = "Muro Perimetral Marco Plataforma 2x4\" Aislado",
            categoria = "Estructuras",
            sistemaConstructivo = "Marco Plataforma",
            material = "Pino Radiata Impregnado",
            aplicacion = "Muro Exterior",
            descripcion = "Solución constructiva tradicional de pies derechos 2x4\" en Pino Radiata impregnado Grado C16/C24, barrera de humedad, placa estructural OSB 11.1mm y lana mineral 80mm.",
            resistenciaFuego = "F-30",
            aislacionTermica = "U = 0.35 W/m²K (Cumple Zona 1 a 6 OGUC)",
            aislacionAcustica = "STC 48 dB",
            dimensionesEstandard = "Pie derechos 2x4\" a 40cm eje, lana mineral 80mm",
            normativaRelacionada = listOf("NCh 1198 (Diseño Estructural)", "OGUC Art. 4.1.10 (Térmico)", "NCh 935/1 (Fuego)"),
            documentoPdfNombre = "FT_EST_001_Marco_Plataforma.pdf",
            esDestacado = true
        ),
        FichaTecnica(
            id = "ft_002",
            codigo = "FT-CER-002",
            titulo = "Muro Cortafuego Doble Marco 2x4\" Volcanita ST",
            categoria = "Cerramientos",
            sistemaConstructivo = "Marco Plataforma",
            material = "Pino Radiata Impregnado",
            aplicacion = "Muro Divisorio",
            descripcion = "Muro cortafuego y divisorio entre unidades de vivienda. Consta de doble estructura de madera independiente con aislamiento acústico térmico y doble placa de yeso cartón RH/ST de 15mm por cara.",
            resistenciaFuego = "F-90",
            aislacionTermica = "U = 0.30 W/m²K",
            aislacionAcustica = "STC 56 dB",
            dimensionesEstandard = "Doble marco 2x4\" desfasado 10mm, lana de vidrio 100mm x2",
            normativaRelacionada = listOf("NCh 935/1 (Resistencia al Fuego)", "NCh 352 (Acústica)", "OGUC Art. 4.3.3"),
            documentoPdfNombre = "FT_CER_002_Cortafuego_F90.pdf",
            esDestacado = true
        ),
        FichaTecnica(
            id = "ft_003",
            codigo = "FT-CLT-001",
            titulo = "Panel Muro CLT 5 Capas (100mm) Estructural",
            categoria = "Estructuras",
            sistemaConstructivo = "CLT (Madera Contralaminada)",
            material = "CLT 5 Capas",
            aplicacion = "Muro Estructural / Muro Exterior",
            descripcion = "Panel masivo de madera contralaminada CLT de 100mm de espesor (5 capas de 20mm). Alta rigidez en el plano y fuera del plano, prefabricado con cortes de vanos por CNC.",
            resistenciaFuego = "F-60 (Cálculo por carbonatación NCh 1198)",
            aislacionTermica = "U = 0.42 W/m²K (Requiere EIFS exterior)",
            aislacionAcustica = "STC 50 dB",
            dimensionesEstandard = "Ancho hasta 3.0m, Largo hasta 12.0m, Espesor 100mm",
            normativaRelacionada = listOf("PR NCh 3600 (CLT)", "NCh 1198 Anexo F", "OGUC Art. 5.1.22"),
            documentoPdfNombre = "FT_CLT_001_Panel_Muro.pdf",
            esDestacado = true
        ),
        FichaTecnica(
            id = "ft_004",
            codigo = "FT-MLE-001",
            titulo = "Viga Laminada Estructural MLE Grado C24",
            categoria = "Estructuras",
            sistemaConstructivo = "Madera Laminada Encolada (MLE)",
            material = "MLE Pino Radiata",
            aplicacion = "Techumbre / Vigas",
            descripcion = "Elemento lineal de madera laminada encolada con adhesivo estructural mUPF/PRF. Diseñada para salvar luces grandes en gimnasios, salas multiuso y viviendas residenciales.",
            resistenciaFuego = "F-30 / F-60 segun escuadría",
            aislacionTermica = "No aplica (Elemento puntual)",
            aislacionAcustica = "No aplica",
            dimensionesEstandard = "Escuadrías variables desde 90x240mm hasta 200x800mm",
            normativaRelacionada = listOf("NCh 2148 (MLE Requisitos)", "NCh 2150 (MLE Inspección)", "NCh 1198"),
            documentoPdfNombre = "FT_MLE_001_Viga_Laminada.pdf",
            esDestacado = false
        ),
        FichaTecnica(
            id = "ft_005",
            codigo = "FT-ENT-001",
            titulo = "Entrepiso Marco Madera Viguetas I-Joist 241mm",
            categoria = "Entrepisos",
            sistemaConstructivo = "Marco Plataforma",
            material = "Viguetas I-Joist / Pino",
            aplicacion = "Entrepiso",
            descripcion = "Sistema de entrepiso liviano de alto desempeño con viguetas compuestas tipo I-Joist, placa fenólica OSB 18mm con lengüeta y ranura, y amortiguación de impacto acústico.",
            resistenciaFuego = "F-30 (Con cielo falso Volcanita 15mm)",
            aislacionTermica = "U = 0.40 W/m²K",
            aislacionAcustica = "STC 52 dB / Ln,w 58 dB",
            dimensionesEstandard = "Viguetas H=241mm @ 40cm/60cm eje",
            normativaRelacionada = listOf("NCh 1198 (Cálculo entrepisos)", "OGUC 4.1.10", "NCh 352"),
            documentoPdfNombre = "FT_ENT_001_Entrepiso_IJoist.pdf",
            esDestacado = false
        ),
        FichaTecnica(
            id = "ft_006",
            codigo = "FT-TEC-001",
            titulo = "Techumbre Cercha Madera Pino Radiata Ventilada",
            categoria = "Techumbres",
            sistemaConstructivo = "Marco Plataforma",
            material = "Pino Radiata Impregnado",
            aplicacion = "Cubierta",
            descripcion = "Sistema de cubierta liviana mediante cerchas industrializadas conectadas con placas dentadas. Incluye barrera anticondensación, camara de aire ventilada y barrera térmica sobre cielo.",
            resistenciaFuego = "F-15",
            aislacionTermica = "U = 0.25 W/m²K (Aislación lana mineral 120mm sobre cielo)",
            aislacionAcustica = "STC 42 dB",
            dimensionesEstandard = "Pendiente 15° - 35°, escuadrías 2x3\" / 2x4\"",
            normativaRelacionada = listOf("NCh 1198", "OGUC Art. 4.1.10 Zona 1 a 7"),
            documentoPdfNombre = "FT_TEC_001_Cercha_Ventilada.pdf",
            esDestacado = false
        ),
        FichaTecnica(
            id = "ft_007",
            codigo = "FT-REV-001",
            titulo = "Fachada Ventilada Encapado Madera Termotratada",
            categoria = "Revestimientos",
            sistemaConstructivo = "Poste y Viga",
            material = "Pino Radiata Termotratado",
            aplicacion = "Muro Exterior",
            descripcion = "Solución de revestimiento exterior tipo fachada ventilada con perfiles de Pino Radiata termotratado (ThermoWood) de alta durabilidad biológica y estabilidad dimensional.",
            resistenciaFuego = "F-15",
            aislacionTermica = "U = 0.50 W/m²K (Cámara de aire ventilada 20mm)",
            aislacionAcustica = "STC 40 dB",
            dimensionesEstandard = "Perfil 1x4\" a traslape o tinglado",
            normativaRelacionada = listOf("NCh 789/1 (Durabilidad)", "OGUC Art. 4.1.10"),
            documentoPdfNombre = "FT_REV_001_Fachada_Termotratada.pdf",
            esDestacado = false
        ),
        FichaTecnica(
            id = "ft_008",
            codigo = "FT-CLT-002",
            titulo = "Muro Interior CLT 3 Capas (60mm) Divisorio",
            categoria = "Cerramientos",
            sistemaConstructivo = "CLT (Madera Contralaminada)",
            material = "CLT 3 Capas",
            aplicacion = "Tabique Interior",
            descripcion = "Tabique divisorio interior autosoportante de CLT 3 capas (60mm) con caras cepilladas calidad vista para terminación natural en madera.",
            resistenciaFuego = "F-30",
            aislacionTermica = "U = 0.55 W/m²K",
            aislacionAcustica = "STC 45 dB",
            dimensionesEstandard = "Espesor 60mm (20-20-20mm), Altura 2.60m a 3.00m",
            normativaRelacionada = listOf("PR NCh 3600", "NCh 1198"),
            documentoPdfNombre = "FT_CLT_002_Muro_3Capas.pdf",
            esDestacado = false
        )
    )

    val normativas = listOf(
        NormativaItem(
            id = "norm_1",
            codigo = "NCh 1198:2014",
            titulo = "Madera Estructural - Cálculo y Métodos de Diseño",
            descripcion = "Establece los métodos de cálculo y tensiones admisibles para el diseño de estructuras de madera aserrada y laminada.",
            categoria = "Diseño Estructural",
            enlaceDocumento = "NCh1198_2014_Resumen.pdf"
        ),
        NormativaItem(
            id = "norm_2",
            codigo = "OGUC Art. 4.1.10",
            titulo = "Ordenanza General de Urbanismo y Construcciones - Acondicionamiento Térmico",
            descripcion = "Regula las exigencias de transmitancia térmica (U) para techos, muros y pisos en las 7 zonas térmicas de Chile.",
            categoria = "Acondicionamiento Térmico",
            enlaceDocumento = "OGUC_Art_4_1_10.pdf"
        ),
        NormativaItem(
            id = "norm_3",
            codigo = "NCh 935/1",
            titulo = "Ensaye de Resistencia al Fuego - Elementos de Construcción General",
            descripcion = "Define las metodologías para clasificación de resistencia al fuego F-15, F-30, F-60, F-90 y F-120 en soluciones constructivas.",
            categoria = "Seguridad contra Incendios",
            enlaceDocumento = "NCh935_1_Fuego.pdf"
        ),
        NormativaItem(
            id = "norm_4",
            codigo = "NCh 352",
            titulo = "Aislamiento Acústico - Exigencias para Muros y Entrepisos Divisorios",
            descripcion = "Establece valores mínimos de índice de reducción acústica STC para muros medianeros y entrepisos.",
            categoria = "Aislamiento Acústico",
            enlaceDocumento = "NCh352_Acustica.pdf"
        )
    )

    val herramientas = listOf(
        HerramientaCalculo(
            id = "herr_1",
            nombre = "Calculadora Transmitancia Térmica (U)",
            descripcion = "Herramienta interactiva para calcular el valor U global de soluciones compuestas de madera según zonificación OGUC.",
            tipo = "Acondicionamiento Térmico"
        ),
        HerramientaCalculo(
            id = "herr_2",
            nombre = "Predimensionador de Vigas MLE",
            descripcion = "Estima la escuadría requerida para vigas de madera laminada según luz libre y sobrecarga de diseño.",
            tipo = "Estructural"
        ),
        HerramientaCalculo(
            id = "herr_3",
            nombre = "Verificador de Carbonatación y Fuego",
            descripcion = "Calcula la velocidad de carbonatación nominal y sección residual de madera masiva (CLT/MLE) expuesta a fuego.",
            tipo = "Seguridad contra Fuego"
        )
    )
}

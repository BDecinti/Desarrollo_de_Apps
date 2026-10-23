package com.example.myapplication.data.repository

import com.example.myapplication.data.models.CategoriaItem
import com.example.myapplication.data.models.ConstruccionBasica
import com.example.myapplication.data.models.FichaTecnica
import com.example.myapplication.data.models.HerramientaCalculo
import com.example.myapplication.data.models.NormativaItem
import com.example.myapplication.data.models.RecomendacionMadera
import com.example.myapplication.data.models.SugerenciaPersonalizada
import com.example.myapplication.data.models.Usuario

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
        "Pino Radiata Termotratado",
        "Lenga (Nativa)",
        "Roble",
        "Cedro / Teca",
        "Pino Oregón",
        "Raulí / Coigüe",
        "Terciado Enchapado / MDF"
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

    val construccionesBasicas = listOf(
        ConstruccionBasica(
            id = "const_001",
            titulo = "Cómodas y Cajoneras de Dormitorio",
            categoria = "Almacenamiento",
            descripcion = "Muebles de guardado con múltiples cajones y correderas. Requieren madera muy estable que no se deforme ni dilate para evitar el atascamiento de las cajoneras.",
            dificultadEjecucion = "Intermedio",
            consejosConstruccion = listOf(
                "Usar correderas telescópicas para proteger las guías de madera y garantizar un deslizamiento suave.",
                "Utilizar ensamble de corte a 45° o cola de milano en frentes de cajón para máxima durabilidad.",
                "Aplicar sellador de poros en el interior de los cajones para no manchar ni enganchar telas finas."
            ),
            recomendacionesMadera = listOf(
                RecomendacionMadera(
                    nombreMadera = "Lenga (Nativa)",
                    categoriaClima = "Interior y Templado",
                    categoriaUtilidad = "Estética y Elegancia",
                    porqueRecomendada = "Madera noble patagónica de grano uniforme y tonalidad rosada fina. Excelente maquinado para molduras y frentes de cajón de alto valor estético.",
                    caracteristicasClave = listOf("Textura fina y homogénea", "Excelente retención de lustres y barnices", "Baja tendencia a agrietarse")
                ),
                RecomendacionMadera(
                    nombreMadera = "Roble",
                    categoriaClima = "Cálido y Seco",
                    categoriaUtilidad = "Alta Resistencia",
                    porqueRecomendada = "Ofrece alta resistencia a golpes en frentes y cubierta superior. Ideal para climas secos donde maderas blandas se resecan o rayan con facilidad.",
                    caracteristicasClave = listOf("Gran dureza superficial Janka", "Soporta alto impacto y peso", "Veta marcada con personalidad")
                ),
                RecomendacionMadera(
                    nombreMadera = "Terciado Enchapado / MDF",
                    categoriaClima = "Interior y Templado",
                    categoriaUtilidad = "Funcional y Económico",
                    porqueRecomendada = "Perfecto para el cuerpo interno, fondos de cajón y divisiones interiores. Evita deformaciones por humedad y optimiza el costo del mueble.",
                    caracteristicasClave = listOf("Estabilidad dimensional total", "Económico y fácil de cortar", "Superficie lista para pintar o lacar")
                )
            )
        ),
        ConstruccionBasica(
            id = "const_002",
            titulo = "Mesas de Comedor y Escritorios",
            categoria = "Muebles de Interior",
            descripcion = "Cubiertas de superficie amplia expuestas a derrames, calor puntual de platos, rayones y cargas de uso diario intensivo.",
            dificultadEjecucion = "Intermedio",
            consejosConstruccion = listOf(
                "Utilizar unión por machihembrado o entallado con lengüeta en la cubierta para evitar separaciones.",
                "Aplicar barniz poliuretánico o aceite duro para impermeabilizar contra líquidos y manchas.",
                "Dejar margen de dilatación en las fijaciones de la base hacia la cubierta."
            ),
            recomendacionesMadera = listOf(
                RecomendacionMadera(
                    nombreMadera = "Raulí / Coigüe",
                    categoriaClima = "Interior y Templado",
                    categoriaUtilidad = "Estética y Elegancia",
                    porqueRecomendada = "Maderas nativas de belleza superior con vetas rojizas únicas. Destacan en comedores principales y escritorios de alto perfil.",
                    caracteristicasClave = listOf("Veta rojiza cálida y exclusiva", "Superficie sedosa al tacto", "Excelente presencia decorativa")
                ),
                RecomendacionMadera(
                    nombreMadera = "Roble",
                    categoriaClima = "Cálido y Seco",
                    categoriaUtilidad = "Alta Resistencia",
                    porqueRecomendada = "Resiste rasguños, vajilla caliente y uso familiar continuo. Su bajo nivel de retracción evita rajaduras en ambientes con calefacción.",
                    caracteristicasClave = listOf("Soporta uso rudo e impactos", "Resistente a la fricción continuada", "Excelente estabilidad térmica")
                ),
                RecomendacionMadera(
                    nombreMadera = "Pino Radiata Seleccionado",
                    categoriaClima = "Interior y Templado",
                    categoriaUtilidad = "Funcional y Económico",
                    porqueRecomendada = "Opción accesible para escritorios juveniles y mesas rústicas. Muy fácil de teñir para imitar tonalidades más oscuras.",
                    caracteristicasClave = listOf("Muy económico y disponible", "Ligero y fácil de maquinar", "Apto para tintes, tinturas y pátinas")
                )
            )
        ),
        ConstruccionBasica(
            id = "const_003",
            titulo = "Muebles de Estantería y Centros de TV",
            categoria = "Almacenamiento",
            descripcion = "Muebles modulares y repisas diseñados para soportar libros, televisores y decoración pesada sin deformarse (panquearse) en el centro.",
            dificultadEjecucion = "Fácil",
            consejosConstruccion = listOf(
                "Reforzar luces libres mayores a 80cm con un soporte posterior o listón inferior de refuerzo.",
                "Fijar la estructura a la pared mediante escuadras ocultas para seguridad en zonas sísmicas.",
                "Disimular pasacables en el panel posterior para una estética limpia."
            ),
            recomendacionesMadera = listOf(
                RecomendacionMadera(
                    nombreMadera = "Pino Oregón",
                    categoriaClima = "Interior y Templado",
                    categoriaUtilidad = "Alta Resistencia",
                    porqueRecomendada = "Posee una rigidez estructural superior al pino común. Soporta grandes pesos en tramos largos sin flexión visible.",
                    caracteristicasClave = listOf("Alta rigidez flexional", "Veta contrastada muy atractiva", "Resistente a la curvatura por peso")
                ),
                RecomendacionMadera(
                    nombreMadera = "Lenga (Nativa)",
                    categoriaClima = "Interior y Templado",
                    categoriaUtilidad = "Estética y Elegancia",
                    porqueRecomendada = "Permite estantes limpios y sofisticados con bordes redondeados y terminaciones estilo nórdico de tendencia.",
                    caracteristicasClave = listOf("Acabado mate y uniforme", "Fácil de cepillar y moldear", "Aspecto moderno y acogedor")
                ),
                RecomendacionMadera(
                    nombreMadera = "Terciado Enchapado / MDF",
                    categoriaClima = "Interior y Templado",
                    categoriaUtilidad = "Funcional y Económico",
                    porqueRecomendada = "Ideal para muebles modulares de televisión y centros de entretención. Permite paneles continuos de gran tamaño.",
                    caracteristicasClave = listOf("Placas de formato amplio", "Costo altamente competitivo", "Superficie lisa óptima para lacado")
                )
            )
        ),
        ConstruccionBasica(
            id = "const_004",
            titulo = "Armarios y Closets Empotrados",
            categoria = "Almacenamiento",
            descripcion = "Sistemas de almacenamiento a medida para vestidores y dormitorios. Requieren puertas ligeras pero rígidas que mantengan la planeidad.",
            dificultadEjecucion = "Intermedio",
            consejosConstruccion = listOf(
                "Usar bisagras de cazoleta regulables en 3 ejes para alinear puertas perfectamente.",
                "Asegurar rendijas de ventilación posterior para prevenir humedad acumulada en ropa.",
                "Nivelar minuciosamente los rieles de las puertas correderas."
            ),
            recomendacionesMadera = listOf(
                RecomendacionMadera(
                    nombreMadera = "Terciado Enchapado / MDF",
                    categoriaClima = "Interior y Templado",
                    categoriaUtilidad = "Funcional y Económico",
                    porqueRecomendada = "Es el estándar para cuerpos de closet y repisas. Ligero, plano e inalterable ante variaciones menores de humedad.",
                    caracteristicasClave = listOf("Sin arqueamiento en puertas altas", "Formatos estandarizados", "Limpieza y mantenimiento fácil")
                ),
                RecomendacionMadera(
                    nombreMadera = "Cedro / Teca",
                    categoriaClima = "Cálido y Seco",
                    categoriaUtilidad = "Estética y Elegancia",
                    porqueRecomendada = "El cedro posee aroma natural ahuyentador de polillas y excelente estabilidad dimensional en climas secos y calurosos.",
                    caracteristicasClave = listOf("Aroma natural anti-polilla", "Gran resistencia a bichos", "No se contrae por calor seco")
                ),
                RecomendacionMadera(
                    nombreMadera = "Pino Radiata Impregnado",
                    categoriaClima = "Interior y Templado",
                    categoriaUtilidad = "Funcional y Económico",
                    porqueRecomendada = "Excelente para listonados de bastidores, marcos interiores de soporte y repisas secundarias.",
                    caracteristicasClave = listOf("Económico y fácil de conseguir", "Fácil encolado y clavado", "Buena resistencia general")
                )
            )
        ),
        ConstruccionBasica(
            id = "const_005",
            titulo = "Pérgolas y Muebles para Terraza Exterior",
            categoria = "Muebles de Exterior",
            descripcion = "Muebles de terraza, bancas, poltronas y pergolados expuestos directamente al sol radiante, lluvia y variaciones térmicas drásticas.",
            dificultadEjecucion = "Avanzado",
            consejosConstruccion = listOf(
                "Utilizar tornillería de acero inoxidable o galvanizado en caliente para evitar manchas de óxido.",
                "Dejar separaciones de 5mm a 8mm entre listones para drenar agua de lluvia rápidamente.",
                "Reaplicar un protector UV o aceite de teca anualmente para mantener el color activo."
            ),
            recomendacionesMadera = listOf(
                RecomendacionMadera(
                    nombreMadera = "Cedro / Teca",
                    categoriaClima = "Húmedo y Exterior",
                    categoriaUtilidad = "Alta Resistencia",
                    porqueRecomendada = "Posee aceites y resinas naturales hidrorepelentes que impiden la pudrición, ataques de hongos e hinchazón bajo lluvias intensas.",
                    caracteristicasClave = listOf("Resistencia natural a la intemperie", "No requiere químicos pesados", "Máxima durabilidad biológica")
                ),
                RecomendacionMadera(
                    nombreMadera = "Pino Radiata Termotratado",
                    categoriaClima = "Cálido y Seco",
                    categoriaUtilidad = "Estética y Elegancia",
                    porqueRecomendada = "Tratado térmicamente a más de 200°C. No se tuerce con el sol abrasador ni se raja en veranos de extremo calor seco.",
                    caracteristicasClave = listOf("Estabilidad dimensional extrema", "Tono tostado elegante prémium", "Cero ataque de hongos")
                ),
                RecomendacionMadera(
                    nombreMadera = "Pino Radiata Impregnado",
                    categoriaClima = "Húmedo y Exterior",
                    categoriaUtilidad = "Funcional y Económico",
                    porqueRecomendada = "Tratamiento a presión que protege contra pudrición por agua. Opción muy económica para estructuras de pérgola y bancas de patio.",
                    caracteristicasClave = listOf("Protegido contra humedad e insectos", "Bajo costo para grandes estructuras", "Apto para teñir con cetol")
                )
            )
        ),
        ConstruccionBasica(
            id = "const_006",
            titulo = "Respaldos y Estructuras de Cama",
            categoria = "Muebles de Interior",
            descripcion = "Bases de cama, somieres y respaldos decorativos. Deben ofrecer soporte firme, silenciar crujidos y ser agradables al tacto en el dormitorio.",
            dificultadEjecucion = "Fácil",
            consejosConstruccion = listOf(
                "Colocar felpas o cintas amortiguadoras en las uniones para evitar crujidos nocturnos.",
                "Utilizar largueros de al menos 1.5 pulgadas de espesor para evitar pandeo estructural.",
                "Redondear cantos y esquinas expuestas para evitar golpes accidentales."
            ),
            recomendacionesMadera = listOf(
                RecomendacionMadera(
                    nombreMadera = "Lenga (Nativa)",
                    categoriaClima = "Interior y Templado",
                    categoriaUtilidad = "Estética y Elegancia",
                    porqueRecomendada = "Brinda una estética cálida y nórdica en respaldos con acabado al aceite natural o barniz satinado.",
                    caracteristicasClave = listOf("Textura suave al tacto", "Sin astillas filosas", "Tono neutro elegante")
                ),
                RecomendacionMadera(
                    nombreMadera = "Roble",
                    categoriaClima = "Cálido y Seco",
                    categoriaUtilidad = "Alta Resistencia",
                    porqueRecomendada = "Estructura indestructible para bastidores y patas de cama. Soporta cargas pesadas sin soltarse en los ensambles.",
                    caracteristicasClave = listOf("Anclajes extremadamente firmes", "Dureza de larga duración", "Soporta variaciones de temperatura")
                ),
                RecomendacionMadera(
                    nombreMadera = "Pino Radiata Seleccionado",
                    categoriaClima = "Interior y Templado",
                    categoriaUtilidad = "Funcional y Económico",
                    porqueRecomendada = "Ideal para el emparrillado de somier y estructuras internas que van tapizadas con tela o cuero.",
                    caracteristicasClave = listOf("Ligero para transportar y mover", "Económico para repuestos", "Buena elasticidad para descanso")
                )
            )
        )
    )

    val usuariosDemo = listOf(
        Usuario(
            id = "usr_001",
            username = "carpintero",
            password = "maderas123",
            nombreCompleto = "Carlos Silva",
            perfilOcupacion = "Carpintero Mueblista & Diseñador de Interiores",
            avatarIniciales = "CS",
            historialBusquedas = listOf("Cómodas", "Madera Lenga", "Uniones a 45°", "Muebles de Guardado", "Barniz Poliuretano"),
            sugerenciasPersonalizadas = listOf(
                SugerenciaPersonalizada(
                    id = "sug_101",
                    titulo = "Fabricación de Cómoda Premium en Lenga",
                    descripcion = "Estructura de cajones con guías telescópicas y frentes de Lenga Patagónica con acabado satinado.",
                    motivo = "Basado en tus búsquedas recientes de 'Cómodas' y 'Lenga'",
                    tipo = "Mueble",
                    badgeTexto = "Mueblería Fina"
                ),
                SugerenciaPersonalizada(
                    id = "sug_102",
                    titulo = "Escritorio Ejecutivo en Raulí / Coigüe",
                    descripcion = "Cubiertas de vetas rojizas únicas con encuadre en madera maciza nativa.",
                    motivo = "Basado en tu interés en 'Muebles de Guardado' y maderas nativas",
                    tipo = "Mueble",
                    badgeTexto = "Carpintería Fina"
                ),
                SugerenciaPersonalizada(
                    id = "sug_103",
                    titulo = "Guía de Ensambles en Cola de Milano",
                    descripcion = "Técnicas avanzadas de ebanistería para prolongar la durabilidad de cajoneras sin tornillos expuestos.",
                    motivo = "Basado en tus búsquedas de 'Uniones a 45°'",
                    tipo = "Técnica",
                    badgeTexto = "Técnica de Armado"
                )
            )
        ),
        Usuario(
            id = "usr_002",
            username = "arquitecto",
            password = "diseno2026",
            nombreCompleto = "Valentina Morales",
            perfilOcupacion = "Arquitecta de Proyectos Bioclimáticos y Madera Masiva",
            avatarIniciales = "VM",
            historialBusquedas = listOf("CLT 5 Capas", "Eficiencia Térmica OGUC", "Fachada Ventilada", "Aislamiento Acústico STC 50"),
            sugerenciasPersonalizadas = listOf(
                SugerenciaPersonalizada(
                    id = "sug_201",
                    titulo = "Panel Muro CLT 5 Capas (100mm)",
                    descripcion = "Panel masivo prefabricado CNC con resistencia sísmica y aislamiento STC 50 dB.",
                    motivo = "Basado en tu búsqueda de 'CLT 5 Capas'",
                    tipo = "Ficha Técnica",
                    badgeTexto = "Estructuras Masivas"
                ),
                SugerenciaPersonalizada(
                    id = "sug_202",
                    titulo = "Fachada Ventilada ThermoWood",
                    descripcion = "Revestimiento exterior bioclimático en Pino Termotratado con alta durabilidad sin químicos.",
                    motivo = "Basado en tu búsqueda de 'Fachada Ventilada'",
                    tipo = "Ficha Técnica",
                    badgeTexto = "Sustentabilidad"
                ),
                SugerenciaPersonalizada(
                    id = "sug_203",
                    titulo = "Exigencias OGUC Art. 4.1.10 Térmico",
                    descripcion = "Resumen normativo de valores U límite en las 7 zonas térmicas de Chile.",
                    motivo = "Basado en tus búsquedas de 'Eficiencia Térmica'",
                    tipo = "Normativa",
                    badgeTexto = "Regulación Chilena"
                )
            )
        ),
        Usuario(
            id = "usr_003",
            username = "builder",
            password = "patio2026",
            nombreCompleto = "Mateo Fernández",
            perfilOcupacion = "Constructor de Terrazas, Pérgolas y Obras Exterior",
            avatarIniciales = "MF",
            historialBusquedas = listOf("Pérgolas exterior", "Cedro y Teca", "Pino Impregnado CCA", "Decks e Intemperie"),
            sugerenciasPersonalizadas = listOf(
                SugerenciaPersonalizada(
                    id = "sug_301",
                    titulo = "Pérgolas y Muebles de Terraza en Cedro/Teca",
                    descripcion = "Resinas naturales hidrorepelentes que soportan lluvia intensa y sol directo sin podrírse.",
                    motivo = "Basado en tus búsquedas de 'Pérgolas' y 'Cedro Teca'",
                    tipo = "Mueble Exterior",
                    badgeTexto = "Intemperie"
                ),
                SugerenciaPersonalizada(
                    id = "sug_302",
                    titulo = "Muro Perimetral Marco Plataforma 2x4\" Impregnado",
                    descripcion = "Estructura tratada contra humedad biológica y termitas con barrera anticondensación.",
                    motivo = "Basado en tu búsqueda de 'Pino Impregnado CCA'",
                    tipo = "Ficha Técnica",
                    badgeTexto = "Construcción"
                ),
                SugerenciaPersonalizada(
                    id = "sug_303",
                    titulo = "Pino Termotratado para Decks e Intemperie",
                    descripcion = "Estabilidad dimensional extrema en veranos calurosos de sol abrasador.",
                    motivo = "Basado en tu búsqueda de 'Decks e Intemperie'",
                    tipo = "Material",
                    badgeTexto = "Tecnología Wood"
                )
            )
        )
    )
}

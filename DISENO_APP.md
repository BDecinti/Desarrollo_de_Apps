# Diseña Madera Móvil · Documento de diseño del MVP

Proyecto académico · DSY1105 Desarrollo de Aplicaciones Móviles · Duoc UC (CITT, Sede San Joaquín)
Caso: **Búsqueda móvil de soluciones constructivas en madera** · Organización: Diseña Madera · CIM UC

> Este documento define el alcance, las pantallas, las funcionalidades y los flujos del MVP.
> Todo lo marcado como **Fase 2** queda fuera de la primera implementación.

---

## 1. Descripción de la aplicación

Aplicación móvil **Android** que permite a profesionales y estudiantes del ámbito de la construcción en madera **buscar, filtrar y consultar soluciones constructivas** desde el teléfono, y descubrir la **normativa, documentos y herramientas** relacionadas con cada solución.

Es un **MVP académico**: demuestra un flujo principal completo con una **muestra acotada de datos ficticios o sintéticos** (entre 12 y 20 soluciones). No es una app productiva, no se publica en tiendas y no usa información confidencial de CIM UC ni de Diseña Madera.

### Problema que resuelve
El repositorio real tiene unas 300 fichas técnicas con muchos atributos. Sin una buena organización, encontrar la solución adecuada para una necesidad concreta es lento. La app ofrece una experiencia móvil rápida, clara y orientada al usuario.

### Perfiles de usuario (los que se priorizan en el MVP)
| Perfil | Qué necesita hacer |
|---|---|
| Arquitecto / ingeniero / constructor | Encontrar rápido una solución según sistema, material y aplicación |
| Estudiante / docente | Explorar soluciones y entender sus antecedentes técnicos |

Calculistas, empresas proveedoras y otros perfiles quedan para una fase posterior.

---

## Mapa de cumplimiento de la rúbrica

| Criterio | Peso | Cómo se cumple en el MVP |
|---|---|---|
| Interfaz visual | 15 % | Tema Material 3 con paleta de madera, tarjetas, chips e íconos consistentes (ver F14) |
| Validaciones de campos | 15 % | Formulario de consulta técnica con errores en tiempo real (ver F11) |
| Lógica de validación con MVVM | 10 % | Reglas en `Validators`, errores por campo en el `UiState`, todo orquestado por el ViewModel |
| Animaciones funcionales | 10 % | Corazón de favorito animado y filtros con `AnimatedVisibility` (ver F13) |
| Estructura modular y persistencia local | 15 % | Paquetes por capa y por funcionalidad + Room (catálogo, favoritos, historial, consultas) |
| Herramientas colaborativas (Git/Trello) | 20 % | Flujo de ramas, PR, commits por integrante y tablero Trello (ver sección 12) |
| Recursos nativos | 15 % | Cámara para adjuntar una foto a la consulta técnica (ver F12) |

> Prioridad de trabajo: primero que cada fila de esta tabla quede cumplida y demostrable; lo demás es secundario.

---

## 2. Alcance

### 2.1 Incluido en la primera implementación (MVP / Fase 1)
1. Catálogo sintético de soluciones, normativa y recursos, cargado desde un JSON local a una base de datos Room.
2. Exploración del catálogo (lista de soluciones con tarjetas).
3. Búsqueda por texto.
4. Filtros combinables: sistema constructivo, material, área temática, aplicación.
5. Ficha de detalle de una solución.
6. Navegación a normativa, documentos y herramientas relacionadas, y entre soluciones relacionadas.
7. Favoritos (persistidos localmente).
8. Historial básico de soluciones consultadas.
9. **Formulario de consulta técnica con validaciones de campos** (nombre, correo, teléfono, perfil, mensaje, términos), con errores en tiempo real y lógica en el ViewModel.
10. **Recurso nativo: cámara** para adjuntar una foto a la consulta técnica (con manejo de permiso denegado).
11. **Dos animaciones funcionales:** corazón de favorito animado y filtros/chips con `AnimatedVisibility`.
12. Manejo de estados (cargando, vacío, error) y mensajes claros al usuario.
13. Arquitectura MVVM modular, pruebas unitarias de las validaciones y documentación.
14. Trabajo colaborativo con Git (ramas, PR, commits por integrante) y tablero Trello.

### 2.2 Fuera de la primera implementación (Fase 2, solo mencionado)
- Consumo de una **API de prueba** con Retrofit (mock o JSON público). *La pauta no lo exige; queda para después.*
- **Comparador** de 2 o 3 soluciones lado a lado.
- **Asistente de recomendación simulado** (formulario de criterios que devuelve soluciones sugeridas con reglas simples).
- **Simulación de e-token / firma electrónica avanzada** (pantalla que simula la descarga protegida de un documento).
- Mapas interactivos, visores BIM, sistemas de cálculo (representados con enlaces o pantallas mock).
- Descarga real de documentos y modo offline completo.
- Recursos nativos adicionales (compartir ficha, GPS, escaneo QR, vibración). *Con un recurso nativo basta; la cámara ya está en Fase 1.*
- Modo oscuro refinado, internacionalización, accesibilidad avanzada.
- Login y perfiles de usuario (no son necesarios para el caso).

---

## 3. Stack técnico propuesto

| Aspecto | Decisión |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | MVVM (View · ViewModel · Model/Repository) |
| Navegación | Navigation Compose |
| Estado | `StateFlow` + `collectAsStateWithLifecycle` |
| Persistencia | Room (catálogo, favoritos, historial) |
| Carga inicial de datos | JSON en `assets/` leído con kotlinx.serialization (o Gson/Moshi) |
| Inyección de dependencias | Manual (contenedor simple) en Fase 1; Hilt opcional después |
| Imágenes | Coil (con imágenes locales o URLs públicas) |
| Pruebas | JUnit + `kotlinx-coroutines-test` (prioridad: `Validators` y ViewModels) |
| Validaciones | Clase `Validators` en Kotlin puro, usada por el ViewModel del formulario |
| Animaciones | `animateFloatAsState`, `animateColorAsState` y `AnimatedVisibility` |
| Recurso nativo | Cámara con `ActivityResultContracts.TakePicture` + `FileProvider` (alternativa: CameraX con permiso en tiempo de ejecución) |
| Min SDK | 26 (ajustar si el curso indica otro) |

---

## 4. Modelo de datos

### Entidades principales
- **Solucion**: `id`, `nombre`, `descripcion`, `sistemaConstructivo`, `material`, `areaTematica`, `aplicacion`, `imagen`, `destacada`
- **Normativa**: `id`, `codigo` (ficticio, p. ej. `NORMA-DEMO-001`), `titulo`, `descripcion`, `url`
- **Recurso**: `id`, `titulo`, `tipo` (`DOCUMENTO` | `HERRAMIENTA`), `descripcion`, `url` (ficticia o pública)

### Relaciones (tablas cruzadas)
- `SolucionNormativaCrossRef(solucionId, normativaId)`
- `SolucionRecursoCrossRef(solucionId, recursoId)`
- `SolucionRelacionadaCrossRef(solucionId, relacionadaId)`

### Datos locales del usuario
- **Favorito**: `solucionId`, `fecha`
- **HistorialConsulta**: `id`, `solucionId`, `fecha`
- **ConsultaTecnica**: `id`, `nombre`, `correo`, `telefono?`, `perfil`, `solucionId?`, `mensaje`, `fotoUri?`, `fecha` (el "envío" es simulado: se guarda localmente)

### Valores de ejemplo para los filtros
- Sistema constructivo: Entramado ligero, Madera laminada (MLE), Madera contralaminada (CLT), Poste y viga
- Material: Pino radiata, Madera laminada encolada, CLT, Tablero OSB
- Área temática: Estructura, Envolvente térmica, Acústica, Fuego, Humedad
- Aplicación: Muro, Entrepiso, Cubierta, Fachada

> Todo el contenido (nombres, descripciones, normas, documentos) debe ser **ficticio, sintético o público**.

---

## 5. Pantallas

| # | Pantalla | Fase | Propósito |
|---|---|---|---|
| 1 | **Splash / Bienvenida** | 1 | Presentar la app y precargar el catálogo en Room |
| 2 | **Inicio / Explorar** | 1 | Barra de búsqueda, accesos por categoría, soluciones destacadas |
| 3 | **Resultados** | 1 | Lista de soluciones según texto y filtros, con chips de filtros activos |
| 4 | **Filtros** (bottom sheet) | 1 | Seleccionar sistema, material, área y aplicación; limpiar y aplicar |
| 5 | **Detalle de solución** | 1 | Ficha con antecedentes técnicos; secciones Resumen, Técnica y Relacionados; botón de favorito |
| 6 | **Detalle de normativa** | 1 | Código, título, descripción y soluciones que la usan |
| 7 | **Detalle de recurso / herramienta** | 1 | Descripción y acción de abrir enlace (simulado o público) |
| 8 | **Favoritos** | 1 | Lista de soluciones guardadas, con opción de quitar |
| 9 | **Historial** | 1 | Últimas fichas consultadas, con opción de limpiar |
| 10 | **Acerca de** | 1 | Descripción del proyecto, aviso de datos ficticios y créditos |
| 11 | **Consulta técnica** (formulario) | 1 | Formulario con validaciones y foto adjunta con la cámara |
| 12 | **Mis consultas** | 1 | Lista de consultas guardadas con su detalle y foto |
| 13 | Comparador | 2 | Comparar soluciones lado a lado |
| 14 | Asistente de recomendación | 2 | Formulario de criterios y resultados sugeridos (simulado) |
| 15 | Simulación e-token / firma | 2 | Representar el acceso a un documento protegido |

### Navegación principal
Barra inferior con 4 destinos: **Explorar · Favoritos · Historial · Consulta**. *Acerca de* se abre desde el menú de la barra superior.
Las pantallas de resultados, detalle de solución, normativa y recurso se abren como destinos secundarios dentro del grafo de navegación.

---

## 6. Funcionalidades (detalle y criterios de aceptación)

### F1. Carga del catálogo
- Al primer inicio, el catálogo se lee desde `assets/catalogo.json` y se inserta en Room.
- **Aceptación:** al reabrir la app los datos persisten y no se duplican.

### F2. Exploración
- Lista de soluciones en tarjetas (imagen, nombre, sistema, material).
- **Aceptación:** al menos 12 soluciones visibles con scroll fluido.

### F3. Búsqueda por texto
- Busca por nombre y descripción, sin distinguir mayúsculas ni tildes, con pequeño retardo (debounce).
- **Aceptación:** escribir "muro" muestra solo soluciones relacionadas; si no hay resultados se muestra un estado vacío con sugerencia.

### F4. Filtros combinables
- Selección múltiple por categoría; los filtros se combinan con la búsqueda por texto.
- **Aceptación:** los chips activos se ven en la lista de resultados, se pueden quitar uno a uno y existe "Limpiar filtros".

### F5. Ficha de detalle
- Muestra todos los atributos de la solución y sus relaciones.
- **Aceptación:** desde la ficha se puede llegar a una normativa, un recurso y una solución relacionada.

### F6. Recursos relacionados
- Normativas, documentos y herramientas vinculadas a la solución.
- **Aceptación:** cada elemento abre su pantalla de detalle; los enlaces externos se abren en el navegador o se muestran como simulación.

### F7. Favoritos
- Agregar y quitar desde la ficha o desde la lista; persistidos en Room.
- **Aceptación:** los favoritos se mantienen tras cerrar la app.

### F8. Historial
- Se registra cada ficha abierta (sin duplicados consecutivos).
- **Aceptación:** se listan en orden de más reciente a más antiguo y se pueden limpiar con confirmación.

### F9. Estados y validaciones
- Estados de carga, vacío y error en todas las listas.
- Validación del campo de búsqueda (longitud máxima, caracteres no válidos).
- Confirmación antes de acciones destructivas (limpiar historial o favoritos).

### F10. Experiencia móvil
- Textos legibles, áreas táctiles amplias, contraste adecuado y soporte para rotación sin perder el estado.

### F11. Formulario de consulta técnica (validaciones)
- Campos: nombre, correo, teléfono (opcional), perfil profesional (lista desplegable), solución relacionada (precargada si se llega desde una ficha), mensaje y aceptación de términos.
- Reglas: nombre obligatorio (mín. 3 caracteres, solo letras y espacios); correo con formato válido; teléfono opcional con formato chileno (9 dígitos, parte con 9); perfil obligatorio; mensaje de 20 a 500 caracteres con contador; términos obligatorios.
- Los errores se muestran bajo cada campo, al perder el foco y al intentar enviar. El botón Enviar se habilita solo con el formulario válido.
- Las reglas viven en `Validators`; el ViewModel mantiene el `UiState` con los valores, los errores por campo y el estado de envío.
- **Aceptación:** con datos inválidos no se envía y se indica qué corregir; con datos válidos se guarda en Room, se muestra una confirmación y se limpia el formulario.

### F12. Recurso nativo: cámara
- Botón "Adjuntar foto" en el formulario: abre la cámara, guarda la foto en almacenamiento interno y muestra una vista previa con opción de quitarla.
- Si se declara y solicita el permiso `CAMERA`, se maneja el caso de permiso denegado con un mensaje claro.
- **Aceptación:** la foto se ve en el formulario y en el detalle de "Mis consultas".

### F13. Animaciones funcionales
- **Corazón de favorito:** escala y cambio de color al marcar o desmarcar.
- **Filtros y chips:** aparecen y desaparecen con `AnimatedVisibility`.
- **Aceptación:** las animaciones son fluidas y comunican un cambio de estado (no son solo decoración). Con estas dos basta.

### F14. Interfaz visual
- Tema Material 3 con paleta inspirada en la madera (marrones cálidos, verde oscuro de acento), tipografía consistente, tarjetas con imagen, íconos uniformes y espaciados regulares.
- Misma estructura visual en todas las pantallas; áreas táctiles de al menos 48 dp.

---

## 7. Flujos

### Flujo principal (el que se demuestra completo)
```
Inicio → escribir búsqueda / abrir Filtros → Resultados filtrados
      → abrir Ficha de solución → ver Normativa o Recurso relacionado
      → (opcional) marcar Favorito → volver y revisar Favoritos / Historial
```

### Flujos secundarios
1. **Filtrar sin texto:** Inicio → Filtros → seleccionar criterios → Aplicar → Resultados.
2. **Descubrir por relación:** Ficha → Soluciones relacionadas → otra Ficha.
3. **Normativa transversal:** Ficha → Normativa → lista de soluciones que la usan → Ficha.
4. **Gestión de favoritos:** Ficha → corazón → pestaña Favoritos → quitar.
5. **Retomar consulta:** pestaña Historial → tocar una ficha previa.
6. **Sin resultados:** búsqueda sin coincidencias → estado vacío → "Limpiar filtros".
7. **Consulta técnica:** Ficha → "Consultar" (solución precargada) → completar formulario con validaciones → adjuntar foto con la cámara → Enviar → confirmación → Mis consultas.

### Diagrama de navegación
```
Splash → [Explorar] ──► Resultados ──► Detalle Solución ──► Detalle Normativa
            │   ▲                            │ ├────────────► Detalle Recurso
            │   └── Filtros (sheet)          └──► Detalle Solución (relacionada)
            ├── [Favoritos] ──► Detalle Solución
            ├── [Historial] ──► Detalle Solución
            └── [Consulta] ──► Formulario (+ cámara) ──► Mis consultas
(Acerca de: desde el menú de la barra superior)
```

---

## 8. Arquitectura MVVM

```
app/src/main/java/cl/duoc/disenamadera/
├── data/
│   ├── local/          Room: entidades, DAOs, base de datos, seed desde JSON
│   ├── model/          Modelos de dominio y relaciones
│   └── repository/     CatalogoRepository, FavoritosRepository, HistorialRepository, ConsultasRepository
├── domain/
│   └── validation/     Validators.kt (reglas puras y testeables)
├── ui/
│   ├── navigation/     NavGraph y rutas
│   ├── theme/          Colores, tipografía, tema Material 3
│   ├── components/     Tarjetas, chips, estados vacío/error/carga
│   └── screens/
│       ├── explorar/   ExplorarScreen + ExplorarViewModel
│       ├── resultados/ ResultadosScreen + ResultadosViewModel (texto + filtros)
│       ├── detalle/    DetalleSolucionScreen + ViewModel
│       ├── normativa/  DetalleNormativaScreen + ViewModel
│       ├── recurso/    DetalleRecursoScreen + ViewModel
│       ├── favoritos/  FavoritosScreen + ViewModel
│       ├── historial/  HistorialScreen + ViewModel
│       ├── consulta/   ConsultaFormScreen + ConsultaViewModel (validaciones y cámara), MisConsultasScreen
│       └── acerca/     AcercaScreen
└── MainActivity.kt
```

Reglas:
- La **View** (Composable) solo observa un `UiState` y envía eventos.
- El **ViewModel** expone `StateFlow<UiState>` y no conoce la UI.
- El **Repository** es la única puerta de acceso a Room (y a la API en Fase 2).
- Las **validaciones** viven en `Validators` y las invoca el ViewModel; la View solo pinta los errores que vienen en el `UiState`.
- **Modularidad:** paquetes separados por capa (`data`, `domain`, `ui`) y por funcionalidad dentro de `ui/screens`. Consultar con el docente si además se espera dividir en módulos Gradle (`:app`, `:core`, `:feature-*`).

---

## 9. Casos de prueba (para la demostración y la documentación)

| ID | Caso | Resultado esperado |
|---|---|---|
| CP-01 | Abrir la app por primera vez | Se carga el catálogo y se muestra Inicio |
| CP-02 | Buscar "muro" | Solo aparecen soluciones que coinciden |
| CP-03 | Buscar un texto inexistente | Estado vacío con opción de limpiar |
| CP-04 | Aplicar filtro de material + aplicación | Resultados cumplen ambos criterios |
| CP-05 | Quitar un chip de filtro | La lista se actualiza |
| CP-06 | Abrir una ficha | Se muestran todos los atributos y relaciones |
| CP-07 | Navegar de ficha a normativa y volver | Se conserva el estado de la lista |
| CP-08 | Marcar y desmarcar favorito | Aparece y desaparece en Favoritos, y persiste al reabrir |
| CP-09 | Consultar varias fichas | El historial muestra el orden correcto |
| CP-10 | Rotar la pantalla en resultados | Se mantienen texto y filtros |
| CP-11 | Enviar el formulario vacío o con datos inválidos | No se envía y cada campo muestra su error |
| CP-12 | Enviar el formulario con datos válidos | Se guarda, aparece en Mis consultas y el formulario se limpia |
| CP-13 | Adjuntar foto con la cámara (y probar cancelar o denegar permiso) | La foto se previsualiza; la cancelación o el rechazo no rompen la app |
| CP-14 | Marcar favorito y abrir/cerrar filtros | Las dos animaciones se ejecutan de forma fluida |
| CP-15 | Pruebas unitarias de `Validators` y ViewModels | Pasan en verde |

---

## 10. Entregables y documentación

- Repositorio GitHub con README, ramas por funcionalidad, pull requests y commits de todos los integrantes.
- Tablero Trello con tareas, responsables y evidencia de avance.
- Este documento actualizado con el alcance final.
- Catálogo sintético (`catalogo.json`) y nota de que los datos son ficticios.
- Evidencia de casos de prueba (capturas o video del flujo principal).
- Guía breve para continuar el MVP (cómo agregar soluciones, cómo conectar una API).
- APK de demostración (debug o firmado, según indique la rúbrica).

---

## 11. Riesgos y decisiones pendientes

- **Rúbrica:** el alcance ya está alineado con la pauta (ver el mapa al inicio). Confirmar con el docente si la "estructura modular" exige módulos Gradle y si el recurso nativo debe ser alguno en particular.
- **Datos:** acordar con el docente el catálogo sintético oficial; si no llega a tiempo, usar el definido aquí.
- **Alcance:** evitar iniciar Fase 2 antes de tener el flujo principal estable.

---

## 12. Trabajo colaborativo: Git y Trello (20 % de la nota)

### Git
- **Ramas:** `main` (estable, solo entregas), `develop` (integración) y `feature/<nombre>` por funcionalidad (por ejemplo `feature/filtros`, `feature/consulta-form`, `feature/camara`).
- **Commits** pequeños y frecuentes con convención: `feat:`, `fix:`, `docs:`, `refactor:`, `test:` (ejemplo: `feat: valida formato de correo en consulta técnica`).
- **Pull requests** hacia `develop`, con al menos una revisión de otro integrante.
- Todos los integrantes deben tener commits y PR visibles en el historial.
- `.gitignore` de Android, README con instrucciones y tags para hitos (`v0.1`, `v1.0-mvp`).
- Mencionar la tarjeta de Trello en la rama o en el PR (ejemplo: `feature/filtros` ↔ tarjeta "Filtros combinables").

### Trello
- **Columnas:** Backlog · Por hacer · En progreso · En revisión · Hecho.
- **Etiquetas:** Diseño, Validaciones, MVVM, Animación, Recurso nativo, Persistencia, Documentación, Git.
- Una tarjeta por funcionalidad, con responsable, fecha, checklist y criterio de aceptación (usar F1 a F14).
- **Tarjetas iniciales sugeridas:** configurar proyecto y tema · modelos, Room y seed JSON · navegación y Explorar · búsqueda y filtros · detalle de solución · favoritos e historial (con animación) · formulario y `Validators` · cámara · pruebas · README y documentación.
- Mover las tarjetas a medida que avanza el trabajo y compartir el tablero con el docente.

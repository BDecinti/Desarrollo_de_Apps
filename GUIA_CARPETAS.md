# 🗺️ Guía de carpetas del proyecto "Diseña Madera Móvil"

> Pensada para quien construye su **primera app Android**. No necesitas entender todo hoy: úsala como un mapa al que volver cuando te preguntes *"¿y esto para qué sirve?"*.

---

## 🏠 La gran analogía: la app es un restaurante

Antes de entrar en las carpetas, quédate con esta idea. Una app bien organizada funciona como un restaurante:

| En el restaurante | En la app | Carpeta |
|---|---|---|
| 🍽️ El **comedor** (lo que ve el cliente) | La interfaz: pantallas, botones, colores | `ui/` |
| 🧑‍🍳 El **cocinero** (decide qué hacer con el pedido) | El **ViewModel**: lógica de cada pantalla | dentro de `ui/screens/...` |
| 🧑‍💼 El **bodeguero** (sabe dónde está cada ingrediente) | El **Repository**: único que pide datos | `data/repository/` |
| 🥫 La **bodega** (donde se guardan los ingredientes) | La base de datos **Room** | `data/local/` |
| 📖 El **libro de reglas** del local ("no se sirve alcohol a menores") | Las **validaciones** | `domain/validation/` |
| 🏗️ Los **planos y permisos** del local | Archivos de configuración (Gradle, Manifest) | raíz y `app/` |

Esta forma de organizar se llama **MVVM** (Model – View – ViewModel). La regla de oro: **el comedor nunca entra a la bodega directamente**; siempre pasa por el cocinero y el bodeguero.

```
 Usuario toca algo
       │
       ▼
   🍽️ VIEW (pantalla)  ──evento──▶  🧑‍🍳 VIEWMODEL  ──pide──▶  🧑‍💼 REPOSITORY  ──lee/guarda──▶  🥫 ROOM (BD)
       ▲                                  │
       └────────── UiState (datos) ◀──────┘
```

---

## 🌳 Vista general del proyecto

```
Desarrollo_de_Apps/                     ← RAÍZ: todo el proyecto
├── 📄 README.md, DISENO_APP.md, ...    ← documentación
├── ⚙️ build.gradle.kts, settings.gradle.kts, gradle.properties
├── 📁 gradle/                          ← versiones de librerías + "wrapper"
└── 📁 app/                             ← LA APP en sí (aquí vive tu código)
    ├── build.gradle.kts                ← configuración de la app
    └── src/
        ├── main/                       ← el código real de la app
        │   ├── AndroidManifest.xml
        │   ├── assets/
        │   ├── res/
        │   └── java/cl/duoc/disenamadera/
        │       ├── MainActivity.kt
        │       ├── data/
        │       ├── domain/
        │       └── ui/
        └── test/                       ← pruebas automáticas
```

> 💡 **Tip:** las carpetas que hoy solo tienen un archivo `.gitkeep` están **vacías a propósito**. Git no guarda carpetas vacías, así que `.gitkeep` es un "post-it" que dice *"esta carpeta existe, aquí irá algo"*. Son los cuartos del restaurante que ya están construidos pero todavía sin muebles.

---

# 📦 PARTE 1 · La raíz del proyecto

## 📄 Archivos de documentación

| Archivo | Para qué sirve | Asociación |
|---|---|---|
| `README.md` | Portada del repositorio en GitHub: qué es la app y sus características | La **carátula de un libro** |
| `DISENO_APP.md` | Alcance, pantallas, modelo de datos, flujos y casos de prueba del MVP | Los **planos del arquitecto** |
| `PROMPT_INICIAL.md` | Instrucciones para pedirle a un asistente de IA que te ayude a construir la app paso a paso | La **receta** para empezar a cocinar |
| `GUIA_CARPETAS.md` | Este archivo 🙂 | El **mapa del tesoro** |

## ⚙️ Archivos de Gradle (el "sistema de construcción")

**¿Qué es Gradle?** Es la herramienta que **toma tu código y lo convierte en una app instalable (.apk)**. Imagina una **fábrica de armado**: le das las piezas (código, imágenes, librerías) y ella las ensambla.

| Archivo | Qué hace |
|---|---|
| `settings.gradle.kts` | Dice **cómo se llama el proyecto** (`DisenaMaderaMovil`) y **qué módulos incluye** (`:app`). También indica *de dónde descargar* librerías (Google, Maven). Es la **lista de invitados** de la fábrica. |
| `build.gradle.kts` (raíz) | Configuración común a todo el proyecto. Aquí se declaran los *plugins* (herramientas de la fábrica). |
| `gradle.properties` | Ajustes de la fábrica: cuánta memoria usar, opciones de compilación. |
| `gradlew` / `gradlew.bat` | Scripts para ejecutar Gradle **sin instalarlo** (`gradlew` para Mac/Linux, `gradlew.bat` para Windows). Android Studio los usa por detrás. |
| `local.properties` | Guarda la **ruta de tu Android SDK en tu PC**. Es personal de cada computador y **no se sube a Git** (está en `.gitignore`). |
| `.gitignore` | Lista de lo que Git debe **ignorar** (carpetas `build/`, `.idea/`, archivos `.apk`...). Como una lista de "no empacar esto en la maleta". |

## 📁 `gradle/`

| Contenido | Para qué sirve |
|---|---|
| `libs.versions.toml` | **El catálogo de librerías.** Un único lugar donde se escriben las versiones de todo lo que usa la app. ⭐ Es como la **lista de compras del supermercado**: aquí anotas *qué* librerías y *qué versión*; luego `app/build.gradle.kts` las "saca del carrito". |
| `wrapper/` | Guarda qué versión exacta de Gradle usar, para que **todos los del grupo compilen igual** (tú, tu compañero, el profe). |
| `gradle-daemon-jvm.properties` | Configuración del Java con el que corre Gradle (archivo generado automáticamente, aún sin subir a Git). |

### 🔑 Ejemplo: cómo se conecta una librería

```toml
# 1) En gradle/libs.versions.toml: la "anotas" en la lista de compras
coil-compose = { module = "io.coil-kt:coil-compose", version.ref = "coil" }
```
```kotlin
// 2) En app/build.gradle.kts: la "agregas al carrito"
implementation(libs.coil.compose)
```
Y ya puedes usar Coil para mostrar imágenes en tu código.

## 📁 `build/` (carpeta generada)

Es la **basura útil** que genera Gradle al compilar (archivos temporales, el `.apk` armado). **Nunca la edites ni la subas a Git.** Si algo se rompe raro, borrarla y recompilar suele arreglarlo (en Android Studio: *Build → Clean Project*).

---

# 📱 PARTE 2 · La carpeta `app/`

Es **la aplicación propiamente tal**. Un proyecto Android puede tener varios módulos, pero este tiene uno solo, `:app`.

## `app/build.gradle.kts` 🧾

La **ficha técnica de la app**. Lo más importante:

```kotlin
namespace = "cl.duoc.disenamadera"   // el "apellido" único de tu app
applicationId = "cl.duoc.disenamadera"
minSdk = 26      // Android más antiguo que soporta (Android 8.0)
targetSdk = 36   // Android para el que está optimizada
versionName = "0.1"
```

Y la sección `dependencies { ... }` con las **librerías** que usa. Cada una resuelve un problema:

| Librería | Qué hace | Analogía |
|---|---|---|
| **Jetpack Compose** (`compose.ui`, `material3`) | Dibuja la interfaz con código Kotlin | Tus **legos de pantalla** |
| **Navigation Compose** | Moverse entre pantallas | El **mapa del metro** |
| **Room** | Base de datos local en el celular | Tu **bodega** |
| **Coil** | Carga y muestra imágenes | El **marco de fotos** |
| **kotlinx-serialization** | Lee el archivo JSON y lo convierte en objetos | Un **traductor** JSON → Kotlin |
| **Coroutines** | Hacer tareas largas sin congelar la app | Un **mesero extra** para que nadie espere |
| **ViewModel / Lifecycle** | Guardar el estado de la pantalla | La **memoria** del cocinero |
| **JUnit** | Pruebas automáticas | El **inspector de calidad** |

> ⚠️ Cuando cambies este archivo, Android Studio mostrará un aviso **"Sync Now"**. Púlsalo: le avisa a Gradle que revise los cambios.

## `app/proguard-rules.pro`
Reglas para **achicar y ofuscar** el código al publicar la app. Por ahora no hace nada (`isMinifyEnabled = false`). Puedes ignorarlo.

---

# 🧩 PARTE 3 · `app/src/main/` — el corazón

Android separa el proyecto en **conjuntos de código** (*source sets*). `main` es lo que viaja dentro de la app instalada; `test` es solo para tus pruebas.

## 📜 `AndroidManifest.xml` — el "carnet de identidad"

Es lo primero que lee Android sobre tu app. Dice:

- **Cómo se llama** la app y su ícono.
- **Cuál es la pantalla inicial** (`MainActivity`, marcada como LAUNCHER = "la puerta de entrada").
- **Qué permisos** necesita (en etapas futuras: la cámara).

```xml
<activity android:name=".MainActivity" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />   <!-- "ábreme al tocar el ícono" -->
    </intent-filter>
</activity>
```

> 🎯 **Asociación:** es como el **carnet de identidad** + **la lista de permisos** de tu app. Si no declaras algo aquí (una pantalla, un permiso), Android finge que no existe.

## 🗃️ `assets/` — la caja de archivos crudos

Archivos que Android **guarda tal cual, sin tocarlos**. Aquí irá `catalogo.json` con las soluciones constructivas de ejemplo.

```json
{ "id": 1, "nombre": "Muro de entramado ligero", "material": "Pino radiata", ... }
```

Al primer inicio, la app lee ese JSON y lo copia a la base de datos Room (a eso el diseño le llama *seed* = "sembrar" datos iniciales).

> 🎯 **Asociación:** es la **caja de apuntes** que traes en la mochila desde el primer día.

## 🎨 `res/` — los recursos (todo lo que no es código)

| Subcarpeta | Contenido | Ejemplo en tu proyecto |
|---|---|---|
| `values/strings.xml` | **Los textos** de la app | `app_name = "Diseña Madera Móvil"` |
| `values/themes.xml` | Tema base del sistema (barra de estado, etc.) | `Theme.DisenaMadera` |
| `drawable/` | Gráficos vectoriales e imágenes | Fondo y primer plano del ícono |
| `mipmap-anydpi-v26/` | **Íconos de la app** (el que ves en el menú del teléfono) | `ic_launcher.xml` |

💡 **¿Por qué los textos van en `strings.xml` y no escritos en el código?** Para poder **cambiarlos o traducirlos en un solo lugar**. En vez de escribir `"Diseña Madera Móvil"` 10 veces, escribes la referencia `@string/app_name` y listo.

---

# 🧠 PARTE 4 · `java/cl/duoc/disenamadera/` — tu código Kotlin

El nombre raro `cl/duoc/disenamadera` es el **paquete** (la dirección única de tu app, tipo dominio al revés: `duoc.cl` → `cl.duoc`). Dentro hay 3 capas y 1 archivo de entrada.

## 🚪 `MainActivity.kt` — la puerta de entrada

Es **la primera pantalla que Android abre**. En Compose casi todo el trabajo es dibujar con `setContent { ... }`.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {                       // "dibuja esto en pantalla"
            DisenaMaderaTheme {            // aplica los colores de madera
                Text("Diseña Madera Móvil")
            }
        }
    }
}
```

Ahora mismo solo muestra el título (es un *placeholder*). En la etapa 3 aquí se conectará la **navegación** real entre pantallas.

> 🎯 **Asociación:** es la **puerta principal del restaurante**. Todos los clientes entran por aquí, y desde aquí los reparte a las mesas.

---

## 🥫 CAPA `data/` — "la bodega y el bodeguero"

**Todo lo relacionado con datos**: guardarlos, leerlos, definir cómo son.

### `data/model/` — los moldes de los datos
Define **cómo es cada "cosa"** de tu app con `data class` de Kotlin.

```kotlin
data class Solucion(
    val id: Int,
    val nombre: String,
    val material: String,
    val destacada: Boolean
)
```
Según tu diseño, aquí van: **Solucion**, **Normativa**, **Recurso**, **Favorito**, **HistorialConsulta**, **ConsultaTecnica**.

> 🎯 **Asociación:** son los **moldes de galletas**. El molde define la forma; cada galleta es un dato concreto.

### `data/local/` — la base de datos Room
Todo lo de la **base de datos dentro del teléfono**. Room usa 3 piezas:

| Pieza | Qué es | Analogía |
|---|---|---|
| **Entity** | Una tabla | Una hoja de Excel |
| **DAO** | Las consultas (guardar, buscar, borrar) | Las **órdenes** que le das al bodeguero |
| **Database** | Junta todo y abre la BD | La **bodega completa** |

```kotlin
@Dao
interface SolucionDao {
    @Query("SELECT * FROM solucion WHERE nombre LIKE '%' || :texto || '%'")
    fun buscar(texto: String): Flow<List<Solucion>>
}
```
También aquí va el código que **siembra** el catálogo desde `assets/catalogo.json`.

### `data/repository/` — el bodeguero
Los **Repository** son **la única puerta de acceso a los datos**. Los ViewModels nunca hablan con Room; le piden al repositorio.

Según el diseño: `CatalogoRepository`, `FavoritosRepository`, `HistorialRepository`, `ConsultasRepository`.

```kotlin
// El ViewModel dice: "tráeme las soluciones de CLT"
repositorio.buscarSoluciones(texto = "CLT")
// No le importa si vienen de Room, de internet o de un JSON. Eso lo resuelve el repositorio.
```

> 💡 **¿Por qué esta capa intermedia?** Si mañana quieres traer datos de internet (Fase 2, con Retrofit), **solo cambias el repositorio**; las pantallas ni se enteran.

---

## 📖 CAPA `domain/` — "el libro de reglas"

### `domain/validation/`
Aquí vivirá `Validators.kt`: **funciones simples que revisan si un dato es válido**. Están escritas en Kotlin puro (sin Android), lo que las hace **muy fáciles de probar**. Esto vale **15 % + 10 %** de tu nota. ⭐

```kotlin
object Validators {
    fun validarCorreo(correo: String): String? =
        if (correo.isBlank()) "El correo es obligatorio"
        else if (!correo.contains("@")) "Correo no válido"
        else null        // null = "todo bien, sin error"
}
```

> 🎯 **Asociación:** el **guardia de la discoteca** que revisa tu carnet. Solo responde "pasa" o "no pasa, y por esto".

Se usan en el **formulario de consulta técnica** (nombre, correo, teléfono, mensaje, términos).

---

## 🍽️ CAPA `ui/` — "el comedor" (lo que ve el usuario)

Todo lo visual. Tiene 4 subcarpetas.

### `ui/theme/` — el diseño visual (¡ya tiene código!) 🎨

| Archivo | Qué hace |
|---|---|
| `Color.kt` | La **paleta de colores**: marrones de madera y verde bosque |
| `Type.kt` | La **tipografía** (tamaños y estilos de letra) |
| `Theme.kt` | Junta todo en `DisenaMaderaTheme { }`, incluyendo **modo claro y oscuro** |

```kotlin
val MaderaPrimary = Color(0xFF6D4C41)   // marrón madera
val BosqueSecondary = Color(0xFF2E5339) // verde bosque
```
Envolviendo la app en `DisenaMaderaTheme`, **todos los botones, tarjetas y textos toman esos colores automáticamente**.

> 🎯 **Asociación:** es la **decoración del restaurante**: cambias el color de las paredes una vez y se ve en todo el local. Se llama **Material 3**, el sistema de diseño de Google.

### `ui/components/` — las piezas reutilizables 🧱
Componentes que se usan en **varias pantallas**: tarjeta de solución, chip de filtro, mensaje de "lista vacía", indicador de carga, mensaje de error.

```kotlin
@Composable
fun SolucionCard(solucion: Solucion, onClick: () -> Unit) { ... }
// La usas igual en Explorar, Resultados, Favoritos e Historial → se escribe UNA vez
```

> 🎯 **Asociación:** son los **legos**. Los armas una vez y los reutilizas en todos lados.

### `ui/navigation/` — el mapa del metro 🚇
Define **qué pantallas existen y cómo se llega de una a otra** (NavGraph y rutas). Incluye la barra inferior con 4 destinos: **Explorar · Favoritos · Historial · Consulta**.

```kotlin
// Ejemplo de ruta con parámetro:
"detalle/{solucionId}"   →  abre la ficha de la solución número X
```

### `ui/screens/` — las pantallas 📱
Una subcarpeta **por funcionalidad**. Cada una tendrá normalmente **2 archivos**:

- `XxxScreen.kt` → lo que se **ve** (la View)
- `XxxViewModel.kt` → la **lógica** y el estado (el cocinero)

| Carpeta | Qué pantalla es | Qué hace |
|---|---|---|
| `explorar/` | **Inicio** | Barra de búsqueda, categorías y soluciones destacadas. Es la portada. |
| `resultados/` | **Resultados** | Lista filtrada por texto y chips de filtros activos. |
| `detalle/` | **Ficha de la solución** | Todos los antecedentes técnicos + botón ❤️ de favorito. |
| `normativa/` | **Detalle de normativa** | Código, título y soluciones que usan esa norma. |
| `recurso/` | **Detalle de recurso** | Documento o herramienta, con botón para abrir el enlace. |
| `favoritos/` | **Favoritos** | Soluciones que guardaste (con el corazón animado). |
| `historial/` | **Historial** | Últimas fichas que viste, con opción de limpiar. |
| `consulta/` | **Consulta técnica** | Formulario con validaciones + foto con la **cámara** + lista "Mis consultas". |
| `acerca/` | **Acerca de** | Info del proyecto y aviso de que los datos son ficticios. |

#### 🔍 Cómo se ve una pantalla por dentro (MVVM en miniatura)

Ejemplo con `favoritos/`:

```kotlin
// FavoritosViewModel.kt — el COCINERO: guarda el estado
class FavoritosViewModel(private val repo: FavoritosRepository) : ViewModel() {
    val uiState: StateFlow<FavoritosUiState> = ...   // lo que la pantalla debe mostrar
    fun quitarFavorito(id: Int) { /* le pide al repo que lo borre */ }
}

// FavoritosScreen.kt — el COMEDOR: solo dibuja
@Composable
fun FavoritosScreen(viewModel: FavoritosViewModel) {
    val state by viewModel.uiState.collectAsState()
    LazyColumn { items(state.favoritos) { SolucionCard(it) } }   // lista de tarjetas
}
```

**Regla:** la pantalla **no piensa, solo dibuja** lo que el ViewModel le da. Si el estado cambia, Compose **redibuja solo**.

---

# 🧪 PARTE 5 · `app/src/test/` — las pruebas

`app/src/test/java/cl/duoc/disenamadera/` guardará las **pruebas unitarias**: código que **prueba tu código automáticamente**, sin abrir el emulador. Prioridad: probar `Validators` y los ViewModels.

```kotlin
@Test
fun `correo sin arroba es invalido`() {
    assertNotNull(Validators.validarCorreo("pedro.gmail.com"))   // debe dar error
}
```

> 🎯 **Asociación:** el **control de calidad** de una fábrica. Cada vez que cambias algo, corres las pruebas y sabes si rompiste algo en segundos.

En Android Studio: clic derecho sobre la carpeta → *Run Tests*.

---

# 🧭 Resumen visual: ¿dónde va cada cosa?

| Si quiero... | Voy a... |
|---|---|
| Cambiar un color de la app | `ui/theme/Color.kt` |
| Cambiar un texto o el nombre de la app | `res/values/strings.xml` |
| Agregar una librería nueva | `gradle/libs.versions.toml` + `app/build.gradle.kts` |
| Crear una pantalla nueva | `ui/screens/<nombre>/` + registrarla en `ui/navigation/` |
| Crear un botón/tarjeta reutilizable | `ui/components/` |
| Definir un nuevo tipo de dato | `data/model/` |
| Guardar/consultar en la base de datos | `data/local/` (DAO) |
| Que una pantalla obtenga datos | `data/repository/` |
| Validar un campo del formulario | `domain/validation/Validators.kt` |
| Pedir un permiso (cámara) | `AndroidManifest.xml` |
| Agregar los datos iniciales de ejemplo | `assets/catalogo.json` |
| Escribir una prueba | `app/src/test/...` |

---

# 🚀 Estado actual y próximos pasos

**✅ Ya hecho (etapa 1):**
- Proyecto configurado con Compose, Room, Navigation, Coil y demás librerías.
- Tema Material 3 con paleta de madera (claro y oscuro).
- Estructura de carpetas MVVM creada.
- `MainActivity` mostrando un título de prueba.

**⏳ Pendiente (carpetas con `.gitkeep`):**
1. `data/model` + `data/local` + `assets` → modelos, Room y el JSON del catálogo.
2. `ui/navigation` + `ui/screens/explorar` → primera pantalla navegable.
3. Búsqueda, filtros y detalle.
4. Favoritos e historial (con animación del corazón).
5. Formulario con `Validators` y cámara.
6. Pruebas unitarias y documentación.

---

# 🎓 Cuando abras el proyecto en Android Studio

1. **File → Open** y elige la carpeta `Desarrollo_de_Apps`.
2. Espera el **Gradle Sync** (barra inferior; la primera vez demora varios minutos porque descarga librerías).
3. Cambia la vista del panel izquierdo de *Android* a *Project* para ver las carpetas **tal como están en disco** (la vista *Android* las reorganiza y puede confundir al principio).
4. Crea un emulador en **Device Manager** y presiona ▶️ **Run**.

> 💡 **Consejo final:** no intentes memorizar todo. Aprende la regla de oro y lo demás se vuelve intuitivo:
> **Pantalla (View) → pide a → ViewModel → pide a → Repository → habla con → Room.**
> Si cada archivo que escribes respeta quién le habla a quién, tu app quedará ordenada.

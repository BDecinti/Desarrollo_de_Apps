# Diseña Madera Móvil

Aplicación móvil Android desarrollada como MVP académico para la asignatura **Desarrollo de Aplicaciones Móviles (DSY1105)** de Duoc UC, a partir del caso de *Diseña Madera · CIM UC*.

Permite a profesionales y estudiantes buscar, filtrar y consultar soluciones constructivas en madera, revisar sus fichas técnicas y descubrir normativa, documentos y herramientas relacionadas.

## Integrantes

| Nombre | Rol |
|--------|-----|
| Bruno Decinti | Desarrollo |
| Alessandro Nava | Desarrollo |

> Completar con los nombres completos de todos los integrantes del equipo.

## Funcionalidades implementadas

- **Inicio**: buscador, categorías rápidas y fichas destacadas.
- **Catálogo**: listado de fichas técnicas con búsqueda por texto y filtros por categoría, sistema constructivo, material y aplicación (con chips removibles y opción de limpiar filtros).
- **Detalle de ficha**: vista completa de cada solución constructiva.
- **Construcciones básicas (Muebles)**: sección con soluciones de muebles y construcciones simples en madera.
- **Normativa**: consulta de normativa y recursos relacionados.
- **Favoritos**: marcar/desmarcar fichas, con persistencia local mediante `SharedPreferences`.
- **Sesión de usuario**: inicio de sesión, registro y cierre de sesión (con confirmación), sesión persistente y sugerencias personalizadas según el perfil. Las cuentas creadas en la app se guardan localmente con la contraseña **hasheada con sal**.
- **Formularios validados** (inicio de sesión y registro) con retroalimentación visual por campo: ícono de error/éxito, mensaje bajo el campo, indicador de fortaleza de contraseña y animación de "sacudida" al enviar con errores.
- **Foto de perfil con la cámara** (recurso nativo 1): se solicita el permiso en tiempo de ejecución y la foto se guarda en el almacenamiento interno de la app.
- **Zona climática con la ubicación** (recurso nativo 2): en *Muebles* se detecta la zona de Chile del usuario (una sola lectura de ubicación) y se filtran las maderas recomendadas según su clima.
- Además se usan **Intent de compartir** y **feedback háptico** en el detalle de ficha.
- **Animaciones**: transiciones direccionales entre pestañas, entrada/salida deslizante del detalle, corazón de favorito con rebote, reordenamiento animado de listas, mensajes de error con expansión/fundido y banner de bienvenida temporal (4,5 s).
- Arquitectura **MVVM** (ViewModel + `StateFlow`), UI con **Jetpack Compose** y Material 3.
- Pruebas unitarias del ViewModel y de la lógica de dominio (validaciones, hash de contraseñas, zonas climáticas).

## Arquitectura y estructura del proyecto

```
app/src/main/java/com/example/myapplication/
├── data/
│   ├── local/        PreferencesManager, UserStorage, ProfilePhotoStorage   (persistencia local)
│   ├── models/       FichaModels                                           (modelos de datos)
│   └── repository/   FichaRepository, AuthRepository                       (acceso a datos)
├── domain/           Lógica pura, sin dependencias de Android y con tests
│   ├── validation/   FormValidators, ValidationResult
│   ├── security/     PasswordHasher
│   └── climate/      ClimateZone
├── device/           Acceso a recursos nativos: CameraAction, LocationAction, PermissionDeniedDialog
└── ui/
    ├── viewmodel/    DisenaMaderaViewModel, AuthFormViewModel              (estado y lógica de UI)
    ├── components/   Componentes reutilizables (ValidatedTextField, FavoriteButton, ProfileAvatar...)
    ├── screens/      Pantallas
    └── theme/        Color, Typography, Theme
```

Las reglas de validación viven en `domain/validation/FormValidators` y el estado de los formularios en `AuthFormViewModel`; los composables solo muestran el estado y notifican eventos (no contienen reglas de negocio).

## Recursos nativos y permisos

| Recurso | Permiso | Dónde se usa |
|---------|---------|--------------|
| Cámara | `CAMERA` (en tiempo de ejecución) + `FileProvider` | Inicio → avatar del perfil → foto de perfil |
| Ubicación | `ACCESS_COARSE_LOCATION` / `ACCESS_FINE_LOCATION` (en tiempo de ejecución) | Muebles → tarjeta de zona climática |
| Compartir | — (Intent del sistema) | Detalle de ficha |
| Vibración háptica | — | Detalle de ficha |

Si el usuario rechaza un permiso, la app explica para qué se usa y ofrece abrir los ajustes. Las funciones siguen disponibles sin cámara ni GPS (`uses-feature required="false"`).

### Usuarios de demostración

Se pueden usar también las cuentas creadas desde la pantalla *Crear cuenta*.


| Usuario | Contraseña |
|---------|------------|
| carpintero | maderas123 |
| arquitecto | diseno2026 |
| builder | patio2026 |

## Planificación y trabajo en equipo

- Tablero Trello: _pegar aquí el enlace del tablero_
- Repositorio GitHub: _pegar aquí el enlace del repositorio_

## Tecnologías

Kotlin, Jetpack Compose, Material 3, Coroutines/StateFlow, Android ViewModel, SharedPreferences, FileProvider, LocationManager. `minSdk 24`, `targetSdk 36`.

## Pasos para ejecutar

### Requisitos
- Android Studio (versión reciente) con Android SDK 36 instalado.
- JDK 17 o superior (incluido con Android Studio).
- Un emulador Android (API 24+) o un dispositivo físico con depuración USB activada.

### Ejecución
1. Clonar el repositorio:
   ```bash
   git clone <URL-del-repositorio>
   ```
2. Abrir Android Studio y seleccionar **File > Open**, eligiendo la carpeta `MyApplication`.
3. Esperar a que Gradle sincronice las dependencias (**Sync Project with Gradle Files**).
4. Seleccionar un emulador o dispositivo conectado.
5. Presionar **Run ▶** (o `Shift + F10`).

### Desde la terminal
```bash
cd MyApplication
./gradlew assembleDebug      # compilar (en Windows: gradlew.bat assembleDebug)
./gradlew installDebug       # instalar en dispositivo/emulador conectado
```

### Ejecutar pruebas unitarias
```bash
cd MyApplication
./gradlew test
```

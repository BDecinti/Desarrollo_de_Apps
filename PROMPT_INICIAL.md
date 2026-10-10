# Prompt inicial para crear el proyecto en Android Studio

Pega este prompt en tu asistente (Gemini en Android Studio, Claude Code u otro) y adjunta o deja en la raíz del repo el archivo `DISENO_APP.md`.

---

```
Actúa como desarrollador Android senior. Ayúdame a construir, paso a paso, el MVP académico
"Diseña Madera Móvil" para la asignatura DSY1105 (Desarrollo de Aplicaciones Móviles, Duoc UC).
Todo el alcance, pantallas, flujos, modelo de datos y casos de prueba están en DISENO_APP.md:
léelo primero y respétalo.

CONTEXTO
App Android para buscar, filtrar y consultar soluciones constructivas en madera, y ver
normativa, documentos y herramientas relacionadas. Es un MVP académico con una muestra de
datos ficticios (12 a 20 soluciones). No es una app productiva.

PAUTA DE EVALUACIÓN (prioriza esto)
- Interfaz visual 15 %, validaciones de campos 15 %, lógica de validación con MVVM 10 %,
  animaciones funcionales 10 % (con una o dos basta), estructura modular y persistencia
  local 15 %, Git/Trello 20 %, recursos nativos 15 % (con uno basta).

STACK OBLIGATORIO
- Kotlin, Jetpack Compose, Material 3
- Arquitectura MVVM (View / ViewModel / Repository), con StateFlow y UiState por pantalla
- Navigation Compose, con barra inferior: Explorar, Favoritos, Historial, Consulta
  (Acerca de se abre desde el menú de la barra superior)
- Room para catálogo, favoritos, historial y consultas; el catálogo se precarga desde assets/catalogo.json
- Coil para imágenes
- Pruebas unitarias con JUnit y kotlinx-coroutines-test
- Package base: cl.duoc.disenamadera · minSdk 26

ALCANCE DE ESTA PRIMERA IMPLEMENTACIÓN (Fase 1)
1. Catálogo sintético cargado a Room (soluciones, normativa ficticia, recursos, relaciones).
2. Pantalla Explorar con búsqueda por texto y soluciones destacadas.
3. Resultados con búsqueda por texto + filtros combinables (sistema constructivo, material,
   área temática, aplicación), con chips de filtros activos y "Limpiar filtros".
4. Ficha de detalle de solución con secciones y navegación a normativa, recursos y
   soluciones relacionadas.
5. Detalle de normativa y detalle de recurso/herramienta.
6. Favoritos y Historial persistidos en Room.
7. Formulario de consulta técnica con validaciones de campos en tiempo real: nombre (mín. 3,
   solo letras), correo válido, teléfono chileno opcional, perfil obligatorio, mensaje de
   20 a 500 caracteres con contador y términos obligatorios. Las reglas viven en una clase
   Validators; el ViewModel expone los errores por campo en el UiState. Se guarda en Room
   y hay una pantalla "Mis consultas".
8. Recurso nativo: cámara para adjuntar una foto a la consulta (guardarla en almacenamiento
   interno, mostrar vista previa, manejar cancelación y permiso denegado).
9. Dos animaciones funcionales: corazón de favorito (escala + color) y AnimatedVisibility
   en filtros/chips.
10. Estados de carga, vacío y error; confirmación al limpiar historial.
11. Pantalla Acerca de con aviso de que los datos son ficticios.
12. Pruebas unitarias de Validators y de los ViewModels principales.

FUERA DE ALCANCE POR AHORA (no implementar, pero deja la arquitectura preparada)
- Consumo de API con Retrofit
- Comparador de soluciones
- Asistente de recomendación simulado
- Simulación de e-token / firma electrónica
- Mapas, BIM, cálculos, descargas reales, modo offline completo, login

REGLAS DE TRABAJO
- Trabaja por etapas y detente al terminar cada una para que yo la pruebe:
  1) Crear proyecto, dependencias, tema y estructura de carpetas.
  2) Modelos, Room, seed desde catalogo.json y repositorios.
  3) Navegación y pantalla Explorar.
  4) Búsqueda, filtros y Resultados.
  5) Detalle de solución, normativa y recurso.
  6) Favoritos e Historial, con la animación del corazón.
  7) Formulario de consulta técnica: Validators, ViewModel, guardado en Room y Mis consultas.
  8) Cámara en el formulario.
  9) Estados, animación de filtros, pulido de UI y pantalla Acerca de.
  10) Pruebas unitarias y README.
- Genera un catalogo.json de ejemplo con datos 100% ficticios (por ejemplo normas como
  NORMA-DEMO-001; no uses documentos reales ni confidenciales).
- Código limpio, comentado donde aporte, nombres en español para el dominio y en inglés
  para elementos técnicos de Kotlin/Android.
- La View no contiene lógica de negocio; los ViewModels no referencian Composables ni Context.
- Explícame brevemente cada decisión importante, porque debo defender el proyecto en clases.
- Al terminar cada etapa, sugiere el nombre de la rama (feature/...) y un mensaje de commit
  con convención (feat:, fix:, docs:, test:), porque Git/Trello se evalúa.
- Si algo del enunciado es ambiguo, pregúntame antes de asumir.

Empieza con la etapa 1: indícame los pasos exactos en Android Studio, el contenido de
build.gradle.kts (con las dependencias) y la estructura de carpetas.
```

---

## Cómo usarlo

1. Copia `DISENO_APP.md` a la raíz de tu repositorio y haz commit.
2. Abre el proyecto en Android Studio y pega el prompt.
3. Avanza etapa por etapa y haz un commit por cada una.
4. Antes de la etapa 1, crea el tablero Trello (columnas Backlog, Por hacer, En progreso, En revisión, Hecho) y las ramas `main` y `develop`: Git/Trello vale el 20 % de la nota y se evalúa a lo largo del trabajo, no solo al final.
5. Confirma con tu docente si la "estructura modular" exige módulos Gradle además de la separación por paquetes.

# Matriz de requisitos – PetHealth (Grupo 08)

**Empresa:** PetHealth Cloud S.A.S.
**Objetivo:** registrar la atención médica veterinaria, diagnósticos y esquemas de vacunación.
**Actores:** Administrador, Veterinario, Auxiliar.

## 1. Requisitos funcionales del Grupo 08

| ID | Requisito | Actor(es) | Pantalla Android | Endpoint | Criterio de aceptación |
|---|---|---|---|---|---|
| RF-08-01 | Registrar pacientes | Todos | PacientesActivity, RegistrarPacienteActivity | `POST /api/mascotas`, `POST /api/propietarios` | Al guardar nombre, especie y propietario válidos, la mascota aparece en la lista. Sin propietario o sin nombre se muestra error y no se guarda. |
| RF-08-02 | Abrir historia clínica | Veterinario, Admin | HistoriaClinicaActivity | `POST /api/historias` | Si el paciente no tiene historia, el veterinario puede abrirla. Si ya tiene, la API responde 400. El auxiliar no ve el botón y la API le responde 403. |
| RF-08-03 | Documentar consulta | Veterinario, Admin | NuevaConsultaActivity | `POST /api/consultas` | La consulta con motivo se guarda con fecha actual y aparece en la cronología. Sin motivo no se guarda. |
| RF-08-04 | Registrar diagnóstico | Veterinario, Admin | DiagnosticosActivity (se abre al tocar una consulta de la cronología) | `POST /api/diagnosticos` | El diagnóstico queda asociado a una consulta existente y se ve en la historia. |
| RF-08-05 | Anotar tratamiento | Veterinario, Admin | NuevaConsultaActivity | `POST /api/consultas` | El tratamiento se guarda con la consulta y se muestra en la cronología y en el resumen. |
| RF-08-06 | Registrar vacuna | Todos | VacunacionActivity | `POST /api/vacunas/aplicaciones` | La aplicación se guarda con mascota, vacuna y fecha, y aparece en el listado de vacunas del paciente. |
| RF-08-07 | Programar refuerzo | Todos | VacunacionActivity | `POST /api/vacunas/aplicaciones` | Cada aplicación tiene fecha de refuerzo posterior a la de aplicación. |
| RF-08-08 | Consultar cronología | Todos | HistoriaClinicaActivity | `GET /api/historias/{mascotaId}` | Las consultas se muestran de la más reciente a la más antigua, con diagnósticos y tratamiento. |
| RF-08-09 | Filtrar por paciente | Todos | PacientesActivity | `GET /api/mascotas` | Al escribir en el buscador se filtra por nombre de mascota o de propietario. Si no hay coincidencias se muestra "No se encontraron pacientes". |
| RF-08-10 | Exportar resumen clínico | Todos | ReportesMedicosActivity | `GET /api/historias/{mascotaId}/resumen` | Se genera un resumen con datos del paciente, propietario, consultas, diagnósticos, tratamientos y vacunas. |

## 2. Requisitos funcionales comunes

| ID | Requisito | Actor(es) | Pantalla Android | Endpoint | Criterio de aceptación |
|---|---|---|---|---|---|
| RF-C-01 | Inicio de sesión | Todos | LoginActivity | `POST /api/auth/login` | Con credenciales válidas entra al Home; con inválidas muestra "Correo o contraseña incorrectos" (401). |
| RF-C-02 | Registro de usuarios | Administrador | RegistroActivity | `POST /api/auth/registro` | Solo el administrador ve el botón y puede crear usuarios con rol. Otro rol recibe 403. |
| RF-C-03 | Gestión de perfil | Todos | PerfilActivity | `GET /api/auth/perfil` | Muestra nombre, correo y rol del usuario de la sesión. |
| RF-C-04 | Cerrar sesión | Todos | HomeActivity | – | Borra la sesión guardada y vuelve al login. |

## 3. Requisitos no funcionales

| ID | Requisito | Cómo se cumple | Criterio de aceptación |
|---|---|---|---|
| RNF-08-01 | Validación de formularios y mensajes comprensibles | Validación en Android (campos obligatorios, formato de correo, fechas) y en la API (`[Required]`, `[EmailAddress]`, `[Range]`) | Ningún formulario inválido llega a guardarse; el usuario ve el error junto al campo. |
| RNF-08-02 | Estados cargando, vacío y error | Clase `Resultado` (Cargando / Exito / Error) en cada ViewModel | Cada lista muestra rueda de carga, mensaje de lista vacía y mensaje de error con "Reintentar". |
| RNF-08-03 | Persistencia y consistencia transaccional | MySQL + Entity Framework Core; llaves foráneas e índice único en historia clínica | Los datos siguen después de reiniciar la app y la API. |
| RNF-08-04 | Separación UI, lógica y acceso a datos | Android: Activity → ViewModel → Repository → Retrofit. API: Controller → Service → Repository → DbContext | Cada capa está en su propio paquete o proyecto. |
| RNF-08-05 | Control de acceso por rol validado en API | JWT + `[Authorize(Roles = ...)]` en los controllers | Sin token: 401. Rol sin permiso: 403, aunque se llame la API directamente. |
| RNF-08-06 | Interfaz adaptable y navegación accesible | Layouts con `match_parent`, `ScrollView` y Material Components; edge-to-edge | Las pantallas se ven completas en distintos tamaños y se navega con botones claros. |
| RNF-08-07 | Registro de errores sin exponer datos sensibles | Mensajes genéricos al usuario; contraseñas con BCrypt; Logcat en nivel BASIC | No se muestran contraseñas, tokens ni trazas de error al usuario ni en los logs. |
| RNF-08-08 | Pruebas de operaciones válidas e inválidas | Pruebas manuales en Swagger y en el emulador | Ver `docs/evidencias/`. |

## 4. Reglas de negocio

| ID | Regla | Dónde se valida |
|---|---|---|
| RN-01 | Una mascota tiene una sola historia clínica | API (`HistoriaClinicaService`) + índice único en BD |
| RN-02 | No se registra una mascota sin un propietario existente | API (`MascotaService`) + app |
| RN-03 | El documento del propietario no se puede repetir | API (`PropietarioService`) |
| RN-04 | Solo Veterinario y Administrador abren historias, registran consultas y diagnósticos | API (`[Authorize]`) + app oculta botones |
| RN-05 | Solo el Administrador registra usuarios y agrega vacunas al catálogo | API (`[Authorize]`) + app oculta botones |
| RN-06 | La fecha de aplicación de una vacuna no puede ser futura | API (`VacunaService`) + selector de fecha en app |
| RN-07 | La fecha de refuerzo debe ser posterior a la de aplicación | API (`VacunaService`) + app |
| RN-08 | Una consulta requiere motivo; un diagnóstico requiere una consulta existente | API + app |
| RN-09 | Las contraseñas se guardan cifradas (BCrypt) y tienen mínimo 6 caracteres | API (`AuthService`) |
| RN-10 | No puede haber dos usuarios con el mismo correo | API (`AuthService`) |
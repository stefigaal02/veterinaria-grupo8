# PetHealth – Historia clínica y vacunación veterinaria

Proyecto integrador – Desarrollo de Aplicaciones Móviles Nativas – UCompensar 2026-2
Grupo 08 – Empresa: PetHealth Cloud S.A.S.

## Integrantes
- Stteffany Torres Conde
- Stefanie Gallo Becerra

## Descripción
Aplicación Android para registrar la atención médica veterinaria: pacientes, historia clínica,
consultas, diagnósticos, tratamientos y esquemas de vacunación con refuerzos.

## Tecnologías
- Android: Kotlin, Activities con XML, ViewModel, Retrofit
- Backend: ASP.NET Core .NET 8, Entity Framework Core, JWT
- Base de datos: MySQL (XAMPP)

## Estructura del repositorio
- `android/` – Proyecto de Android Studio
- `backend/` – API REST por capas (Api, Application, Domain, Infrastructure)
- `docs/` – Diagramas, matriz de requisitos y evidencias de pruebas

## Cómo ejecutar

### Backend
1. Iniciar MySQL en XAMPP
2. cd backend
3. dotnet ef database update --project PetHealth.Infrastructure --startup-project PetHealth.Api
4. dotnet run --project PetHealth.Api
5. Swagger: http://localhost:5021/swagger

### Android
1. Abrir la carpeta android/PetHealth en Android Studio
2. Iniciar el emulador
3. Crear el túnel hacia la API (cada vez que se abre el emulador):
   adb reverse tcp:5021 tcp:5021
4. Ejecutar la app

### Usuarios de prueba
- vet@pethealth.com / 123456 (Veterinario)
- aux@pethealth.com / 123456 (Auxiliar)
- admin@pethealth.com / 123456 (Administrador)

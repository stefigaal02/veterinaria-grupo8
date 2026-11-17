package com.grupo08.pethealth.data.remote

data class LoginRequest(val email: String, val password: String)

data class RegistroRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val rol: String
)

data class UsuarioResponse(
    val id: Int,
    val nombre: String,
    val email: String,
    val rol: String
)

data class LoginResponse(val token: String, val usuario: UsuarioResponse)

data class MascotaResponse(
    val id: Int,
    val nombre: String,
    val especie: String,
    val raza: String,
    val propietarioId: Int,
    val propietarioNombre: String
)

data class PropietarioResponse(
    val id: Int,
    val documento: String,
    val nombre: String,
    val telefono: String
)

data class CrearPropietarioRequest(
    val documento: String,
    val nombre: String,
    val telefono: String
)

data class CrearMascotaRequest(
    val nombre: String,
    val especie: String,
    val raza: String,
    val propietarioId: Int
)

data class AbrirHistoriaRequest(val mascotaId: Int)

data class ConsultaResponse(
    val id: Int,
    val fecha: String,
    val motivo: String,
    val tratamiento: String,
    val diagnosticos: List<String>
)

data class HistoriaResponse(
    val id: Int,
    val mascotaId: Int,
    val mascotaNombre: String,
    val fechaApertura: String,
    val consultas: List<ConsultaResponse>
)

data class CrearConsultaRequest(
    val historiaClinicaId: Int,
    val motivo: String,
    val tratamiento: String
)

data class CrearDiagnosticoRequest(val consultaId: Int, val descripcion: String)

data class DiagnosticoResponse(val id: Int, val consultaId: Int, val descripcion: String)

data class VacunaResponse(val id: Int, val nombre: String)

data class CrearVacunaRequest(val nombre: String)

data class AplicarVacunaRequest(
    val mascotaId: Int,
    val vacunaId: Int,
    val fechaAplicacion: String,
    val fechaRefuerzo: String
)

data class AplicacionResponse(
    val id: Int,
    val mascotaId: Int,
    val mascotaNombre: String,
    val vacunaId: Int,
    val vacunaNombre: String,
    val fechaAplicacion: String,
    val fechaRefuerzo: String
)

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
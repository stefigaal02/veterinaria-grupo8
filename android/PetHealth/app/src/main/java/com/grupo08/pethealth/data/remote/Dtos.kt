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
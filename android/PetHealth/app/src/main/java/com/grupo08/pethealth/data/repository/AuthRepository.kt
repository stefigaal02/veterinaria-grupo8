package com.grupo08.pethealth.data.repository

import com.grupo08.pethealth.data.remote.LoginRequest
import com.grupo08.pethealth.data.remote.LoginResponse
import com.grupo08.pethealth.data.remote.PetHealthApi
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.SessionManager
import com.grupo08.pethealth.utils.llamarApi

class AuthRepository(
    private val api: PetHealthApi,
    private val session: SessionManager
) {
    suspend fun login(email: String, password: String): Resultado<LoginResponse> {
        val resultado = llamarApi { api.login(LoginRequest(email, password)) }
        if (resultado is Resultado.Exito) {
            val u = resultado.datos.usuario
            session.guardar(resultado.datos.token, u.nombre, u.rol)
        }
        return resultado
    }
}
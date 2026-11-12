package com.grupo08.pethealth.data.repository

import com.grupo08.pethealth.data.remote.CrearPropietarioRequest
import com.grupo08.pethealth.data.remote.PetHealthApi
import com.grupo08.pethealth.data.remote.PropietarioResponse
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.llamarApi

class PropietarioRepository(private val api: PetHealthApi) {

    suspend fun listar(): Resultado<List<PropietarioResponse>> =
        llamarApi { api.propietarios() }

    suspend fun crear(documento: String, nombre: String, telefono: String): Resultado<PropietarioResponse> =
        llamarApi { api.crearPropietario(CrearPropietarioRequest(documento, nombre, telefono)) }
}
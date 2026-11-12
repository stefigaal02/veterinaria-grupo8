package com.grupo08.pethealth.data.repository

import com.grupo08.pethealth.data.remote.MascotaResponse
import com.grupo08.pethealth.data.remote.PetHealthApi
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.llamarApi
import com.grupo08.pethealth.data.remote.CrearMascotaRequest
class MascotaRepository(private val api: PetHealthApi) {

    suspend fun listar(): Resultado<List<MascotaResponse>> =
        llamarApi { api.mascotas() }

    suspend fun crear(nombre: String, especie: String, raza: String, propietarioId: Int): Resultado<MascotaResponse> =
        llamarApi {
            api.crearMascota(CrearMascotaRequest(nombre, especie, raza, propietarioId))

        }
}
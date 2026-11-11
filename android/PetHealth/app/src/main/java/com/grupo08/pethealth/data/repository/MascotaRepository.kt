package com.grupo08.pethealth.data.repository

import com.grupo08.pethealth.data.remote.MascotaResponse
import com.grupo08.pethealth.data.remote.PetHealthApi
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.llamarApi

class MascotaRepository(private val api: PetHealthApi) {

    suspend fun listar(): Resultado<List<MascotaResponse>> =
        llamarApi { api.mascotas() }
}
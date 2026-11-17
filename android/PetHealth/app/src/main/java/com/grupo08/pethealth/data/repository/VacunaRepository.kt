package com.grupo08.pethealth.data.repository

import com.grupo08.pethealth.data.remote.*
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.llamarApi

class VacunaRepository(private val api: PetHealthApi) {

    suspend fun vacunas(): Resultado<List<VacunaResponse>> = llamarApi { api.vacunas() }

    suspend fun crearVacuna(nombre: String): Resultado<VacunaResponse> =
        llamarApi { api.crearVacuna(CrearVacunaRequest(nombre)) }

    suspend fun aplicar(mascotaId: Int, vacunaId: Int, fecha: String, refuerzo: String): Resultado<AplicacionResponse> =
        llamarApi { api.aplicarVacuna(AplicarVacunaRequest(mascotaId, vacunaId, fecha, refuerzo)) }

    suspend fun aplicaciones(mascotaId: Int): Resultado<List<AplicacionResponse>> =
        llamarApi { api.aplicaciones(mascotaId) }

    suspend fun proximas(dias: Int): Resultado<List<AplicacionResponse>> =
        llamarApi { api.proximas(dias) }
}

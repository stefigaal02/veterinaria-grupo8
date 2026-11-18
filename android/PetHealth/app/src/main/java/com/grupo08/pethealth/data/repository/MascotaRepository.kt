package com.grupo08.pethealth.data.repository

import com.grupo08.pethealth.data.remote.MascotaResponse
import com.grupo08.pethealth.data.remote.PetHealthApi
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.llamarApi
import com.grupo08.pethealth.data.remote.CrearMascotaRequest
import com.grupo08.pethealth.data.remote.MensajeResponse
class MascotaRepository(private val api: PetHealthApi) {

    suspend fun listar(): Resultado<List<MascotaResponse>> =
        llamarApi { api.mascotas() }

    suspend fun crear(nombre: String, especie: String, raza: String, propietarioId: Int): Resultado<MascotaResponse> =
        llamarApi {
            api.crearMascota(CrearMascotaRequest(nombre, especie, raza, propietarioId))

        }

    suspend fun obtener(id: Int): Resultado<MascotaResponse> =
        llamarApi { api.mascota(id) }

    suspend fun anulados(): Resultado<List<MascotaResponse>> =
        llamarApi { api.mascotasAnuladas() }

    suspend fun actualizar(id: Int, nombre: String, especie: String, raza: String, propietarioId: Int): Resultado<MascotaResponse> =
        llamarApi { api.actualizarMascota(id, CrearMascotaRequest(nombre, especie, raza, propietarioId)) }

    suspend fun anular(id: Int): Resultado<MensajeResponse> =
        llamarApi { api.anularMascota(id) }

    suspend fun reactivar(id: Int): Resultado<MensajeResponse> =
        llamarApi { api.reactivarMascota(id) }
}

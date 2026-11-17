package com.grupo08.pethealth.data.repository

import com.grupo08.pethealth.data.remote.*
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.llamarApi

class HistoriaRepository(private val api: PetHealthApi) {

    suspend fun obtener(mascotaId: Int): Resultado<HistoriaResponse> =
        llamarApi { api.historia(mascotaId) }

    suspend fun abrir(mascotaId: Int): Resultado<HistoriaResponse> =
        llamarApi { api.abrirHistoria(AbrirHistoriaRequest(mascotaId)) }

    suspend fun crearConsulta(historiaId: Int, motivo: String, tratamiento: String): Resultado<ConsultaResponse> =
        llamarApi { api.crearConsulta(CrearConsultaRequest(historiaId, motivo, tratamiento)) }

    suspend fun crearDiagnostico(consultaId: Int, descripcion: String): Resultado<DiagnosticoResponse> =
        llamarApi { api.crearDiagnostico(CrearDiagnosticoRequest(consultaId, descripcion)) }

    suspend fun resumen(mascotaId: Int): Resultado<String> {
        return when (val r = llamarApi { api.resumen(mascotaId) }) {
            is Resultado.Exito -> Resultado.Exito(r.datos.string())
            is Resultado.Error -> r
            else -> Resultado.Cargando
        }
    }
}

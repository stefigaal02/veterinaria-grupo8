package com.grupo08.pethealth.utils

import org.json.JSONObject
import retrofit2.Response
import java.io.IOException

suspend fun <T> llamarApi(llamada: suspend () -> Response<T>): Resultado<T> {
    return try {
        val respuesta = llamada()
        val cuerpo = respuesta.body()
        if (respuesta.isSuccessful && cuerpo != null) {
            Resultado.Exito(cuerpo)
        } else {
            Resultado.Error(mensajeError(respuesta))
        }
    } catch (e: IOException) {
        Resultado.Error("No hay conexión con el servidor")
    } catch (e: Exception) {
        Resultado.Error("Ocurrió un error inesperado")
    }
}

private fun mensajeError(respuesta: Response<*>): String {
    // Si la API mandó { "mensaje": "..." } usamos ese texto
    val texto = respuesta.errorBody()?.string()
    val mensaje = try {
        JSONObject(texto ?: "").optString("mensaje")
    } catch (e: Exception) {
        ""
    }
    if (mensaje.isNotBlank()) return mensaje

    return when (respuesta.code()) {
        400 -> "Datos inválidos, revisa el formulario"
        401 -> "Sesión no válida, inicia sesión de nuevo"
        403 -> "No tienes permiso para esta acción"
        404 -> "No se encontró la información"
        else -> "Error del servidor (${respuesta.code()})"
    }
}
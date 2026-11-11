package com.grupo08.pethealth.utils

sealed class Resultado<out T> {
    object Cargando : Resultado<Nothing>()
    data class Exito<T>(val datos: T) : Resultado<T>()
    data class Error(val mensaje: String) : Resultado<Nothing>()
}
package com.grupo08.pethealth.utils

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("pethealth_sesion", Context.MODE_PRIVATE)

    fun guardar(token: String, nombre: String, rol: String) {
        prefs.edit()
            .putString("token", token)
            .putString("nombre", nombre)
            .putString("rol", rol)
            .apply()
    }

    fun token(): String? = prefs.getString("token", null)
    fun nombre(): String = prefs.getString("nombre", "") ?: ""
    fun rol(): String = prefs.getString("rol", "") ?: ""

    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }
}
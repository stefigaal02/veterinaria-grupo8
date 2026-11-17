package com.grupo08.pethealth.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import com.grupo08.pethealth.utils.BaseActivity
import com.grupo08.pethealth.R
import com.grupo08.pethealth.ui.login.AuthViewModel
import com.grupo08.pethealth.utils.Resultado

class PerfilActivity : BaseActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val progress = findViewById<ProgressBar>(R.id.progress)
        val tvError = findViewById<TextView>(R.id.tvError)

        viewModel.perfil.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> progress.visibility = View.VISIBLE
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    findViewById<TextView>(R.id.tvNombre).text = "Nombre: ${estado.datos.nombre}"
                    findViewById<TextView>(R.id.tvEmail).text = "Correo: ${estado.datos.email}"
                    findViewById<TextView>(R.id.tvRol).text = "Rol: ${estado.datos.rol}"
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    tvError.text = estado.mensaje
                    tvError.visibility = View.VISIBLE
                }
            }
        }

        viewModel.cargarPerfil()
    }
}

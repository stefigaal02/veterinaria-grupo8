package com.grupo08.pethealth.ui.historia

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.grupo08.pethealth.utils.BaseActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.grupo08.pethealth.R
import com.grupo08.pethealth.utils.Resultado

class NuevaConsultaActivity : BaseActivity() {

    private val viewModel: HistoriaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_consulta)

        val historiaId = intent.getIntExtra("historiaId", 0)
        val tilMotivo = findViewById<TextInputLayout>(R.id.tilMotivo)
        val etMotivo = findViewById<TextInputEditText>(R.id.etMotivo)
        val etTratamiento = findViewById<TextInputEditText>(R.id.etTratamiento)
        val tvError = findViewById<TextView>(R.id.tvError)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            val motivo = etMotivo.text.toString().trim()
            val tratamiento = etTratamiento.text.toString().trim()
            tilMotivo.error = null
            if (motivo.isEmpty()) {
                tilMotivo.error = "El motivo es obligatorio"
            } else {
                viewModel.guardarConsulta(historiaId, motivo, tratamiento)
            }
        }

        viewModel.consulta.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    btnGuardar.isEnabled = false
                    tvError.visibility = View.GONE
                }
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this, "Consulta registrada", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    btnGuardar.isEnabled = true
                    tvError.text = estado.mensaje
                    tvError.visibility = View.VISIBLE
                }
            }
        }
    }
}

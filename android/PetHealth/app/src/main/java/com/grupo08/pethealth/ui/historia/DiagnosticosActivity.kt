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

class DiagnosticosActivity : BaseActivity() {

    private val viewModel: HistoriaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnosticos)

        val consultaId = intent.getIntExtra("consultaId", 0)
        findViewById<TextView>(R.id.tvConsulta).text = "Consulta: ${intent.getStringExtra("motivo")}"

        val tilDescripcion = findViewById<TextInputLayout>(R.id.tilDescripcion)
        val etDescripcion = findViewById<TextInputEditText>(R.id.etDescripcion)
        val tvError = findViewById<TextView>(R.id.tvError)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            val descripcion = etDescripcion.text.toString().trim()
            tilDescripcion.error = null
            if (descripcion.isEmpty()) {
                tilDescripcion.error = "La descripción es obligatoria"
            } else {
                viewModel.guardarDiagnostico(consultaId, descripcion)
            }
        }

        viewModel.diagnostico.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    btnGuardar.isEnabled = false
                    tvError.visibility = View.GONE
                }
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this, "Diagnóstico registrado", Toast.LENGTH_SHORT).show()
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

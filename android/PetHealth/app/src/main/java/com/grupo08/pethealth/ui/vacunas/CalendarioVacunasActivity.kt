package com.grupo08.pethealth.ui.vacunas

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import com.grupo08.pethealth.utils.BaseActivity
import com.grupo08.pethealth.R
import com.grupo08.pethealth.utils.Resultado

class CalendarioVacunasActivity : BaseActivity() {

    private val viewModel: VacunaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario_vacunas)

        val mascotaId = intent.getIntExtra("mascotaId", 0)
        val lista = findViewById<ListView>(R.id.lvProximas)
        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val btnReintentar = findViewById<Button>(R.id.btnReintentar)

        btnReintentar.setOnClickListener { viewModel.cargarAplicaciones(mascotaId) }

        viewModel.aplicaciones.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    tvMensaje.visibility = View.GONE
                    btnReintentar.visibility = View.GONE
                }
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    val textos = estado.datos.sortedBy { it.fechaRefuerzo }.map {
                        "${it.vacunaNombre}\nAplicada: ${it.fechaAplicacion.take(10)}  Refuerzo: ${it.fechaRefuerzo.take(10)}"
                    }
                    lista.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, textos)
                    tvMensaje.text = "Este paciente no tiene vacunas programadas"
                    tvMensaje.visibility = if (textos.isEmpty()) View.VISIBLE else View.GONE
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    tvMensaje.text = estado.mensaje
                    tvMensaje.visibility = View.VISIBLE
                    btnReintentar.visibility = View.VISIBLE
                }
            }
        }

        viewModel.cargarAplicaciones(mascotaId)
    }
}

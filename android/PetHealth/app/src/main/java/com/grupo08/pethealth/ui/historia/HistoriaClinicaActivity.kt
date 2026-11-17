package com.grupo08.pethealth.ui.historia

import android.content.Intent
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
import com.grupo08.pethealth.data.remote.HistoriaResponse
import com.grupo08.pethealth.ui.vacunas.VacunacionActivity
import com.grupo08.pethealth.utils.Resultado

class HistoriaClinicaActivity : BaseActivity() {

    private val viewModel: HistoriaViewModel by viewModels()
    private var mascotaId = 0
    private var historia: HistoriaResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historia)

        mascotaId = intent.getIntExtra("mascotaId", 0)
        val nombre = intent.getStringExtra("mascotaNombre") ?: ""

        findViewById<TextView>(R.id.tvTitulo).text = "Historia de $nombre"
        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val btnAbrir = findViewById<Button>(R.id.btnAbrir)
        val panel = findViewById<View>(R.id.panelAcciones)
        val lista = findViewById<ListView>(R.id.lvConsultas)

        btnAbrir.setOnClickListener { viewModel.abrir(mascotaId) }

        findViewById<Button>(R.id.btnNuevaConsulta).setOnClickListener {
            val i = Intent(this, NuevaConsultaActivity::class.java)
            i.putExtra("historiaId", historia?.id ?: 0)
            startActivity(i)
        }

        findViewById<Button>(R.id.btnVacunacion).setOnClickListener {
            val i = Intent(this, VacunacionActivity::class.java)
            i.putExtra("mascotaId", mascotaId)
            i.putExtra("mascotaNombre", nombre)
            startActivity(i)
        }

        findViewById<Button>(R.id.btnReporte).setOnClickListener {
            val i = Intent(this, ReportesMedicosActivity::class.java)
            i.putExtra("mascotaId", mascotaId)
            startActivity(i)
        }

        lista.setOnItemClickListener { _, _, pos, _ ->
            val c = historia?.consultas?.get(pos) ?: return@setOnItemClickListener
            val i = Intent(this, DiagnosticosActivity::class.java)
            i.putExtra("consultaId", c.id)
            i.putExtra("motivo", c.motivo)
            startActivity(i)
        }

        viewModel.historia.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    tvMensaje.visibility = View.GONE
                    btnAbrir.visibility = View.GONE
                }
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    historia = estado.datos
                    panel.visibility = View.VISIBLE
                    val consultas = estado.datos.consultas.sortedByDescending { it.fecha }
                    historia = estado.datos.copy(consultas = consultas)
                    val textos = consultas.map {
                        val diag = if (it.diagnosticos.isEmpty()) "Sin diagnóstico" else it.diagnosticos.joinToString(", ")
                        "${it.fecha.take(10)} - ${it.motivo}\nTratamiento: ${it.tratamiento}\nDiagnóstico: $diag"
                    }
                    lista.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, textos)
                    if (consultas.isEmpty()) {
                        tvMensaje.text = "Aún no hay consultas registradas"
                        tvMensaje.visibility = View.VISIBLE
                    } else {
                        tvMensaje.visibility = View.GONE
                    }
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    panel.visibility = View.GONE
                    lista.adapter = null
                    tvMensaje.text = estado.mensaje
                    tvMensaje.visibility = View.VISIBLE
                    btnAbrir.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargar(mascotaId)
    }
}

package com.grupo08.pethealth.ui.historia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.grupo08.pethealth.R
import com.grupo08.pethealth.data.remote.HistoriaResponse
import com.grupo08.pethealth.data.remote.MascotaResponse
import com.grupo08.pethealth.ui.pacientes.RegistrarPacienteActivity
import com.grupo08.pethealth.ui.vacunas.VacunacionActivity
import com.grupo08.pethealth.utils.BaseActivity
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.SessionManager

class HistoriaClinicaActivity : BaseActivity() {

    private val viewModel: HistoriaViewModel by viewModels()
    private var mascotaId = 0
    private var historia: HistoriaResponse? = null
    private var mascota: MascotaResponse? = null
    private var sinHistoria = false

    private lateinit var btnAbrir: Button
    private lateinit var btnNuevaConsulta: Button
    private lateinit var btnEditar: Button
    private lateinit var btnAnular: Button
    private lateinit var tvAnulado: TextView
    private var puedeEscribir = true
    private var esAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historia)

        mascotaId = intent.getIntExtra("mascotaId", 0)
        val nombre = intent.getStringExtra("mascotaNombre") ?: ""

        findViewById<TextView>(R.id.tvTitulo).text = "Historia de $nombre"
        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)
        val progress = findViewById<ProgressBar>(R.id.progress)
        btnAbrir = findViewById(R.id.btnAbrir)
        btnNuevaConsulta = findViewById(R.id.btnNuevaConsulta)
        btnEditar = findViewById(R.id.btnEditar)
        btnAnular = findViewById(R.id.btnAnular)
        tvAnulado = findViewById(R.id.tvAnulado)
        val panel = findViewById<View>(R.id.panelAcciones)
        val lista = findViewById<ListView>(R.id.lvConsultas)

        val rol = SessionManager(this).rol()
        puedeEscribir = rol != "Auxiliar"
        esAdmin = rol == "Administrador"

        btnAbrir.setOnClickListener { viewModel.abrir(mascotaId) }

        btnNuevaConsulta.setOnClickListener {
            val i = Intent(this, NuevaConsultaActivity::class.java)
            i.putExtra("historiaId", historia?.id ?: 0)
            startActivity(i)
        }

        btnEditar.setOnClickListener {
            val m = mascota ?: return@setOnClickListener
            val i = Intent(this, RegistrarPacienteActivity::class.java)
            i.putExtra("mascotaId", m.id)
            i.putExtra("nombre", m.nombre)
            i.putExtra("especie", m.especie)
            i.putExtra("raza", m.raza)
            i.putExtra("propietarioId", m.propietarioId)
            startActivity(i)
        }

        btnAnular.setOnClickListener {
            val m = mascota ?: return@setOnClickListener
            MaterialAlertDialogBuilder(this)
                .setTitle("Anular paciente")
                .setMessage("¿Anular a ${m.nombre}? Dejará de aparecer en la lista, pero su historia clínica se conserva.")
                .setPositiveButton("Anular") { _, _ -> viewModel.anular(mascotaId) }
                .setNegativeButton("Cancelar", null)
                .show()
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
            if (!puedeEscribir) return@setOnItemClickListener
            val c = historia?.consultas?.get(pos) ?: return@setOnItemClickListener
            val i = Intent(this, DiagnosticosActivity::class.java)
            i.putExtra("consultaId", c.id)
            i.putExtra("motivo", c.motivo)
            startActivity(i)
        }

        viewModel.mascota.observe(this) { estado ->
            if (estado is Resultado.Exito) {
                mascota = estado.datos
                findViewById<TextView>(R.id.tvTitulo).text = "Historia de ${estado.datos.nombre}"
                actualizarBotones()
            } else if (estado is Resultado.Error) {
                Toast.makeText(this, estado.mensaje, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.anulado.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> progress.visibility = View.VISIBLE
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this, estado.datos.mensaje, Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this, estado.mensaje, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.historia.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    tvMensaje.visibility = View.GONE
                    sinHistoria = false
                    actualizarBotones()
                }
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    sinHistoria = false
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
                    actualizarBotones()
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    panel.visibility = View.GONE
                    lista.adapter = null
                    tvMensaje.text = estado.mensaje
                    tvMensaje.visibility = View.VISIBLE
                    sinHistoria = estado.mensaje.contains("no tiene historia")
                    actualizarBotones()
                }
            }
        }
    }

    private fun actualizarBotones() {
        val m = mascota
        val anulado = m != null && !m.activo
        tvAnulado.visibility = if (anulado) View.VISIBLE else View.GONE
        btnNuevaConsulta.visibility = if (puedeEscribir && !anulado) View.VISIBLE else View.GONE
        btnAbrir.visibility = if (puedeEscribir && !anulado && sinHistoria) View.VISIBLE else View.GONE
        btnEditar.visibility = if (m != null && !anulado) View.VISIBLE else View.GONE
        btnAnular.visibility = if (esAdmin && m != null && !anulado) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargarMascota(mascotaId)
        viewModel.cargar(mascotaId)
    }
}

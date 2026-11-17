package com.grupo08.pethealth.ui.vacunas

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.grupo08.pethealth.utils.BaseActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.grupo08.pethealth.R
import com.grupo08.pethealth.data.remote.VacunaResponse
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class VacunacionActivity : BaseActivity() {

    private val viewModel: VacunaViewModel by viewModels()
    private var vacunas: List<VacunaResponse> = emptyList()
    private var mascotaId = 0
    private var dialogo: AlertDialog? = null
    private val formato = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vacunacion)

        mascotaId = intent.getIntExtra("mascotaId", 0)
        findViewById<TextView>(R.id.tvTitulo).text = "Vacunas de ${intent.getStringExtra("mascotaNombre")}"

        val spVacuna = findViewById<Spinner>(R.id.spVacuna)
        val tilFecha = findViewById<TextInputLayout>(R.id.tilFecha)
        val tilRefuerzo = findViewById<TextInputLayout>(R.id.tilRefuerzo)
        val etFecha = findViewById<TextInputEditText>(R.id.etFecha)
        val etRefuerzo = findViewById<TextInputEditText>(R.id.etRefuerzo)
        val tvError = findViewById<TextView>(R.id.tvError)
        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val lista = findViewById<ListView>(R.id.lvAplicaciones)

        val hoy = formato.format(Date())
        etFecha.setText(hoy)
        etFecha.setOnClickListener {
            elegirFecha(etFecha, null, System.currentTimeMillis())
        }
        etRefuerzo.setOnClickListener {
            val minimo = Calendar.getInstance()
            minimo.time = formato.parse(etFecha.text.toString()) ?: Date()
            minimo.add(Calendar.DAY_OF_MONTH, 1)
            elegirFecha(etRefuerzo, minimo.timeInMillis, null)
        }

        val btnNuevaVacuna = findViewById<Button>(R.id.btnNuevaVacuna)
        if (SessionManager(this).rol() != "Administrador") btnNuevaVacuna.visibility = View.GONE
        btnNuevaVacuna.setOnClickListener { mostrarDialogoVacuna() }
        findViewById<Button>(R.id.btnCalendario).setOnClickListener {
            startActivity(Intent(this, CalendarioVacunasActivity::class.java).putExtra("mascotaId", mascotaId))
        }

        btnGuardar.setOnClickListener {
            val fecha = etFecha.text.toString()
            val refuerzo = etRefuerzo.text.toString()
            tilFecha.error = if (fecha.isEmpty()) "Selecciona la fecha" else null
            tilRefuerzo.error = if (refuerzo.isEmpty()) "Selecciona la fecha de refuerzo" else null
            tvError.visibility = View.GONE

            if (vacunas.isEmpty()) {
                tvError.text = "No hay vacunas, crea una con \"+ Nueva vacuna\""
                tvError.visibility = View.VISIBLE
            } else if (fecha.isNotEmpty() && refuerzo.isNotEmpty()) {
                if (fecha > hoy) {
                    tilFecha.error = "La fecha de aplicación no puede ser futura"
                } else if (refuerzo <= fecha) {
                    tilRefuerzo.error = "El refuerzo debe ser posterior a la aplicación"
                } else {
                    val vacuna = vacunas[spVacuna.selectedItemPosition]
                    viewModel.aplicar(mascotaId, vacuna.id, fecha, refuerzo)
                }
            }
        }

        viewModel.vacunas.observe(this) { estado ->
            if (estado is Resultado.Exito) {
                vacunas = estado.datos
                spVacuna.adapter = ArrayAdapter(
                    this, android.R.layout.simple_spinner_dropdown_item, vacunas.map { it.nombre }
                )
            } else if (estado is Resultado.Error) {
                tvError.text = estado.mensaje
                tvError.visibility = View.VISIBLE
            }
        }

        viewModel.vacunaCreada.observe(this) { estado ->
            when (estado) {
                is Resultado.Exito -> {
                    dialogo?.dismiss()
                    Toast.makeText(this, "Vacuna creada", Toast.LENGTH_SHORT).show()
                    viewModel.cargarVacunas()
                }
                is Resultado.Error -> Toast.makeText(this, estado.mensaje, Toast.LENGTH_LONG).show()
                else -> {}
            }
        }

        viewModel.guardado.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    btnGuardar.isEnabled = false
                }
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    btnGuardar.isEnabled = true
                    etFecha.setText(hoy)
                    etRefuerzo.setText("")
                    Toast.makeText(this, "Vacuna registrada", Toast.LENGTH_SHORT).show()
                    viewModel.cargarAplicaciones(mascotaId)
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    btnGuardar.isEnabled = true
                    tvError.text = estado.mensaje
                    tvError.visibility = View.VISIBLE
                }
            }
        }

        viewModel.aplicaciones.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> progress.visibility = View.VISIBLE
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    val textos = estado.datos.map {
                        "${it.vacunaNombre}\nAplicada: ${it.fechaAplicacion.take(10)}  Refuerzo: ${it.fechaRefuerzo.take(10)}"
                    }
                    lista.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, textos)
                    tvMensaje.text = "Aún no hay vacunas aplicadas"
                    tvMensaje.visibility = if (textos.isEmpty()) View.VISIBLE else View.GONE
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    tvMensaje.text = estado.mensaje
                    tvMensaje.visibility = View.VISIBLE
                }
            }
        }

        viewModel.cargarVacunas()
        viewModel.cargarAplicaciones(mascotaId)
    }

    private fun elegirFecha(campo: TextInputEditText, minimo: Long?, maximo: Long?) {
        val c = Calendar.getInstance()
        campo.text.toString().takeIf { it.isNotEmpty() }?.let { c.time = formato.parse(it) ?: Date() }
        val dialogo = DatePickerDialog(this, { _, a, m, d ->
            campo.setText(String.format("%04d-%02d-%02d", a, m + 1, d))
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
        minimo?.let { dialogo.datePicker.minDate = it }
        maximo?.let { dialogo.datePicker.maxDate = it }
        dialogo.show()
    }

    private fun mostrarDialogoVacuna() {
        val vista = layoutInflater.inflate(R.layout.dialog_vacuna, null)
        val til = vista.findViewById<TextInputLayout>(R.id.tilNombreVacuna)
        val et = vista.findViewById<TextInputEditText>(R.id.etNombreVacuna)

        dialogo = MaterialAlertDialogBuilder(this)
            .setTitle("Nueva vacuna")
            .setView(vista)
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", null)
            .show()

        dialogo?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val nombre = et.text.toString().trim()
            if (nombre.isEmpty()) {
                til.error = "Obligatorio"
            } else {
                viewModel.crearVacuna(nombre)
            }
        }
    }
}

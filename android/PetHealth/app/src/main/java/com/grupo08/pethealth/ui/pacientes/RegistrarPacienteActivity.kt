package com.grupo08.pethealth.ui.pacientes

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.grupo08.pethealth.R
import com.grupo08.pethealth.data.remote.PropietarioResponse
import com.grupo08.pethealth.utils.Resultado

class RegistrarPacienteActivity : AppCompatActivity() {

    private val viewModel: RegistrarPacienteViewModel by viewModels()
    private var propietarios: List<PropietarioResponse> = emptyList()
    private var idPorSeleccionar: Int? = null
    private var dialogo: AlertDialog? = null

    private lateinit var spPropietario: Spinner
    private lateinit var tvErrorPropietario: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_paciente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        spPropietario = findViewById(R.id.spPropietario)
        tvErrorPropietario = findViewById(R.id.tvErrorPropietario)
        val tilNombre = findViewById<TextInputLayout>(R.id.tilNombre)
        val tilEspecie = findViewById<TextInputLayout>(R.id.tilEspecie)
        val etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        val etEspecie = findViewById<TextInputEditText>(R.id.etEspecie)
        val etRaza = findViewById<TextInputEditText>(R.id.etRaza)
        val tvError = findViewById<TextView>(R.id.tvError)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val progress = findViewById<ProgressBar>(R.id.progress)

        findViewById<Button>(R.id.btnNuevoPropietario).setOnClickListener { mostrarDialogoPropietario() }

        // ----- Lista de propietarios -----
        viewModel.propietarios.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> tvErrorPropietario.visibility = View.GONE
                is Resultado.Exito -> {
                    propietarios = estado.datos
                    val textos = propietarios.map { "${it.nombre} - ${it.documento}" }
                    spPropietario.adapter = ArrayAdapter(
                        this, android.R.layout.simple_spinner_dropdown_item, textos
                    )
                    // Si se acaba de crear uno, dejarlo seleccionado
                    idPorSeleccionar?.let { id ->
                        val pos = propietarios.indexOfFirst { it.id == id }
                        if (pos >= 0) spPropietario.setSelection(pos)
                        idPorSeleccionar = null
                    }
                    if (propietarios.isEmpty()) {
                        tvErrorPropietario.text = "No hay propietarios. Crea uno con \"+ Nuevo\""
                        tvErrorPropietario.visibility = View.VISIBLE
                    }
                }
                is Resultado.Error -> {
                    tvErrorPropietario.text = estado.mensaje
                    tvErrorPropietario.visibility = View.VISIBLE
                }
            }
        }

        // ----- Resultado de crear propietario -----
        viewModel.propietarioCreado.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {}
                is Resultado.Exito -> {
                    dialogo?.dismiss()
                    Toast.makeText(this, "Propietario registrado", Toast.LENGTH_SHORT).show()
                    idPorSeleccionar = estado.datos.id
                    viewModel.cargarPropietarios()
                }
                is Resultado.Error -> {
                    // Ej: "Ya existe un propietario con ese documento"
                    Toast.makeText(this, estado.mensaje, Toast.LENGTH_LONG).show()
                }
            }
        }

        // ----- Guardar paciente -----
        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val especie = etEspecie.text.toString().trim()
            val raza = etRaza.text.toString().trim()

            tilNombre.error = null
            tilEspecie.error = null
            var valido = true
            if (nombre.isEmpty()) { tilNombre.error = "El nombre es obligatorio"; valido = false }
            if (especie.isEmpty()) { tilEspecie.error = "La especie es obligatoria"; valido = false }
            if (propietarios.isEmpty()) {
                tvErrorPropietario.text = "Debes seleccionar un propietario"
                tvErrorPropietario.visibility = View.VISIBLE
                valido = false
            }

            if (valido) {
                val propietario = propietarios[spPropietario.selectedItemPosition]
                viewModel.guardar(nombre, especie, raza, propietario.id)
            }
        }

        viewModel.guardado.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    btnGuardar.isEnabled = false
                    tvError.visibility = View.GONE
                }
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this, "Paciente ${estado.datos.nombre} registrado", Toast.LENGTH_SHORT).show()
                    finish()   // vuelve a la lista, que se recarga sola
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    btnGuardar.isEnabled = true
                    tvError.text = estado.mensaje
                    tvError.visibility = View.VISIBLE
                }
            }
        }

        viewModel.cargarPropietarios()
    }

    private fun mostrarDialogoPropietario() {
        val vista = layoutInflater.inflate(R.layout.dialog_propietario, null)
        val tilDoc = vista.findViewById<TextInputLayout>(R.id.tilDocumento)
        val tilNom = vista.findViewById<TextInputLayout>(R.id.tilNombreProp)
        val tilTel = vista.findViewById<TextInputLayout>(R.id.tilTelefono)
        val etDoc = vista.findViewById<TextInputEditText>(R.id.etDocumento)
        val etNom = vista.findViewById<TextInputEditText>(R.id.etNombreProp)
        val etTel = vista.findViewById<TextInputEditText>(R.id.etTelefono)

        dialogo = MaterialAlertDialogBuilder(this)
            .setTitle("Nuevo propietario")
            .setView(vista)
            .setPositiveButton("Guardar", null)   // se configura abajo para validar antes de cerrar
            .setNegativeButton("Cancelar", null)
            .show()

        dialogo?.getButton(AlertDialog.BUTTON_POSITIVE)?.setOnClickListener {
            val doc = etDoc.text.toString().trim()
            val nom = etNom.text.toString().trim()
            val tel = etTel.text.toString().trim()

            tilDoc.error = if (doc.isEmpty()) "Obligatorio" else null
            tilNom.error = if (nom.isEmpty()) "Obligatorio" else null
            tilTel.error = if (tel.length < 7) "Teléfono no válido" else null

            if (tilDoc.error == null && tilNom.error == null && tilTel.error == null) {
                viewModel.crearPropietario(doc, nom, tel)
            }
        }
    }
}
package com.grupo08.pethealth.ui.pacientes

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.grupo08.pethealth.R
import com.grupo08.pethealth.data.remote.MascotaResponse
import com.grupo08.pethealth.utils.Resultado
import android.content.Intent

class PacientesActivity : AppCompatActivity() {

    private val viewModel: PacientesViewModel by viewModels()
    private lateinit var adapter: PacientesAdapter
    private lateinit var etBuscar: TextInputEditText
    private lateinit var tvMensaje: TextView
    private var todos: List<MascotaResponse> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pacientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left + 32, bars.top + 32, bars.right + 32, bars.bottom)
            insets
        }

        etBuscar = findViewById(R.id.etBuscar)
        tvMensaje = findViewById(R.id.tvMensaje)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val btnReintentar = findViewById<Button>(R.id.btnReintentar)
        findViewById<View>(R.id.fabNuevo).setOnClickListener {
            startActivity(Intent(this, RegistrarPacienteActivity::class.java))
        }
        val rv = findViewById<RecyclerView>(R.id.rvPacientes)

        adapter = PacientesAdapter { mascota ->
            // En el siguiente paso esto abrirá la historia clínica
            Toast.makeText(this, "Paciente: ${mascota.nombre}", Toast.LENGTH_SHORT).show()
        }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        etBuscar.doAfterTextChanged { filtrar() }
        btnReintentar.setOnClickListener { viewModel.cargar() }

        viewModel.estado.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    tvMensaje.visibility = View.GONE
                    btnReintentar.visibility = View.GONE
                }
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    todos = estado.datos
                    filtrar()
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    adapter.actualizar(emptyList())
                    tvMensaje.text = estado.mensaje
                    tvMensaje.visibility = View.VISIBLE
                    btnReintentar.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargar()   // recarga al volver a la pantalla
    }

    // RF-08-09: filtrar por paciente
    private fun filtrar() {
        val texto = etBuscar.text.toString().trim().lowercase()
        val lista = if (texto.isEmpty()) todos else todos.filter {
            it.nombre.lowercase().contains(texto) ||
                    it.propietarioNombre.lowercase().contains(texto)
        }
        adapter.actualizar(lista)

        if (lista.isEmpty()) {
            tvMensaje.text = if (todos.isEmpty()) "Aún no hay pacientes registrados"
            else "No se encontraron pacientes"
            tvMensaje.visibility = View.VISIBLE
        } else {
            tvMensaje.visibility = View.GONE
        }
    }
}
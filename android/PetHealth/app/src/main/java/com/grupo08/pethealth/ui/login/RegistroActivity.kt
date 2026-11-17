package com.grupo08.pethealth.ui.login

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.grupo08.pethealth.utils.BaseActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.grupo08.pethealth.R
import com.grupo08.pethealth.utils.Resultado

class RegistroActivity : BaseActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val tilNombre = findViewById<TextInputLayout>(R.id.tilNombre)
        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val spRol = findViewById<Spinner>(R.id.spRol)
        val tvError = findViewById<TextView>(R.id.tvError)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        spRol.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            listOf("Veterinario", "Auxiliar", "Administrador")
        )

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            tilNombre.error = if (nombre.isEmpty()) "El nombre es obligatorio" else null
            tilEmail.error = if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Correo no válido" else null
            tilPassword.error = if (password.length < 6) "Mínimo 6 caracteres" else null

            if (tilNombre.error == null && tilEmail.error == null && tilPassword.error == null) {
                viewModel.registrar(nombre, email, password, spRol.selectedItem.toString())
            }
        }

        viewModel.registro.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    btnRegistrar.isEnabled = false
                    tvError.visibility = View.GONE
                }
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    btnRegistrar.isEnabled = true
                    tvError.text = estado.mensaje
                    tvError.visibility = View.VISIBLE
                }
            }
        }
    }
}

package com.grupo08.pethealth.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.grupo08.pethealth.R
import com.grupo08.pethealth.ui.home.HomeActivity
import com.grupo08.pethealth.utils.Resultado
import com.grupo08.pethealth.utils.SessionManager

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left + 48, bars.top, bars.right + 48, bars.bottom)
            insets
        }

        // Si ya hay sesión guardada, entra directo
        if (SessionManager(this).token() != null) {
            irAHome()
            return
        }

        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnIngresar = findViewById<Button>(R.id.btnIngresar)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val tvError = findViewById<TextView>(R.id.tvError)

        btnIngresar.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            // Validación del lado del cliente
            tilEmail.error = null
            tilPassword.error = null
            var valido = true
            if (email.isEmpty()) {
                tilEmail.error = "Ingresa tu correo"
                valido = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmail.error = "Correo no válido"
                valido = false
            }
            if (password.isEmpty()) {
                tilPassword.error = "Ingresa tu contraseña"
                valido = false
            }

            if (valido) viewModel.login(email, password)
        }

        viewModel.estado.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    btnIngresar.isEnabled = false
                    tvError.visibility = View.GONE
                }
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    irAHome()
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    btnIngresar.isEnabled = true
                    tvError.text = estado.mensaje
                    tvError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun irAHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
package com.grupo08.pethealth.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.grupo08.pethealth.R
import com.grupo08.pethealth.ui.login.LoginActivity
import com.grupo08.pethealth.utils.SessionManager

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val session = SessionManager(this)
        findViewById<TextView>(R.id.tvBienvenida).text =
            "Hola, ${session.nombre()}\nRol: ${session.rol()}"

        findViewById<Button>(R.id.btnCerrarSesion).setOnClickListener {
            session.cerrarSesion()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
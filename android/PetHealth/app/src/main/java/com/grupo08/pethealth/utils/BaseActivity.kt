package com.grupo08.pethealth.utils

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

open class BaseActivity : AppCompatActivity() {

    open val conFlecha = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(conFlecha)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        val valor = TypedValue()
        theme.resolveAttribute(androidx.appcompat.R.attr.actionBarSize, valor, true)
        val altoBarra = TypedValue.complexToDimensionPixelSize(valor.data, resources.displayMetrics)
        val contenido = findViewById<View>(android.R.id.content)
        ViewCompat.setOnApplyWindowInsetsListener(contenido) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top + altoBarra, bars.right, 0)
            insets
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

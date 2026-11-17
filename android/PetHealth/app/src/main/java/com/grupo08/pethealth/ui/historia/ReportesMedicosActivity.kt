package com.grupo08.pethealth.ui.historia

import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import com.grupo08.pethealth.utils.BaseActivity
import com.grupo08.pethealth.R
import com.grupo08.pethealth.utils.Resultado

class ReportesMedicosActivity : BaseActivity() {

    private val viewModel: HistoriaViewModel by viewModels()

    private val crearPdf = registerForActivityResult(
        ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        if (uri != null) guardarPdf(uri)
    }

    private fun guardarPdf(uri: Uri) {
        val texto = findViewById<TextView>(R.id.tvResumen).text.toString()
        val paint = Paint().apply { textSize = 12f }
        val ancho = 595
        val alto = 842
        val margen = 40
        val lineas = mutableListOf<String>()
        for (parrafo in texto.split("\n")) {
            var resto = parrafo
            if (resto.isEmpty()) lineas.add("")
            while (resto.isNotEmpty()) {
                val n = paint.breakText(resto, true, (ancho - 2 * margen).toFloat(), null)
                lineas.add(resto.substring(0, n))
                resto = resto.substring(n)
            }
        }

        val pdf = PdfDocument()
        var numero = 1
        var pagina = pdf.startPage(PdfDocument.PageInfo.Builder(ancho, alto, numero).create())
        var y = margen.toFloat()
        for (linea in lineas) {
            if (y > alto - margen) {
                pdf.finishPage(pagina)
                numero++
                pagina = pdf.startPage(PdfDocument.PageInfo.Builder(ancho, alto, numero).create())
                y = margen.toFloat()
            }
            pagina.canvas.drawText(linea, margen.toFloat(), y, paint)
            y += 18f
        }
        pdf.finishPage(pagina)

        try {
            contentResolver.openOutputStream(uri)?.use { pdf.writeTo(it) }
            Toast.makeText(this, "PDF guardado", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "No se pudo guardar el PDF", Toast.LENGTH_LONG).show()
        }
        pdf.close()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reportes_medicos)

        val mascotaId = intent.getIntExtra("mascotaId", 0)
        val tvResumen = findViewById<TextView>(R.id.tvResumen)
        val tvMensaje = findViewById<TextView>(R.id.tvMensaje)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val btnCompartir = findViewById<Button>(R.id.btnCompartir)

        val btnPdf = findViewById<Button>(R.id.btnPdf)
        btnPdf.setOnClickListener { crearPdf.launch("resumen-clinico-$mascotaId.pdf") }

        btnCompartir.setOnClickListener {
            val i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_SUBJECT, "Resumen clínico")
            i.putExtra(Intent.EXTRA_TEXT, tvResumen.text.toString())
            startActivity(Intent.createChooser(i, "Exportar resumen"))
        }

        viewModel.resumen.observe(this) { estado ->
            when (estado) {
                is Resultado.Cargando -> {
                    progress.visibility = View.VISIBLE
                    tvMensaje.visibility = View.GONE
                }
                is Resultado.Exito -> {
                    progress.visibility = View.GONE
                    tvResumen.text = estado.datos
                    btnCompartir.visibility = View.VISIBLE
                    btnPdf.visibility = View.VISIBLE
                }
                is Resultado.Error -> {
                    progress.visibility = View.GONE
                    tvMensaje.text = estado.mensaje
                    tvMensaje.visibility = View.VISIBLE
                }
            }
        }

        viewModel.cargarResumen(mascotaId)
    }
}

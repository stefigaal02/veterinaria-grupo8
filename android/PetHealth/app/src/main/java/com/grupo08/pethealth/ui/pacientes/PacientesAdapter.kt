package com.grupo08.pethealth.ui.pacientes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grupo08.pethealth.R
import com.grupo08.pethealth.data.remote.MascotaResponse

class PacientesAdapter(
    private val onClick: (MascotaResponse) -> Unit
) : RecyclerView.Adapter<PacientesAdapter.ViewHolder>() {

    private var lista: List<MascotaResponse> = emptyList()

    fun actualizar(nueva: List<MascotaResponse>) {
        lista = nueva
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvEspecie: TextView = view.findViewById(R.id.tvEspecie)
        val tvPropietario: TextView = view.findViewById(R.id.tvPropietario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_paciente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val m = lista[position]
        holder.tvNombre.text = m.nombre
        holder.tvEspecie.text = "${m.especie} · ${m.raza}"
        holder.tvPropietario.text = "Propietario: ${m.propietarioNombre}"
        holder.itemView.setOnClickListener { onClick(m) }
    }

    override fun getItemCount() = lista.size
}
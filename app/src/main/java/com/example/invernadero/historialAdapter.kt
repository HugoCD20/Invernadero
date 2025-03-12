package com.example.invernadero

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class historialAdapter(private val historial: MutableList<historialTemp>) :
    RecyclerView.Adapter<historialAdapter.HistorialViewHolder>() {

    class HistorialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val temperatura: TextView = itemView.findViewById(R.id.temperatura)
        val humedad: TextView = itemView.findViewById(R.id.humedad)
        val fecha: TextView = itemView.findViewById(R.id.fecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return HistorialViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val item = historial[position]
        holder.temperatura.text = item.temperatura
        holder.humedad.text = item.humedad
        holder.fecha.text = item.fecha
    }

    override fun getItemCount() = historial.size
}
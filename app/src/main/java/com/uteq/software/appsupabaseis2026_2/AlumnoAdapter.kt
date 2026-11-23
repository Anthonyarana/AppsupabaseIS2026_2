package com.uteq.software.appsupabaseis2026_2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.uteq.software.appsupabaseis2026_2.models.Alumno

/**
 * Adaptador de la lista de alumnos.
 *
 * Mantiene el mismo formato de texto que la versión original
 * ("Nombre:", "Paralelo:", "Correo:", "Teléfono:") y separa cada alumno
 * con un espaciado vertical, sin tarjetas.
 */
class AlumnoAdapter :
    ListAdapter<Alumno, AlumnoAdapter.AlumnoViewHolder>(AlumnoDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlumnoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alumno, parent, false)
        return AlumnoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlumnoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AlumnoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtContenido: TextView = itemView.findViewById(R.id.txtContenido)

        fun bind(alumno: Alumno) {
            txtContenido.text = buildString {
                append("Nombre: ").append(alumno.nombres.ifBlank { "Sin registrar" })
                append("\nParalelo: ").append(alumno.paralelo.ifBlank { "Sin registrar" })
                append("\nCorreo: ").append(alumno.correo?.takeIf { it.isNotBlank() } ?: "Sin registrar")
                append("\nTeléfono: ").append(alumno.telefono?.takeIf { it.isNotBlank() } ?: "Sin registrar")
            }
        }
    }

    private object AlumnoDiffCallback : DiffUtil.ItemCallback<Alumno>() {
        override fun areItemsTheSame(oldItem: Alumno, newItem: Alumno): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Alumno, newItem: Alumno): Boolean =
            oldItem == newItem
    }
}

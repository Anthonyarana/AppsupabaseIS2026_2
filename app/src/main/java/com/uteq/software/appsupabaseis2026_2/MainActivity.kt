package com.uteq.software.appsupabaseis2026_2

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uteq.software.appsupabaseis2026_2.data.SupabaseManager
import com.uteq.software.appsupabaseis2026_2.models.Alumno
import com.uteq.software.appsupabaseis2026_2.utils.SupabaseErrorHandler
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerAlumnos: RecyclerView
    private lateinit var txtEstado: TextView
    private val adapter = AlumnoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerAlumnos = findViewById(R.id.recyclerAlumnos)
        txtEstado = findViewById(R.id.txtEstado)

        recyclerAlumnos.layoutManager = LinearLayoutManager(this)
        recyclerAlumnos.adapter = adapter

        consultarAlumnos()
    }

    private fun consultarAlumnos() {
        mostrarEstado("Cargando alumnos...")
        lifecycleScope.launch {
            try {
                val alumnos = SupabaseManager.client
                    .from("alumnos")
                    .select {
                        order("nombres", Order.ASCENDING)
                    }
                    .decodeList<Alumno>()

                adapter.submitList(alumnos)
                if (alumnos.isEmpty()) {
                    mostrarEstado("No se encontraron alumnos.")
                } else {
                    mostrarEstado(null)
                }
            } catch (e: Exception) {
                adapter.submitList(emptyList())
                mostrarEstado("No se pudo cargar la lista.")
                SupabaseErrorHandler.show(this@MainActivity, e)
            }
        }
    }

    /** Muestra un mensaje centrado o lo oculta cuando la lista tiene contenido. */
    private fun mostrarEstado(mensaje: String?) {
        if (mensaje == null) {
            txtEstado.text = ""
            txtEstado.visibility = TextView.GONE
        } else {
            txtEstado.text = mensaje
            txtEstado.visibility = TextView.VISIBLE
        }
    }
}

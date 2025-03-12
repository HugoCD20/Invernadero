package com.example.invernadero

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class Historial : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var historialAdapter: historialAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_historial)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val historial = mutableListOf<historialTemp>()
        val db = Firebase.firestore
        db.collection("Temperatura")
            .get()
            .addOnSuccessListener { result ->
                val historial = mutableListOf<historialTemp>() // Mover la creación de la lista aquí
                for (document in result) {
                    val temperatura = document.getString("temperatura") ?: ""
                    val humedad = document.getString("humedad") ?: ""
                    val fecha = document.getString("fecha") ?: ""

                    historial.add(historialTemp(temperatura, humedad, fecha))
                }

                // Configuración de la RecyclerView después de llenar la lista
                recyclerView = findViewById(R.id.recyclerView)
                recyclerView.layoutManager = LinearLayoutManager(this)
                historialAdapter = historialAdapter(historial)
                recyclerView.adapter = historialAdapter

                // Notificar al adaptador que los datos cambiaron
                historialAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error al obtener documentos.", exception)
            }


    }
}
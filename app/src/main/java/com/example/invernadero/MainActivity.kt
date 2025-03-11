package com.example.invernadero

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())
    private val delay = 1000L // 1000 ms

    private val actualizarDatos = object : Runnable {
        override fun run() {
            obtenerDatos()  // Llamar a la función que recupera los datos
            handler.postDelayed(this, delay) // Volver a ejecutar después del tiempo definido
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        obtenerDatos()
        handler.postDelayed(actualizarDatos, delay)

    }
    private fun obtenerDatos() {
        val temperatura: TextView = findViewById(R.id.textView2)
        val humedad: TextView = findViewById(R.id.textView3)

        val db = Firebase.firestore
        db.collection("Temperatura")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .limit(1)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val documento = result.documents[0]
                    val temp = documento.getString("temperatura") ?: "N/A"
                    val hume = documento.getString("humedad") ?: "N/A"
                    temperatura.text = "$temp°C"
                    humedad.text = "Humedad: $hume%"
                } else {
                    temperatura.text = "No hay datos disponibles"
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error al obtener los documentos.", exception)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(actualizarDatos) // Detener el ciclo cuando la actividad se cierre
    }
}
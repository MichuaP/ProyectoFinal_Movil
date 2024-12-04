package com.pmcl.proyectofinal_movil

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Desarrolladoras : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.desarrolladoras)

        // Inicializa el botón
        val btnBack = findViewById<ImageButton>(R.id.btnVolver)

        // Configura el OnClickListener
        btnBack.setOnClickListener {
            finish() // Finaliza la actividad actual
        }
    }
}

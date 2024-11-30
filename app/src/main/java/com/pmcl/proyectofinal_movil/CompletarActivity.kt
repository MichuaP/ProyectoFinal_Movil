package com.pmcl.proyectofinal_movil

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CompletarActivity : AppCompatActivity() {

    private lateinit var imageViewObjeto: ImageView
    private lateinit var completarControl: Completar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.letra_faltante)

        val btnVolver: ImageButton = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior
        }

        imageViewObjeto = findViewById(R.id.imageViewObjeto)
        completarControl = findViewById(R.id.miControlCompletar)

        // Pasar el ImageView a Completar.kt
        completarControl.cambiarImagen(imageViewObjeto)
    }
}

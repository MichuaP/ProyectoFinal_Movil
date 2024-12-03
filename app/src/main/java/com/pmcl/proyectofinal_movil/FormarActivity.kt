package com.pmcl.proyectofinal_movil

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FormarActivity : AppCompatActivity() {
    private lateinit var imageViewObjeto: ImageView
    private lateinit var formarPalabrasC: FormarPalabras

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.formar_palabras)

        val btnVolver: ImageButton = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }

        imageViewObjeto = findViewById(R.id.imageViewObjeto)
        formarPalabrasC = findViewById(R.id.controlFormar)

        formarPalabrasC.cambiarImagen(imageViewObjeto)
    }
}
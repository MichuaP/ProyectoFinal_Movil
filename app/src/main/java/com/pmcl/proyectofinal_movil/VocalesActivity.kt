package com.pmcl.proyectofinal_movil

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VocalesActivity : AppCompatActivity() {
    private lateinit var buttonA: Alfabeto
    private lateinit var buttonE: Alfabeto
    private lateinit var buttonI: Alfabeto
    private lateinit var buttonO: Alfabeto
    private lateinit var buttonU: Alfabeto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.vocales)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnVolver: ImageButton = findViewById(R.id.btnVolver)

        // Configura el Click Listener para el botón de volver
        btnVolver.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior
        }

        // Initialize buttons
        buttonA = findViewById(R.id.buttonA)
        buttonE = findViewById(R.id.buttonE)
        buttonI = findViewById(R.id.buttonI)
        buttonO = findViewById(R.id.buttonO)
        buttonU = findViewById(R.id.buttonU)

        // Assign sounds to each button
        buttonA.setSoundResource(R.raw.a)
        buttonE.setSoundResource(R.raw.e)
        buttonI.setSoundResource(R.raw.i)
        buttonO.setSoundResource(R.raw.o)
        buttonU.setSoundResource(R.raw.u)
    }
}
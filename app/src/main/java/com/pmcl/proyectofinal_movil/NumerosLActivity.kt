package com.pmcl.proyectofinal_movil

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class NumerosLActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.numeros)
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

        val numero1 = findViewById<NumerosLeccion>(R.id.uno)
        val numero2 = findViewById<NumerosLeccion>(R.id.dos)
        val numero3 = findViewById<NumerosLeccion>(R.id.tres)
        val numero4 = findViewById<NumerosLeccion>(R.id.cuatro)
        val numero5 = findViewById<NumerosLeccion>(R.id.cinco)
        val numero6 = findViewById<NumerosLeccion>(R.id.seis)
        val numero7 = findViewById<NumerosLeccion>(R.id.siete)
        val numero8 = findViewById<NumerosLeccion>(R.id.ocho)
        val numero9 = findViewById<NumerosLeccion>(R.id.nueve)
        val numero10 = findViewById<NumerosLeccion>(R.id.diez)

        numero2.setSonido(R.raw.dos)
        numero1.setSonido(R.raw.uno)
        numero3.setSonido(R.raw.tres)
        numero4.setSonido(R.raw.cuatro)
        numero5.setSonido(R.raw.cinco)
        numero6.setSonido(R.raw.seis)
        numero7.setSonido(R.raw.siete)
        numero8.setSonido(R.raw.ocho)
        numero9.setSonido(R.raw.nueve)
        numero10.setSonido(R.raw.diez)

    }

}
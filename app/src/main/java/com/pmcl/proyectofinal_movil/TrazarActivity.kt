package com.pmcl.proyectofinal_movil

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TrazarActivity : AppCompatActivity() {

    lateinit var lienzo: Trazar
    private var currentLetterIndex = 0
    private val letters = listOf(
        R.drawable.letra_a, R.drawable.letra_b, R.drawable.letra_c,
        R.drawable.letra_d, R.drawable.letra_e, R.drawable.letra_f,
        R.drawable.letra_g, R.drawable.letra_h, R.drawable.letra_i,
        R.drawable.letra_j, R.drawable.letra_k, R.drawable.letra_l,
        R.drawable.letra_m, R.drawable.letra_n, R.drawable.letra_o,
        R.drawable.letra_p, R.drawable.letra_q, R.drawable.letra_r,
        R.drawable.letra_s, R.drawable.letra_t, R.drawable.letra_u,
        R.drawable.letra_v, R.drawable.letra_w, R.drawable.letra_x,
        R.drawable.letra_y, R.drawable.letra_z
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trazar)

        val btnVolver: ImageButton = findViewById(R.id.btnVolver)

        lienzo = findViewById(R.id.lienzo)

        // Mostrar la letra actual en el lienzo
        lienzo.setBackgroundImage(letters[currentLetterIndex])

        findViewById<ImageButton>(R.id.btnSiguiente).setOnClickListener {
            if (currentLetterIndex < letters.size - 1) {
                // Limpiar el lienzo antes de pasar a la letra anterior
                lienzo.clearCanvas()
                currentLetterIndex++
                lienzo.setBackgroundImage(letters[currentLetterIndex])
            } else {
                showToast("Ya estás en la última letra")
            }
            showToast("Letra Siguiente")
        }

        findViewById<ImageButton>(R.id.btnAnterior).setOnClickListener {
            if (currentLetterIndex > 0) {
                // Limpiar el lienzo antes de pasar a la letra anterior
                lienzo.clearCanvas()
                currentLetterIndex--
                lienzo.setBackgroundImage(letters[currentLetterIndex])
            } else {
                showToast("Ya estás en la primera letra")
            }
            showToast("Letra Anterior")
        }

        // Manejar el botón de "Reset"
        findViewById<ImageButton>(R.id.btnReset).setOnClickListener {
            lienzo.clearCanvas()  // Limpiar el lienzo
            showToast("Lienzo reseteado")  // Mensaje de confirmación
        }

        // Configura el Click Listener para el botón de volver
        btnVolver.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

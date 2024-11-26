package com.pmcl.proyectofinal_movil

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TrazarNumActivity : AppCompatActivity() {

    lateinit var lienzo: Trazar
    private var currentNumberIndex = 0
    private val numbers = listOf(
        R.drawable.numero_0, R.drawable.numero_1, R.drawable.numero_2, R.drawable.numero_3,
        R.drawable.numero_4, R.drawable.numero_5, R.drawable.numero_6,
        R.drawable.numero_7, R.drawable.numero_8, R.drawable.numero_9,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trazar_num)

        val btnVolver: ImageButton = findViewById(R.id.btnVolver)

        lienzo = findViewById(R.id.lienzo)

        // Mostrar la letra actual en el lienzo
        lienzo.setBackgroundImage(numbers[currentNumberIndex])

        findViewById<ImageButton>(R.id.btnSiguiente).setOnClickListener {
            if (currentNumberIndex < numbers.size - 1) {
                // Limpiar el lienzo antes de pasar a la letra anterior
                lienzo.clearCanvas()
                currentNumberIndex++
                lienzo.setBackgroundImage(numbers[currentNumberIndex])
            } else {
                showToast("Ya estás en el último número")
            }
            showToast("Número Siguiente")
        }

        findViewById<ImageButton>(R.id.btnAnterior).setOnClickListener {
            if (currentNumberIndex > 0) {
                // Limpiar el lienzo antes de pasar al número anterior
                lienzo.clearCanvas()
                currentNumberIndex--
                lienzo.setBackgroundImage(numbers[currentNumberIndex])
            } else {
                showToast("Ya estás en el primer número")
            }
            showToast("Número Anterior")
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

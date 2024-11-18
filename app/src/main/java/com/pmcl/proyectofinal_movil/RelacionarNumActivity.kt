package com.pmcl.proyectofinal_movil

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RelacionarNumActivity : AppCompatActivity() {
    private var startX = 0f
    private var startY = 0f    //Arrays con numeros random
    val randomNum = (1..10).shuffled().take(4)
    //Array de palabras de acuerdo con los numeros random
    val numLetras = randomNum.map {
        when (it) {
            1 -> "Uno"
            2 -> "Dos"
            3 -> "Tres"
            4 -> "Cuatro"
            5 -> "Cinco"
            6 -> "Seis"
            7 -> "Siete"
            8 -> "Ocho"
            9 -> "Nueve"
            10 -> "Diez"
            else -> it.toString()
        }
    }.shuffled()

    lateinit var canvasView: CanvasRelacionar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relacionar_numeros)

        val btnBack = findViewById<ImageButton>(R.id.btnVolver)
        canvasView = findViewById(R.id.canvasView)

        // Obtén las vistas por ID y agrúpalas en una lista
        val numeroIzqViews = listOf(
            findViewById<NumRelacionar>(R.id.primero),
            findViewById<NumRelacionar>(R.id.segundo),
            findViewById<NumRelacionar>(R.id.tercero),
            findViewById<NumRelacionar>(R.id.cuarto)
        )
        val numeroDerViews = listOf(
            findViewById<NumRelacionar>(R.id.quinto),
            findViewById<NumRelacionar>(R.id.sexto),
            findViewById<NumRelacionar>(R.id.septimo),
            findViewById<NumRelacionar>(R.id.octavo)
        )
        //Asignar valores a las vistas según el array de numeros
        randomNum.forEachIndexed { index, number ->
            numeroIzqViews[index].apply {
                text = number.toString()
                setBackgroundColor(Color.LTGRAY)
            }
        }
        // Asignar letras aleatoriamente a la columna derecha
        numLetras.forEachIndexed { index, letter ->
            numeroDerViews[index].apply {
                text = letter
                setBackgroundColor(Color.LTGRAY)
            }
        }

        numeroIzqViews.forEach { boton ->
            boton.setOnTouchListener { v, event ->
                Log.d("CanvasRelacionar", "tocar canvas")

                val canvasHeight = canvasView.height
                Log.d("CanvasRelacionar", "canvasHeight: $canvasHeight")
                Log.d("CanvasRelacionar", "x: ${event.x}")
                Log.d("CanvasRelacionar", "y: ${event.y}")

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Al presionar el botón, guardamos las coordenadas de inicio
                        startX = event.x
                        // Invertimos la coordenada Y correctamente: subimos las coordenadas
                        startY = canvasHeight - event.y
                        Log.d("CanvasRelacionar", "startX: $startX, startY: $startY")
                    }
                    MotionEvent.ACTION_UP -> {
                        // Al soltar el botón, tomamos las coordenadas finales
                        val endX = event.x
                        // Invertimos la coordenada Y correctamente
                        val endY = canvasHeight - event.y
                        Log.d("CanvasRelacionar", "endX: $endX, endY: $endY")

                        // Llamar al método de la vista personalizada para dibujar la línea
                        //canvasView.addLinea(startX, startY, endX, endY)
                    }
                }
                true
            }
        }

        btnBack.setOnClickListener { finish() }

    }
}
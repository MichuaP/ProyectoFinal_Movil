package com.pmcl.proyectofinal_movil

import android.app.AlertDialog.Builder
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RelacionarNumActivity : AppCompatActivity() {
    val randomNum = (1..10).shuffled().take(4)
    private var selectedLeft: String? = null
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

    private lateinit var mediaCorrecta: MediaPlayer
    private lateinit var mediaIncorrecta: MediaPlayer
    private lateinit var mediaFinal: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.relacionar_numeros)

        val btnBack = findViewById<ImageButton>(R.id.btnVolver)

        mediaCorrecta = MediaPlayer.create(this, R.raw.correct)
        mediaIncorrecta = MediaPlayer.create(this, R.raw.wrong2)
        mediaFinal = MediaPlayer.create(this, R.raw.finish)

        var pairsFound = 0

        //Obtener las vistas por ID y agruparlas en una lista
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

        //Asignar números a la columna izquierda
        randomNum.forEachIndexed { index, number ->
            numeroIzqViews[index].apply {
                text = number.toString()
            }
        }

        //Asignar letras a la columna derecha
        numLetras.forEachIndexed { index, letter ->
            numeroDerViews[index].apply {
                text = letter
            }
        }

        //Manejar interacción entre columnas
        numeroIzqViews.forEach { btnLeft ->
            btnLeft.setOnClickListener {
                //Si el botón no se ha enlazado
                if (btnLeft.isEnabled) {
                    selectedLeft = btnLeft.text.toString()
                    btnLeft.changeColor(Color.YELLOW)
                }
            }
        }

        numeroDerViews.forEach { btnRight ->
            btnRight.setOnClickListener {
                if (btnRight.isEnabled && selectedLeft != null) {
                    val leftWord = when (selectedLeft!!.toInt()) {
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
                        else -> ""
                    }
                    if (btnRight.text.toString() == leftWord) {
                        mediaCorrecta.start()
                        Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show()
                        btnRight.changeColor(Color.GREEN)
                        btnRight.isEnabled = false //deshabilitar el botón derecho correcto
                        numeroIzqViews.find { it.text.toString() == selectedLeft }?.apply {
                            changeColor(Color.GREEN)
                            isEnabled = false //deshabilitar el botón izquierdo correcto
                        }
                        pairsFound++

                        //Revisar si ya se encontraron todos los pares
                        if (pairsFound == 4) {
                            mediaFinal.start()
                            //final juego
                            val builder = Builder(this)
                            builder
                                .setTitle("Felicidades")
                                .setIcon(R.drawable.baseline_check_24)
                                .setMessage("Haz completado el juego")
                                .setPositiveButton("Salir") { dialog, id ->
                                    finish()
                                }
                            builder.show()
                        }
                    } else {
                        mediaIncorrecta.start()
                        btnRight.changeColor(Color.RED)
                        Toast.makeText(this, "Incorrecto, intenta de nuevo", Toast.LENGTH_SHORT).show()
                    }
                    selectedLeft = null // Reinicia la selección
                }
            }
        }
        btnBack.setOnClickListener { finish() }
    }
}
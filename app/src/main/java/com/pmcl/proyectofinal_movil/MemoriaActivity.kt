package com.pmcl.proyectofinal_movil

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MemoriaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memoria_layout)
        val btnSalir = findViewById<ImageButton>(R.id.btnSalir)

        val images: MutableList<Int> = mutableListOf(
            R.drawable.foca, R.drawable.pato, R.drawable.oso,
            R.drawable.ballena, R.drawable.foca, R.drawable.pato,
            R.drawable.oso, R.drawable.ballena
        )

        val buttons = arrayOf(
            findViewById<ImageButton>(R.id.btn1),
            findViewById<ImageButton>(R.id.btn2),
            findViewById<ImageButton>(R.id.btn3),
            findViewById<ImageButton>(R.id.btn4),
            findViewById<ImageButton>(R.id.btn5),
            findViewById<ImageButton>(R.id.btn6),
            findViewById<ImageButton>(R.id.btn7),
            findViewById<ImageButton>(R.id.btn8)
        )

        val cardBack = R.drawable.letrasll
        var clicked = 0
        var turnOver = false
        var lastClicked = -1

        images.shuffle()

        val totalPairs = images.size / 2
        var pairsFound = 0

        for (i in buttons.indices) {
            buttons[i].setBackgroundResource(cardBack)
            buttons[i].tag = images[i]

            buttons[i].setOnClickListener {
                if (!turnOver && buttons[i].isClickable) {
                    buttons[i].setBackgroundResource(images[i]) //Mostrar la imagen
                    if (clicked == 0) {
                        lastClicked = i
                        clicked++
                    } else if (clicked == 1) {
                        clicked++
                        turnOver = true
                        //Desactivar todos los botones temp
                        disableAllButtons(buttons)

                        // Comprobar si coinciden
                        if (buttons[i].tag == buttons[lastClicked].tag) {
                            //Si coinciden, desactivar ambos botones
                            buttons[i].isClickable = false
                            buttons[lastClicked].isClickable = false
                            pairsFound++

                            //Revisar si ya se encontraron todos los pares
                            if (pairsFound == totalPairs) {
                                //final juego
                                Toast.makeText(this, "¡Juego completado!", Toast.LENGTH_SHORT).show()
                            }

                            turnOver = false
                            clicked = 0
                            //Reactivar los botones restantes
                            enableRemainingButtons(buttons)
                        } else {
                            //Si no coinciden, voltearlas después de un tiempo
                            val current = i
                            Handler().postDelayed({
                                buttons[current].setBackgroundResource(cardBack)
                                buttons[lastClicked].setBackgroundResource(cardBack)
                                turnOver = false
                                clicked = 0
                                //Reactivar los botones restantes
                                enableRemainingButtons(buttons)
                            }, 1000) // 1 segundo
                        }
                    }
                }

            }
            btnSalir.setOnClickListener {
                finish()
            }

        }
    }

    //desactivar temporalmente todos los botones
    private fun disableAllButtons(buttons: Array<ImageButton>) {
        for (button in buttons) {
            button.isClickable = false
        }
    }

    //habilitar botones que aún no se han emparejado
    private fun enableRemainingButtons(buttons: Array<ImageButton>) {
        for (button in buttons) {
            if (button.background.constantState == resources.getDrawable(R.drawable.letrasll).constantState) {
                button.isClickable = true
            }
        }
    }


}

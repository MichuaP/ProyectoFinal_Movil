package com.pmcl.proyectofinal_movil

import android.app.AlertDialog
import android.app.AlertDialog.*
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MemoriaActivity : AppCompatActivity() {
    private lateinit var mediaCorrecta: MediaPlayer
    private lateinit var mediaFinal: MediaPlayer
    private var nombreActividad = "Memorama"
    private lateinit var db: DBSQLite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memoria_layout)
        val btnSalir = findViewById<ImageButton>(R.id.btnSalir)
        // MediaPlayer para reproducir los sonidos
        mediaCorrecta = MediaPlayer.create(this, R.raw.correct)
        mediaFinal = MediaPlayer.create(this, R.raw.finish)

        val images: MutableList<Pair<Int, String>> = mutableListOf(
            Pair(R.drawable.focam, "foca"),
            Pair(R.drawable.patom, "pato"),
            Pair(R.drawable.osom, "oso"),
            Pair(R.drawable.conejom, "conejo"),
            Pair(R.drawable.gatom, "gato"),
            Pair(R.drawable.hipopotamom, "hipopotamo"),
            Pair(R.drawable.iguanam, "iguana"),
            Pair(R.drawable.vacam, "vaca"),
            Pair(R.drawable.nutriam, "nutria"),
            Pair(R.drawable.focal, "foca"),
            Pair(R.drawable.patol, "pato"),
            Pair(R.drawable.osol, "oso"),
            Pair(R.drawable.conejol, "conejo"),
            Pair(R.drawable.gatol, "gato"),
            Pair(R.drawable.hipol, "hipopotamo"),
            Pair(R.drawable.iguanal, "iguana"),
            Pair(R.drawable.vacal, "vaca"),
            Pair(R.drawable.nutrial, "nutria")
        )

        val buttons = arrayOf(
            findViewById<ImageButton>(R.id.btn1),
            findViewById<ImageButton>(R.id.btn2),
            findViewById<ImageButton>(R.id.btn3),
            findViewById<ImageButton>(R.id.btn4),
            findViewById<ImageButton>(R.id.btn5),
            findViewById<ImageButton>(R.id.btn6),
            findViewById<ImageButton>(R.id.btn7),
            findViewById<ImageButton>(R.id.btn8),
            findViewById<ImageButton>(R.id.btn9),
            findViewById<ImageButton>(R.id.btn10),
            findViewById<ImageButton>(R.id.btn11),
            findViewById<ImageButton>(R.id.btn12),
            findViewById<ImageButton>(R.id.btn13),
            findViewById<ImageButton>(R.id.btn14),
            findViewById<ImageButton>(R.id.btn15),
            findViewById<ImageButton>(R.id.btn16),
            findViewById<ImageButton>(R.id.btn17),
            findViewById<ImageButton>(R.id.btn18)
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
            buttons[i].tag = images[i].second

            buttons[i].setOnClickListener {
                if (!turnOver && buttons[i].isClickable) {
                    buttons[i].setBackgroundResource(images[i].first) //Mostrar la imagen
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
                            mediaCorrecta.start()
                            buttons[i].isClickable = false
                            buttons[lastClicked].isClickable = false
                            pairsFound++

                            //Revisar si ya se encontraron todos los pares
                            if (pairsFound == totalPairs) {
                                mediaFinal.start()
                                //Guardar progreso
                                guardarProgreso()
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

    private fun guardarProgreso(){
        //inicializar base de datos
        db = DBSQLite(this)
        //Obtener nombre del usuario
        var usuarioActual = SaveSharedPreference.getUserName(this)

        //Guardar progreso
        db.saveProgress(usuarioActual,nombreActividad,100,"S")
        Toast.makeText(this, "Progreso guardado", Toast.LENGTH_SHORT).show()
    }

}

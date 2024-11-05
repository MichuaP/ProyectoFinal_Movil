package com.pmcl.proyectofinal_movil

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Lecciones : AppCompatActivity() {

    // Declara el MediaPlayer como una variable de clase
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lecciones)

        val btnAlfabeto = findViewById<ImageButton>(R.id.btnAlfabeto)
        val btnNumeros = findViewById<ImageButton>(R.id.btnNumeros)
        val btnBack = findViewById<ImageButton>(R.id.btnVolver)

        // Inicializa el MediaPlayer con el archivo de audio
        mediaPlayer = MediaPlayer.create(this, R.raw.instruccabc)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.instruccnum)

        btnAlfabeto.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer.start()

            // Inicia la actividad AlfabetoActivity
            val intent = Intent(this, AlfabetoActivity::class.java)
            startActivity(intent)
        }
        btnNumeros.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer2.start()

            // Inicia la actividad AlfabetoActivity
            val intent = Intent(this, NumerosLActivity::class.java)
            startActivity(intent)
        }
        btnBack.setOnClickListener{
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libera los MediaPlayer cuando la actividad se destruye
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        if (::mediaPlayer2.isInitialized) {
            mediaPlayer2.release()
        }
    }
}

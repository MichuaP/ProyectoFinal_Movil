package com.pmcl.proyectofinal_movil

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Actividades : AppCompatActivity() {

    // Declara el MediaPlayer como una variable de clase
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividades)

        val btnOrdenarVocales = findViewById<ImageButton>(R.id.btnOrdenarVocales)
        val btnBack = findViewById<ImageButton>(R.id.btnVolver)

        // Inicializa el MediaPlayer con el archivo de audio
        mediaPlayer = MediaPlayer.create(this, R.raw.instruccordenarv)

        btnOrdenarVocales.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer.start()

            // Inicia la actividad AlfabetoActivity
            val intent = Intent(this, OrdenarVocalesActivity::class.java)
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
    }
}

package com.pmcl.proyectofinal_movil

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class InicioActivity : AppCompatActivity() {
    // Declara el MediaPlayer como una variable de clase
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)

        val btnLecciones = findViewById<ImageButton>(R.id.btnLecciones)
        val btnActividades = findViewById<ImageButton>(R.id.btnActividades)
        val btnInfo = findViewById<ImageButton>(R.id.btnInfo)
        val btnSalir = findViewById<ImageButton>(R.id.btnSalir)

        // Inicializa el MediaPlayer con el archivo de audio
        mediaPlayer = MediaPlayer.create(this, R.raw.info)

        btnSalir.setOnClickListener {
            finish()
        }
        btnInfo.setOnClickListener {
            mediaPlayer.start()
        }
        btnLecciones.setOnClickListener {
            // Inicia la actividad Lecciones
            val intent = Intent(this, Lecciones::class.java)
            startActivity(intent)
        }
        btnActividades.setOnClickListener {
            // Inicia la actividad Actividades
//            val intent = Intent(this, NumerosLActivity::class.java)
//            startActivity(intent)
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
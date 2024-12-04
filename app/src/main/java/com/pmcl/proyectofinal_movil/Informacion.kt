package com.pmcl.proyectofinal_movil

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Informacion : AppCompatActivity() {

    private lateinit var mediaPlayer1: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ayuda)

        val btnAyuda = findViewById<ImageButton>(R.id.btnInfo)
        val btnEquipo = findViewById<ImageButton>(R.id.btnDesarrolladoras)
        val btnBack = findViewById<ImageButton>(R.id.btnVolver)

        mediaPlayer1 = MediaPlayer.create(this, R.raw.help)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.team)

        btnBack.setOnClickListener {
            finish() // Finaliza la actividad actual
        }

        btnAyuda.setOnClickListener {
            val intent = Intent(this, Help::class.java)
            startActivity(intent)
            mediaPlayer1.start()
        }

        btnEquipo.setOnClickListener {
            val intent = Intent(this, Desarrolladoras::class.java)
            startActivity(intent)
            mediaPlayer2.start()
        }
    }
}
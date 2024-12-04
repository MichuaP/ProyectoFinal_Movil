package com.pmcl.proyectofinal_movil

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if(SaveSharedPreference.getUserName(this).length == 0) {
            // Inicia Login
            mediaPlayer = MediaPlayer.create(this, R.raw.instruccinicio)
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            mediaPlayer.start()
        }
        else { //usuario loggeado
            mediaPlayer = MediaPlayer.create(this, R.raw.info)
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            mediaPlayer.start()
        }

        // Termina MainActivity para que no se vuelva a mostrar
        finish()

    }
}

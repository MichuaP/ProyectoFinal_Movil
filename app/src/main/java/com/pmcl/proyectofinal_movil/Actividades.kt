package com.pmcl.proyectofinal_movil

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class Actividades : AppCompatActivity() {

    // Declara el MediaPlayer como una variable de clase
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer
    private lateinit var mediaPlayer3: MediaPlayer
    private lateinit var mediaPlayer4: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividades)

        val btnOrdenarVocales = findViewById<ImageButton>(R.id.btnOrdenarVocales)
        val btnrelacionarN = findViewById<ImageButton>(R.id.btnRelacionarN)
        val btnmemorama = findViewById<ImageButton>(R.id.btnMemorama)
        val btnCompletarPalabra = findViewById<ImageButton>(R.id.btnCompletaPalabra)
        val btnBack = findViewById<ImageButton>(R.id.btnVolver)

        // Inicializa el MediaPlayer con el archivo de audio
        mediaPlayer = MediaPlayer.create(this, R.raw.instruccordenarv)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.columnas)
        mediaPlayer3 = MediaPlayer.create(this, R.raw.memorama)
        mediaPlayer4 = MediaPlayer.create(this, R.raw.instrucccomp)

        btnOrdenarVocales.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer.start()

            // Inicia la actividad AlfabetoActivity
            val intent = Intent(this, OrdenarVocalesActivity::class.java)
            startActivity(intent)
        }
        btnrelacionarN.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer2.start()

            // Inicia la actividad AlfabetoActivity
            val intent = Intent(this, RelacionarNumActivity::class.java)
            startActivity(intent)
        }
        btnmemorama.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer3.start()

            // Inicia la actividad memorama
            val intent = Intent(this, MemoriaActivity::class.java)
            startActivity(intent)
        }
        btnCompletarPalabra.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer4.start()

            // Inicia la actividad memorama
            val intent = Intent(this, CompletarActivity::class.java)
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
        } else if (::mediaPlayer2.isInitialized) {
            mediaPlayer2.release()
        }else if (::mediaPlayer3.isInitialized) {
            mediaPlayer3.release()
        }else if (::mediaPlayer4.isInitialized) {
            mediaPlayer4.release()
        }
    }
}

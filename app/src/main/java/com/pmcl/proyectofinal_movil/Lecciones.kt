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
    private lateinit var mediaPlayer3: MediaPlayer
    private lateinit var mediaPlayer4: MediaPlayer
    private lateinit var mediaPlayer5: MediaPlayer
    private lateinit var mediaPlayer6: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lecciones)

        val btnAlfabeto = findViewById<ImageButton>(R.id.btnAlfabeto)
        val btnNumeros = findViewById<ImageButton>(R.id.btnNumeros)
        val btnVocales = findViewById<ImageButton>(R.id.btnVocales)
        val btnTrazar = findViewById<ImageButton>(R.id.btnTrazar)
        val btnAgitar = findViewById<ImageButton>(R.id.btnAgita)
        val btnTrazarNum = findViewById<ImageButton>(R.id.btnTrazarNum)
        val btnBack = findViewById<ImageButton>(R.id.btnVolver)

        // Inicializa el MediaPlayer con el archivo de audio
        mediaPlayer = MediaPlayer.create(this, R.raw.instruccabc)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.instruccnum)
        mediaPlayer3 = MediaPlayer.create(this, R.raw.instruccvocales)
        mediaPlayer4 = MediaPlayer.create(this, R.raw.instrucctrazar)
        mediaPlayer5 = MediaPlayer.create(this, R.raw.instruccagita)
        mediaPlayer6 = MediaPlayer.create(this, R.raw.instrucctraznum)

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
        btnVocales.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer3.start()

            // Inicia la actividad VocalesActivity
            val intent = Intent(this, VocalesActivity::class.java)
            startActivity(intent)
        }
        btnTrazar.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer4.start()

            // Inicia la actividad VocalesActivity
            val intent = Intent(this, TrazarActivity::class.java)
            startActivity(intent)
        }

        btnAgitar.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer5.start()

            // Inicia la actividad VocalesActivity
            val intent = Intent(this, AgitaLetrasActivity::class.java)
            startActivity(intent)
        }

        btnTrazarNum.setOnClickListener {
            mediaPlayer6.start()
            val intent = Intent(this, TrazarNumActivity::class.java)
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
        if (::mediaPlayer3.isInitialized) {
            mediaPlayer3.release()
        }
        if (::mediaPlayer4.isInitialized) {
            mediaPlayer4.release()
        }
        if (::mediaPlayer5.isInitialized) {
            mediaPlayer5.release()
        }
        if (::mediaPlayer6.isInitialized) {
            mediaPlayer6.release()
        }
    }
}

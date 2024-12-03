package com.pmcl.proyectofinal_movil

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Actividades : AppCompatActivity() {

    // Declara el MediaPlayer como una variable de clase
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer
    private lateinit var mediaPlayer3: MediaPlayer
    private lateinit var mediaPlayer4: MediaPlayer
    private lateinit var mediaPlayer5: MediaPlayer
    private lateinit var mediaPlayer6: MediaPlayer
    //Variables de la base de datos
    private lateinit var db: DBSQLite
    //Mapear actividades
    val actividadImageViewMap = mapOf(
        "Ordenar vocales" to R.id.progreso1,
        "Relacionar números" to R.id.progreso2,
        "Memorama" to R.id.progreso3,
        "Completar palabra" to R.id.progreso4,
        "Contar objetos" to R.id.progreso5,
        "Formar palabras" to R.id.progreso6
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actividades)

        // Inicializar la base de datos y la vista
        db = DBSQLite(this)
        mostrarProgresos()

        val btnOrdenarVocales = findViewById<ImageButton>(R.id.btnOrdenarVocales)
        val btnrelacionarN = findViewById<ImageButton>(R.id.btnRelacionarN)
        val btnmemorama = findViewById<ImageButton>(R.id.btnMemorama)
        val btnCompletarPalabra = findViewById<ImageButton>(R.id.btnCompletaPalabra)
        val btnContar = findViewById<ImageButton>(R.id.btnContar)
        val btnFormar = findViewById<ImageButton>(R.id.btnFormar)
        val btnBack = findViewById<ImageButton>(R.id.btnVolver)

        // Inicializa el MediaPlayer con el archivo de audio
        mediaPlayer = MediaPlayer.create(this, R.raw.instruccordenarv)
        mediaPlayer2 = MediaPlayer.create(this, R.raw.columnas)
        mediaPlayer3 = MediaPlayer.create(this, R.raw.memorama)
        mediaPlayer4 = MediaPlayer.create(this, R.raw.instrucccomp)
        mediaPlayer5 = MediaPlayer.create(this, R.raw.instrucccontar)
        mediaPlayer6 = MediaPlayer.create(this, R.raw.instrucformar)

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
        btnContar.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer5.start()

            // Inicia la actividad memorama
            val intent = Intent(this, ContarObjetosActivity::class.java)
            startActivity(intent)
        }
        btnFormar.setOnClickListener {
            // Reproduce el audio al presionar el botón
            mediaPlayer6.start()

            // Inicia la actividad formar palabras
            val intent = Intent(this, FormarActivity::class.java)
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
        }else if (::mediaPlayer5.isInitialized) {
            mediaPlayer5.release()
        }
    }

    override fun onResume() {
        super.onResume()
        mostrarProgresos()
    }

    fun mostrarProgresos(){
        //Obtener nombre del usuario
        var usuarioActual = SaveSharedPreference.getUserName(this)

        //Progresos del usuario activo
        val progresos = db.showProgress(usuarioActual)

        // Actualizar en el layout
        progresos.forEach { (actividad, estado) ->
            val imageViewId = actividadImageViewMap[actividad]
            if (imageViewId != null && estado == "S") {
                val imageView = findViewById<ImageView>(imageViewId)
                imageView.setImageResource(R.drawable.completado)
            }
        }
    }

}

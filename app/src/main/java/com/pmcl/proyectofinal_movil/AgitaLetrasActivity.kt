package com.pmcl.proyectofinal_movil

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AgitaLetrasActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var textViewLetra: TextView
    private lateinit var imageButtonObjeto1: ImageButton
    private lateinit var imageButtonObjeto2: ImageButton
    private lateinit var textViewObjeto1: TextView
    private lateinit var textViewObjeto2: TextView

    private val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private var currentLetterIndex = 0

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    // Variables para controlar el estado de la pausa
    private var isPaused = false
    private val threshold = 2  // Umbral para detectar el movimiento

    // MediaPlayer para reproducir los sonidos
    private var mediaPlayer: MediaPlayer? = null

    // Handler para controlar el retraso
    private val handler = Handler()

    // Flag para controlar cuando se puede comenzar
    private var canStartMovement = false

    // Clase para almacenar las imágenes, nombres y los sonidos
    data class LetterImages(val name1: String, val name2: String, val imageRes1: Int, val imageRes2: Int, val soundRes: Int, val soundRes1: Int, val soundRes2: Int)

    // Mapa de letras a imágenes, nombres y sonidos
    private val letterImages = mapOf(
        'A' to LetterImages("Aguacate", "Axolote", R.drawable.aguacate, R.drawable.axolote, R.raw.a, R.raw.aguacate, R.raw.axolote),
        'B' to LetterImages("Ballena", "Balón", R.drawable.ballena, R.drawable.balon, R.raw.b, R.raw.ballena, R.raw.balon),
        'C' to LetterImages("Casa", "Conejo", R.drawable.casa, R.drawable.conejo, R.raw.c, R.raw.casa, R.raw.conejo),
        'D' to LetterImages("Dado", "Dinosaurio", R.drawable.dado, R.drawable.dinosaurio, R.raw.d, R.raw.dado, R.raw.dinosaurio),
        'E' to LetterImages("Elefante", "Estrella", R.drawable.elefante, R.drawable.estrella, R.raw.e, R.raw.elefante, R.raw.estrella),
        'F' to LetterImages("Flor", "Foca", R.drawable.flor, R.drawable.foca, R.raw.f, R.raw.flor, R.raw.foca),
        'G' to LetterImages("Gato", "Guitarra", R.drawable.gato, R.drawable.guitarra, R.raw.g, R.raw.gato, R.raw.guitarra),
        'H' to LetterImages("Helado", "Hipopótamo", R.drawable.helado, R.drawable.hipopotamo, R.raw.h, R.raw.helado, R.raw.hipopotamo),
        'I' to LetterImages("Iguana", "Imán", R.drawable.iguana, R.drawable.iman, R.raw.i, R.raw.iguana, R.raw.iman),
        'J' to LetterImages("Jaguar", "Jitomate", R.drawable.jaguar, R.drawable.jitomate, R.raw.j, R.raw.jaguar, R.raw.jitomate),
        'K' to LetterImages("Kiwi", "Koala", R.drawable.kiwi, R.drawable.koala, R.raw.k, R.raw.kiwi, R.raw.koala),
        'L' to LetterImages("León", "Luna", R.drawable.leon, R.drawable.luna, R.raw.l, R.raw.leon, R.raw.luna),
        'M' to LetterImages("Mantarraya", "Manzana", R.drawable.mantarralla, R.drawable.manzana, R.raw.m, R.raw.mantarralla, R.raw.manzana),
        'N' to LetterImages("Nube", "Nutria", R.drawable.nube, R.drawable.nutria, R.raw.n, R.raw.nube, R.raw.nutria),
        'O' to LetterImages("Ojo", "Oso", R.drawable.ojo, R.drawable.oso, R.raw.o, R.raw.ojo, R.raw.oso),
        'P' to LetterImages("Pato", "Pizza", R.drawable.pato, R.drawable.pizza, R.raw.p, R.raw.pato, R.raw.pizza),
        'Q' to LetterImages("Queso", "Quetzal", R.drawable.queso, R.drawable.quetzal, R.raw.q, R.raw.queso, R.raw.quetzal),
        'R' to LetterImages("Rana", "Reloj", R.drawable.rana, R.drawable.reloj, R.raw.r, R.raw.rana, R.raw.reloj),
        'S' to LetterImages("Serpiente", "Sombrero", R.drawable.serpiente, R.drawable.sombrero, R.raw.s, R.raw.serpiente, R.raw.sombrero),
        'T' to LetterImages("Taco", "Toro", R.drawable.taco, R.drawable.toro, R.raw.t, R.raw.taco, R.raw.toro),
        'U' to LetterImages("Unicornio", "Uvas", R.drawable.unicornio, R.drawable.uvas, R.raw.u, R.raw.unicornio, R.raw.uvas),
        'V' to LetterImages("Vaca", "Ventana", R.drawable.vaca, R.drawable.ventana, R.raw.v, R.raw.vaca, R.raw.ventana),
        'W' to LetterImages("Waffle", "Wombat", R.drawable.waffle, R.drawable.wombat, R.raw.w, R.raw.waffle, R.raw.wombat),
        'X' to LetterImages("Xilófono", "Xolo", R.drawable.xilofono, R.drawable.xolo, R.raw.x, R.raw.xilofono, R.raw.xolo),
        'Y' to LetterImages("Yak", "Yoyo", R.drawable.yak, R.drawable.yoyo, R.raw.y, R.raw.yak, R.raw.yoyo),
        'Z' to LetterImages("Zebra", "Zapatos", R.drawable.zebra, R.drawable.zapatos, R.raw.z, R.raw.zebra, R.raw.zapatos)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.agita_letras)

        // Enlaza los elementos de la interfaz
        textViewLetra = findViewById(R.id.textViewLetra)
        imageButtonObjeto1 = findViewById(R.id.imageButtonObjeto1)
        imageButtonObjeto2 = findViewById(R.id.imageButtonObjeto2)
        textViewObjeto1 = findViewById(R.id.textViewObjeto1)
        textViewObjeto2 = findViewById(R.id.textViewObjeto2)

        // Enlaza el botón de volver
        val btnVolver: ImageButton = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Cierra la actividad
        }

        // Inicializamos el SensorManager y el acelerómetro
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Configuramos el retraso de 3 segundos antes de permitir cualquier movimiento o audio
        handler.postDelayed({
            canStartMovement = true // Permite empezar el movimiento después de 3 segundos
            updateLetterAndImages() // Muestra la primera letra y su imagen
        }, 3000) // 3000 milisegundos = 3 segundos

        // Configura los listeners para los ImageButton
        imageButtonObjeto1.setOnClickListener {
            val currentLetter = alphabet[currentLetterIndex]
            val letterData = letterImages[currentLetter]
            val mediaPlayer = MediaPlayer.create(this, letterData?.soundRes1 ?: 0)
            mediaPlayer.start()
        }

        imageButtonObjeto2.setOnClickListener {
            val currentLetter = alphabet[currentLetterIndex]
            val letterData = letterImages[currentLetter]
            val mediaPlayer = MediaPlayer.create(this, letterData?.soundRes2 ?: 0)
            mediaPlayer.start()
        }
    }

    override fun onResume() {
        super.onResume()
        // Registramos el listener para el acelerómetro
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Desregistramos el listener cuando la actividad entra en pausa
        sensorManager.unregisterListener(this)

        // Detenemos el reproductor de medios si está reproduciendo
        mediaPlayer?.release()
        mediaPlayer = null
    }

    // Método para cambiar la letra y las imágenes al mover el dispositivo hacia la derecha
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || !canStartMovement) return

        // Detectamos la aceleración en el eje X
        val x = event.values[0]

        // Si no estamos en pausa y el movimiento supera el umbral
        if (!isPaused) {
            // Solo permitimos el avance hacia la derecha (movimiento negativo en X)
            if (x < -threshold) {
                currentLetterIndex = (currentLetterIndex + 1) % alphabet.length
                updateLetterAndImages()
                // Mostrar un Toast indicando que se movió a la derecha
                Toast.makeText(this, "Cambio de letra", Toast.LENGTH_SHORT).show()
                isPaused = true  // Pausamos el cambio de letra hasta que el dispositivo se enderece
            }
        }

        // Esperamos a que el dispositivo se enderece (cuando el valor de X vuelva cerca de 0)
        if (Math.abs(x) < threshold) {
            isPaused = false  // Permitimos el siguiente movimiento cuando el dispositivo está "enderezado"
        }
    }

    // Método para actualizar la letra y las imágenes
    private fun updateLetterAndImages() {
        val currentLetter = alphabet[currentLetterIndex]
        val letterData = letterImages[currentLetter]

        runOnUiThread {
            textViewLetra.text = currentLetter.toString()
            imageButtonObjeto1.setImageResource(letterData?.imageRes1 ?: 0) // Sin valores predeterminados
            imageButtonObjeto2.setImageResource(letterData?.imageRes2 ?: 0) // Sin valores predeterminados
            textViewObjeto1.text = letterData?.name1 ?: "" // Sin valores predeterminados
            textViewObjeto2.text = letterData?.name2 ?: "" // Sin valores predeterminados

            // Reproduce el sonido correspondiente a la letra
            mediaPlayer?.release()  // Libera el recurso de sonido si ya se está reproduciendo
            mediaPlayer = MediaPlayer.create(this, letterData?.soundRes ?: 0)
            mediaPlayer?.start()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No es necesario implementar este método para este caso
    }
}
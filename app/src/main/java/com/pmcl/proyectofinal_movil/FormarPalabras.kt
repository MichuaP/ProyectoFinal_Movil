package com.pmcl.proyectofinal_movil

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class FormarPalabras(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var nombreActividad = "Formar palabras"
    private lateinit var db: DBSQLite
    // Dibujos
    private val pRectangulo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private val pRectanguloFaltante = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        style = Paint.Style.FILL
    }
    private val pTexto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 80f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    // Texto de las letras de opciòn
    private val pTextoOpciones = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE  // La letra siempre será blanca
        textSize = 100f  // Aumentar el tamaño de las letras
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    // Variables
    var palabraActual: String = ""
    var palabraConstruida : String = ""
    private var letrasPosibles = mutableListOf<Char>()
    private val palabrasUsadas = mutableSetOf<String>()
    private var indiceActual = 0
    // imagen de la actividad
    private lateinit var imageViewObjeto: ImageView
    private val escala = resources.displayMetrics.density

    // Espacios y márgenes
    private val espacioEntreRectangulos = 20f
    private val altoRectangulo = 150f
    private val anchoRectangulo = 120f
    private val altoRectanguloOpciones = 200f
    private val anchoRectanguloOpciones = 200f

    // MediaPlayer para los sonidos
    private var mediaPlayerCorrecto: MediaPlayer? = null
    private var mediaPlayerFinish: MediaPlayer? = null

    // Colores para las opciones de letras
    private val coloresOpciones = listOf(
        Color.parseColor("#FDED32"),  // Amarillo
        Color.parseColor("#FD4343"),  // Rojo
        Color.parseColor("#70d6ff")   // Azul pastel
    )

    // Mapa de palabras a imágenes
    private val imagenesPalabras = mapOf(
        "DADO" to R.drawable.dado, "FLOR" to R.drawable.flor, "GATO" to R.drawable.gato,
        "KIWI" to R.drawable.kiwi, "LUNA" to R.drawable.luna, "NUBE" to R.drawable.nube,
        "OSO" to R.drawable.oso, "TORO" to R.drawable.toro, "PATO" to R.drawable.pato,
        "RANA" to R.drawable.rana, "TACO" to R.drawable.taco, "UVAS" to R.drawable.uvas,
        "VACA" to R.drawable.vaca, "XOLO" to R.drawable.xolo, "YOYO" to R.drawable.yoyo,
        "YAC" to R.drawable.yak,"OJO" to R.drawable.ojo,"LEON" to R.drawable.leon
    )

    // Mapa de palabras a sonidos
    private val sonidosPalabras = mapOf(
        "DADO" to R.raw.dado, "FLOR" to R.raw.flor, "GATO" to R.raw.gato,
        "KIWI" to R.raw.kiwi, "LUNA" to R.raw.luna, "NUBE" to R.raw.nube,
        "OSO" to R.raw.oso, "TORO" to R.raw.toro, "PATO" to R.raw.pato,
        "RANA" to R.raw.rana, "TACO" to R.raw.taco, "UVAS" to R.raw.uvas,
        "VACA" to R.raw.vaca, "XOLO" to R.raw.xolo, "YOYO" to R.raw.yoyo,
        "YAC" to R.raw.yak,"OJO" to R.raw.ojo,"LEON" to R.raw.leon
    )

    // Lista de todas las palabras posibles
    private val palabras = listOf(
        "Dado", "Flor", "Gato",
        "Kiwi", "Luna", "Nube",
        "Oso", "Toro", "Pato",
        "Rana", "Taco", "Uvas",
        "Vaca", "Xolo", "Yoyo",
        "Yac","Ojo","Leon"
    )

    // Inicialización
    init {
        // Sonidos
        mediaPlayerCorrecto = MediaPlayer.create(context, R.raw.correct)
        mediaPlayerFinish = MediaPlayer.create(context, R.raw.finish)

        // Establecer la palabra y las opciones de letras
        palabraActual = obtenerPalabraAleatoria().toUpperCase() // Convertir a mayúsculas
        letrasPosibles = generarLetrasPosibles()
        // Reproducir el sonido del animal correspondiente a la palabra actual
        val sonidoAnimalResId = sonidosPalabras[palabraActual]
        sonidoAnimalResId?.let {
            MediaPlayer.create(context, it).start()
        }
    }

    // Método para cambiar la imagen asociada a la palabra
    private fun cambiarImagen() {
        val imagenResId = imagenesPalabras[palabraActual] ?: return  // Obtener la imagen desde el mapa
        imageViewObjeto.setImageResource(imagenResId)  // Actualizar la imagen en el ImageView
    }

    // Método para cambiar la imagen y las letras
    private fun cambiarPalabraYImagen() {

        //Limitar a solo 10 palabras
        if (palabrasUsadas.size == 10) {
            //fin del juego
            onCompleteGame()
            return
        }

        // Obtener una nueva palabra aleatoria que no haya sido usada antes
        palabraActual = obtenerPalabraAleatoria()
        palabraConstruida = ""
        indiceActual=0
        letrasPosibles = generarLetrasPosibles()  // Generar nuevas letras posibles

        // Cambiar la imagen asociada a la palabra actual
        cambiarImagen()

        invalidate()  // Redibujar la vista

        // Reproducir el sonido del objeto
        val sonidoAnimalResId = sonidosPalabras[palabraActual]
        sonidoAnimalResId?.let {
            MediaPlayer.create(context, it).start()
        }

    }

    // Método para generar las letras de la palabra
    private fun generarLetrasPosibles(): MutableList<Char> {
        val letras = mutableListOf<Char>()

        for(i in palabraActual.indices) {
            val letra = palabraActual[i]
            letras.add(letra)
        }
        //Colocación aleatoria
        letras.shuffle()

        return letras
    }

    // Obtener una palabra aleatoria
    private fun obtenerPalabraAleatoria(): String {
        // Filtrar las palabras que ya han sido usadas
        val palabrasDisponibles = palabras.filter { !palabrasUsadas.contains(it.toUpperCase()) }
        if (palabrasDisponibles.isEmpty()) {
            // Si ya no hay palabras disponibles, reiniciamos
            palabrasUsadas.clear()
        }

        // Elegir una palabra aleatoria de las palabras disponibles
        val palabraAleatoria = palabrasDisponibles.random().toUpperCase()
        // Marcar la palabra como usada
        palabrasUsadas.add(palabraAleatoria)

        return palabraAleatoria
    }

    // Método para cambiar la imagen asociada a la palabra
    fun cambiarImagen(imageView: ImageView) {
        this.imageViewObjeto = imageView  // referencia al ImageView
        cambiarImagen()
    }

    // Dibujar la vista
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Centrar la palabra
        val totalAnchoPalabra = (palabraActual.length * (anchoRectangulo + espacioEntreRectangulos)) - espacioEntreRectangulos
        var xPos = (width - totalAnchoPalabra) / 2f

        // Dibujar rectangulos para la palabra actual
        for (i in palabraActual.indices) {
            //Rectangulos vacíos
            val rect = RectF(xPos, 100f, xPos + anchoRectangulo, 100f + altoRectangulo)

            //Si la palabra cosntruida tiene una letra en el índice
            if(i in palabraConstruida.indices){
                canvas.drawRoundRect(rect, 15f, 15f, pRectangulo)
                canvas.drawText(palabraConstruida[i].toString(), xPos + anchoRectangulo / 2, 100f + altoRectangulo / 2 - (pTexto.descent() + pTexto.ascent()) / 2, pTexto)
            }else{
                canvas.drawRoundRect(rect, 15f, 15f, pRectanguloFaltante)
            }
            xPos += anchoRectangulo + espacioEntreRectangulos
        }

        // Dibujar las opciones de letras
        val totalAnchoOpciones = letrasPosibles.size * (anchoRectanguloOpciones + espacioEntreRectangulos) - espacioEntreRectangulos
        var opcionX = (width - totalAnchoOpciones) / 2f  // Centrar las opciones
        var yPos = 350f

        for (i in letrasPosibles.indices) {
            val rect = RectF(opcionX, yPos, opcionX + anchoRectanguloOpciones, yPos + altoRectanguloOpciones)
            val colorFondo = coloresOpciones[i % coloresOpciones.size]

            // Dibujar el rectángulo con el color de fondo
            canvas.drawRoundRect(rect, 10f * escala, 10f * escala, Paint().apply { color = colorFondo })

            // Dibujar la letra en el rectángulo
            val letraX = rect.centerX()
            val letraY = rect.centerY() - (pTextoOpciones.descent() + pTextoOpciones.ascent()) / 2
            canvas.drawText(letrasPosibles[i].toString(), letraX, letraY, pTextoOpciones)

            // Mover la posición X para la siguiente opción
            opcionX += anchoRectanguloOpciones + espacioEntreRectangulos
        }
    }

    // Detectar el toque en las letras
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y

                // Verificar si el toque fue en alguna de las letras
                if (y in 350f..(350f + altoRectanguloOpciones)) {
                    var opcionX = (width - (letrasPosibles.size * (anchoRectanguloOpciones + espacioEntreRectangulos) - espacioEntreRectangulos)) / 2f  //centrar

                    for (i in letrasPosibles.indices) {
                        val xPos = opcionX + i * (anchoRectanguloOpciones + espacioEntreRectangulos)

                        //El toque está dentro de uno de los rectángulos de las opciones
                        if(x in xPos..(xPos +  anchoRectanguloOpciones)) {
                            val letraSeleccionada = letrasPosibles[i]

                            //Si la letra seleccionada está en la posición correcta
                            if(letraSeleccionada == palabraActual.get(indiceActual)) {

                                //Dibujar la letra en el rectángulo
                                palabraConstruida+=letraSeleccionada
                                //Aumentar índice
                                indiceActual++
                                //Redibujar vista
                                invalidate()

                                //Verificar si se terminó la palabra
                                if(indiceActual>= palabraActual.length){
                                    //Sonido correcto
                                    mediaPlayerCorrecto?.start()
                                    Toast.makeText(context, "¡Correcto!", Toast.LENGTH_SHORT).show()

                                    // Retrasar el cambio de palabra y la actualización de la imagen
                                    Handler().postDelayed({
                                        // Cambiar la palabra, imagen y letras
                                        cambiarPalabraYImagen()
                                    }, 1500)  // Esperar 1.5 segundos
                                }
                            }
                        }
                    }
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    // Método para cuando el juego se completa
    private fun onCompleteGame() {
        mediaPlayerFinish?.start()  // Sonido de finalización

        //Guardar progreso
        guardarProgreso()

        // Mostrar un diálogo con la opción de reiniciar o salir
        AlertDialog.Builder(context)
            .setTitle("¡Felicidades!")
            .setMessage("¡Has completado el juego!")
            .setNegativeButton("Salir") { dialog, _ ->
                val activity = context as? AppCompatActivity
                activity?.finish()  // Salir de la actividad actual
            }
        .show()
    }

    private fun guardarProgreso(){
        //inicializar base de datos
        db = DBSQLite(context)
        //Obtener nombre del usuario
        var usuarioActual = SaveSharedPreference.getUserName(context)

        //Guardar progreso
        db.saveProgress(usuarioActual,nombreActividad,100,"S")
        Toast.makeText(context, "Progreso guardado", Toast.LENGTH_SHORT).show()
    }
}
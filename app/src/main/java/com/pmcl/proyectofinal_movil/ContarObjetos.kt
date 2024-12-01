package com.pmcl.proyectofinal_movil

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewTreeObserver
import kotlin.math.roundToInt

class ContarObjetos(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val pTextoOpciones = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 150f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    // Lista de imágenes disponibles
    private val imagenesDisponibles = mutableListOf(
        R.drawable.taco, R.drawable.aguacate, R.drawable.estrella,
        R.drawable.dado, R.drawable.flor, R.drawable.manzana,
        R.drawable.nube, R.drawable.pizza, R.drawable.queso
    )

    // Sonidos
    private fun playCorrectSound() {
        // Reproduce el sonido basado en el número correcto
        val correctSoundResId = when (cantidadImagenes) {
            0 -> R.raw.cero
            1 -> R.raw.uno
            2 -> R.raw.dos
            3 -> R.raw.tres
            4 -> R.raw.cuatro
            5 -> R.raw.cinco
            6 -> R.raw.seis
            7 -> R.raw.siete
            8 -> R.raw.ocho
            9 -> R.raw.nueve
            else -> null
        }

        correctSoundResId?.let {
            val mediaPlayer = MediaPlayer.create(context, it)
            mediaPlayer.start()
        }
    }

    // Lista de números posibles (0-9)
    private val numerosDisponibles = (0..9).toMutableList()

    // Variables para controlar el estado actual
    private var cantidadImagenes = 0
    private var imagenActualResId = R.drawable.dado // Imagen predeterminada
    private var letrasPosibles = mutableListOf<Int>()
    private var isLastQuestion = false // Flag para saber si es la última pregunta

    // Variables para controlar la visualización
    private lateinit var linearLayoutImagenes: GridLayout
    private val escala = resources.displayMetrics.density
    private val espacioEntreRectangulos = 10f // Espacio entre las imágenes (ajustado)
    private val altoRectanguloOpciones = 250f
    private val anchoRectanguloOpciones = 250f
    private val espacioEntreRectangulosAjustado = 20f

    private val numBotonesFila1 = 3  // Fila superior con 3 botones
    private val numBotonesFila2 = 2  // Fila inferior con 2 botones

    private val coloresOpciones = listOf(
        Color.parseColor("#FDED32"),
        Color.parseColor("#FD4343"),
        Color.parseColor("#70d6ff"),
        Color.parseColor("#FF5722"),
        Color.parseColor("#8BC34A")
    )

    private var opcionSeleccionada: Int? = null // Variable para almacenar la opción seleccionada

    // Función para oscurecer el color (la que proporcionaste)
    private fun darkenColor(color: Int): Int {
        val factor = 0.8f
        val r = (Color.red(color) * factor).roundToInt()
        val g = (Color.green(color) * factor).roundToInt()
        val b = (Color.blue(color) * factor).roundToInt()
        return Color.rgb(r, g, b)
    }

    private var mediaPlayerFinish: MediaPlayer? = MediaPlayer.create(context, R.raw.finish)
    private var mediaPlayerCorrect: MediaPlayer? = MediaPlayer.create(context, R.raw.correct)
    private var mediaPlayerIncorrect: MediaPlayer? = MediaPlayer.create(context, R.raw.error)

    // Inicializa el LinearLayout con las imágenes
    fun initLinearLayoutImagenes(linearLayout: GridLayout) {
        this.linearLayoutImagenes = linearLayout
        siguientePregunta() // Llama a la primera pregunta
    }

    // Muestra las imágenes en el GridLayout y las centra tanto vertical como horizontalmente
    private fun mostrarImagenes() {
        // Limpiar las imágenes previas
        linearLayoutImagenes.removeAllViews()

        // Usar un ViewTreeObserver para esperar hasta que el GridLayout haya sido medido
        linearLayoutImagenes.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                // El GridLayout ha sido medido, podemos proceder con el cálculo de tamaños
                linearLayoutImagenes.viewTreeObserver.removeOnPreDrawListener(this)

                // Calcular el tamaño de las imágenes y el espacio necesario
                val numColumns = 3 // Número de columnas que queremos (ajustable)
                val imagenWidth = (linearLayoutImagenes.width - (espacioEntreRectangulos * (numColumns - 1))) / numColumns * 0.8f // Reducimos el tamaño de las imágenes al 80%
                val imagenHeight = imagenWidth // Hacemos que la altura sea igual para que sea cuadrada

                // Configurar el GridLayout
                linearLayoutImagenes.columnCount = numColumns
                linearLayoutImagenes.rowCount = (cantidadImagenes + numColumns - 1) / numColumns // Fila dinámica

                // Calcular el total de las imágenes para obtener un espacio vacío tanto en vertical como en horizontal
                val totalAnchoImagenes = (imagenWidth * numColumns) + (espacioEntreRectangulos * (numColumns - 1))
                val totalAltoImagenes = (imagenHeight * ((cantidadImagenes + numColumns - 1) / numColumns)) + (espacioEntreRectangulos * ((cantidadImagenes + numColumns - 1) / numColumns - 1))

                // Centrar las imágenes dentro del GridLayout (horizontal y vertical)
                val paddingHorizontal = (linearLayoutImagenes.width - totalAnchoImagenes).toInt() / 2
                val paddingVertical = (linearLayoutImagenes.height - totalAltoImagenes).toInt() / 2

                linearLayoutImagenes.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)

                // Crear las imágenes y agregarlas al GridLayout
                repeat(cantidadImagenes) {
                    val imageView = ImageView(context)
                    val drawable = resources.getDrawable(imagenActualResId, null)
                    imageView.setImageDrawable(drawable)

                    // Establecer el tamaño de las imágenes
                    imageView.layoutParams = GridLayout.LayoutParams().apply {
                        width = imagenWidth.toInt()
                        height = imagenHeight.toInt()
                        // Reducir el margen entre las imágenes (menos espacio)
                        setMargins(5, 5, 5, 5) // Cambia estos valores a tu gusto
                    }
                    imageView.scaleType = ImageView.ScaleType.FIT_CENTER  // Aseguramos que la imagen no se corte y se ajuste dentro de los límites

                    // Agregar la imagen al GridLayout
                    linearLayoutImagenes.addView(imageView)
                }

                // Indicar que no es necesario redibujar
                return true
            }
        })
    }

    // Genera una nueva pregunta con una cantidad aleatoria de imágenes
    private fun siguientePregunta() {
        // Si ya no quedan números o imágenes disponibles, termina el juego
        if (isLastQuestion) {
            return // No hacer nada si ya es la última pregunta
        }

        if (numerosDisponibles.isEmpty() || imagenesDisponibles.isEmpty()) {
            isLastQuestion = true // Establece que esta es la última pregunta
            mostrarImagenes() // Muestra la última imagen
            invalidate() // Redibuja la vista
            return
        }

        // Seleccionamos un número aleatorio sin repetir
        cantidadImagenes = numerosDisponibles.random()
        numerosDisponibles.remove(cantidadImagenes)  // Eliminar el número seleccionado

        // Seleccionamos una imagen aleatoria sin repetir
        imagenActualResId = imagenesDisponibles.random()
        imagenesDisponibles.remove(imagenActualResId)  // Eliminar la imagen seleccionada

        // Generamos las opciones de respuesta, incluyendo la correcta
        letrasPosibles.clear()
        letrasPosibles.add(cantidadImagenes)
        while (letrasPosibles.size < 5) {
            val opcion = (0..9).random()
            if (opcion !in letrasPosibles) {
                letrasPosibles.add(opcion)
            }
        }
        letrasPosibles.shuffle() // Mezclamos las opciones

        mostrarImagenes() // Mostrar las imágenes correspondientes
        invalidate() // Redibujar el View para mostrar las opciones
    }

    // En el onDraw, ajustamos los colores al presionar la opción correcta
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Aumentamos el espacio entre las opciones
        val espacioEntreRectangulosAjustado = 20f // Nuevo valor para el espacio entre las opciones
        val totalAnchoFila1 = numBotonesFila1 * (anchoRectanguloOpciones + espacioEntreRectangulosAjustado) - espacioEntreRectangulosAjustado
        val totalAnchoFila2 = numBotonesFila2 * (anchoRectanguloOpciones + espacioEntreRectangulosAjustado) - espacioEntreRectangulosAjustado

        // Calculamos las posiciones X de las filas para centrar las opciones
        val paddingXFila1 = (width - totalAnchoFila1) / 2f
        val paddingXFila2 = (width - totalAnchoFila2) / 2f

        // Subimos las filas
        val paddingYFila1 = 200f  // Subimos la primera fila
        val paddingYFila2 = 200f + altoRectanguloOpciones + espacioEntreRectangulosAjustado // Subimos la segunda fila

        // Dibuja las opciones de la fila 1 (3 botones)
        var opcionX = paddingXFila1
        for (i in 0 until numBotonesFila1) {
            val rect = RectF(opcionX, paddingYFila1, opcionX + anchoRectanguloOpciones, paddingYFila1 + altoRectanguloOpciones)

            // Si la opción fue seleccionada, cambiamos el color
            val colorFondo = when {
                opcionSeleccionada == i -> darkenColor(coloresOpciones[i % coloresOpciones.size]) // Opción seleccionada, oscurecida
                letrasPosibles[i] == cantidadImagenes -> coloresOpciones[i % coloresOpciones.size] // Respuesta correcta pero no seleccionada aún
                else -> coloresOpciones[i % coloresOpciones.size] // Opción no seleccionada
            }

            // Dibujamos el rectángulo con el color adecuado
            canvas.drawRoundRect(rect, 15f * escala, 15f * escala, Paint().apply { color = colorFondo })
            canvas.drawText(letrasPosibles[i].toString(), opcionX + anchoRectanguloOpciones / 2, paddingYFila1 + altoRectanguloOpciones / 2 - (pTextoOpciones.descent() + pTextoOpciones.ascent()) / 2, pTextoOpciones)
            opcionX += anchoRectanguloOpciones + espacioEntreRectangulosAjustado
        }

        // Dibuja las opciones de la fila 2 (2 botones)
        opcionX = paddingXFila2
        for (i in numBotonesFila1 until letrasPosibles.size) {
            val rect = RectF(opcionX, paddingYFila2, opcionX + anchoRectanguloOpciones, paddingYFila2 + altoRectanguloOpciones)

            // Si la opción fue seleccionada, cambiamos el color
            val colorFondo = when {
                opcionSeleccionada == i -> darkenColor(coloresOpciones[i % coloresOpciones.size]) // Opción seleccionada, oscurecida
                letrasPosibles[i] == cantidadImagenes -> coloresOpciones[i % coloresOpciones.size] // Respuesta correcta pero no seleccionada aún
                else -> coloresOpciones[i % coloresOpciones.size] // Opción no seleccionada
            }

            // Dibujamos el rectángulo con el color adecuado
            canvas.drawRoundRect(rect, 15f * escala, 15f * escala, Paint().apply { color = colorFondo })
            canvas.drawText(letrasPosibles[i].toString(), opcionX + anchoRectanguloOpciones / 2, paddingYFila2 + altoRectanguloOpciones / 2 - (pTextoOpciones.descent() + pTextoOpciones.ascent()) / 2, pTextoOpciones)
            opcionX += anchoRectanguloOpciones + espacioEntreRectangulosAjustado
        }
    }

    // Detecta el toque sobre las opciones de respuestas (botones)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y

                // Detectar la fila superior (Fila 1)
                if (y in 200f..(200f + altoRectanguloOpciones)) {
                    var opcionX = (width - (numBotonesFila1 * (anchoRectanguloOpciones + espacioEntreRectangulos) - espacioEntreRectangulos)) / 2f
                    for (i in 0 until numBotonesFila1) {
                        val xPos = opcionX + i * (anchoRectanguloOpciones + espacioEntreRectangulos)
                        if (x in xPos..(xPos + anchoRectanguloOpciones)) {
                            opcionSeleccionada = i // Almacena la opción seleccionada
                            invalidate() // Redibuja para actualizar el color
                        }
                    }
                }

                // Detectar la fila inferior (Fila 2)
                else if (y in (200f + altoRectanguloOpciones + espacioEntreRectangulos)..(200f + altoRectanguloOpciones + espacioEntreRectangulos + altoRectanguloOpciones)) {
                    var opcionX = (width - (numBotonesFila2 * (anchoRectanguloOpciones + espacioEntreRectangulos) - espacioEntreRectangulos)) / 2f
                    for (i in numBotonesFila1 until letrasPosibles.size) {
                        val xPos = opcionX + (i - numBotonesFila1) * (anchoRectanguloOpciones + espacioEntreRectangulos)
                        if (x in xPos..(xPos + anchoRectanguloOpciones)) {
                            opcionSeleccionada = i // Almacena la opción seleccionada
                            invalidate() // Redibuja para actualizar el color
                        }
                    }
                }

                return true
            }

            MotionEvent.ACTION_UP -> {
                // Cuando el jugador deja de presionar, comprobamos si la opción seleccionada es correcta
                if (opcionSeleccionada != null) {
                    val respuestaSeleccionada = letrasPosibles[opcionSeleccionada!!]
                    if (respuestaSeleccionada == cantidadImagenes) {
                        // Respuesta correcta
                        mediaPlayerCorrect?.start()
                        Toast.makeText(context, "Correcto", Toast.LENGTH_SHORT).show()

                        // Reproducir el sonido del número correcto
                        playCorrectSound()

                        // Si es la última pregunta, no generamos más preguntas, sino que mostramos la alerta
                        if (isLastQuestion) {
                            onCompleteGame() // Finalizar el juego
                        } else {
                            siguientePregunta() // Siguiente pregunta
                        }
                    } else {
                        // Respuesta incorrecta
                        mediaPlayerIncorrect?.start()
                        Toast.makeText(context, "Incorrecto", Toast.LENGTH_SHORT).show()
                    }

                    // Después de responder, restablecemos la selección
                    opcionSeleccionada = null
                    invalidate() // Redibuja la vista para actualizar colores
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    // Método que se llama al completar el juego
    private fun onCompleteGame() {
        mediaPlayerFinish?.start()  // Sonido de finalización

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
}

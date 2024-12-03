package com.pmcl.proyectofinal_movil

import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class Completar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // Variables para la palabra, imagen y opciones de letras
    var palabraActual: String = ""
    private var palabraCompleta: String = ""
    private var letraFaltante: Char = ' '
    private var letrasPosibles = mutableListOf<Char>()
    private val palabrasUsadas = mutableSetOf<String>()
    private var nombreActividad = "Completar palabra"
    private lateinit var db: DBSQLite

    // Referencia a la imagen de la actividad
    private lateinit var imageViewObjeto: ImageView

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
        textSize = 150f  // Aumentar el tamaño de las letras (ajustar este valor para hacerlo más grande)
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    // Definir un nuevo Paint para la letra correcta (color rojo)
    private val pTextoCorrecto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED  // Color rojo
        textSize = 80f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private val escala = resources.displayMetrics.density

    // Espacios y márgenes
    private val espacioEntreRectangulos = 20f
    private val altoRectangulo = 150f
    private val anchoRectangulo = 120f
    private val altoRectanguloOpciones = 250f  // Aumentar la altura de las opciones de letras
    private val anchoRectanguloOpciones = 250f  // Aumentar el ancho de las opciones de letras


    // MediaPlayer para los sonidos
    private var mediaPlayerCorrecto: MediaPlayer? = null
    private var mediaPlayerIncorrecto: MediaPlayer? = null
    private var mediaPlayerFinish: MediaPlayer? = null

    // Colores para las opciones de letras
    private val coloresOpciones = listOf(
        Color.parseColor("#FDED32"),  // Amarillo
        Color.parseColor("#FD4343"),  // Rojo
        Color.parseColor("#70d6ff")   // Azul pastel
    )

    private fun darkenColor(color: Int): Int {
        val factor = 0.8f
        val r = (Color.red(color) * factor).roundToInt()
        val g = (Color.green(color) * factor).roundToInt()
        val b = (Color.blue(color) * factor).roundToInt()
        return Color.rgb(r, g, b)
    }

    // Mapa de palabras a imágenes
    private val imagenesPalabras = mapOf(
        "AXOLOTE" to R.drawable.axolote,
        "BALLENA" to R.drawable.ballena,
        "BALÓN" to R.drawable.balon,
        "CASA" to R.drawable.casa,
        "CONEJO" to R.drawable.conejo,
        "DADO" to R.drawable.dado,
        "FLOR" to R.drawable.flor,
        "FOCA" to R.drawable.foca,
        "GATO" to R.drawable.gato,
        "HELADO" to R.drawable.helado,
        "IGUANA" to R.drawable.iguana,
        "IMÁN" to R.drawable.iman,
        "JAGUAR" to R.drawable.jaguar,
        "KIWI" to R.drawable.kiwi,
        "KOALA" to R.drawable.koala,
        "LEÓN" to R.drawable.leon,
        "LUNA" to R.drawable.luna,
        "MANZANA" to R.drawable.manzana,
        "NUBE" to R.drawable.nube,
        "NUTRIA" to R.drawable.nutria,
        "OJO" to R.drawable.ojo,
        "OSO" to R.drawable.oso,
        "PATO" to R.drawable.pato,
        "PIZZA" to R.drawable.pizza,
        "QUESO" to R.drawable.queso,
        "QUETZAL" to R.drawable.quetzal,
        "RANA" to R.drawable.rana,
        "RELOJ" to R.drawable.reloj,
        "TACO" to R.drawable.taco,
        "TORO" to R.drawable.toro,
        "UVAS" to R.drawable.uvas,
        "VACA" to R.drawable.vaca,
        "VENTANA" to R.drawable.ventana,
        "WAFFLE" to R.drawable.waffle,
        "WOMBAT" to R.drawable.wombat,
        "XOLO" to R.drawable.xolo,
        "YAK" to R.drawable.yak,
        "YOYO" to R.drawable.yoyo,
        "ZEBRA" to R.drawable.zebra,
        "ZAPATOS" to R.drawable.zapatos,
    )

    // Mapa de palabras a sonidos
    private val sonidosPalabras = mapOf(
        "AXOLOTE" to R.raw.axolote,
        "BALLENA" to R.raw.ballena,
        "BALÓN" to R.raw.balon,
        "CASA" to R.raw.casa,
        "CONEJO" to R.raw.conejo,
        "DADO" to R.raw.dado,
        "FLOR" to R.raw.flor,
        "FOCA" to R.raw.foca,
        "GATO" to R.raw.gato,
        "HELADO" to R.raw.helado,
        "IGUANA" to R.raw.iguana,
        "IMÁN" to R.raw.iman,
        "JAGUAR" to R.raw.jaguar,
        "KIWI" to R.raw.kiwi,
        "KOALA" to R.raw.koala,
        "LEÓN" to R.raw.leon,
        "LUNA" to R.raw.luna,
        "MANZANA" to R.raw.manzana,
        "NUBE" to R.raw.nube,
        "NUTRIA" to R.raw.nutria,
        "OJO" to R.raw.ojo,
        "OSO" to R.raw.oso,
        "PATO" to R.raw.pato,
        "PIZZA" to R.raw.pizza,
        "QUESO" to R.raw.queso,
        "QUETZAL" to R.raw.quetzal,
        "RANA" to R.raw.rana,
        "RELOJ" to R.raw.reloj,
        "TACO" to R.raw.taco,
        "TORO" to R.raw.toro,
        "UVAS" to R.raw.uvas,
        "VACA" to R.raw.vaca,
        "VENTANA" to R.raw.ventana,
        "WAFFLE" to R.raw.waffle,
        "WOMBAT" to R.raw.wombat,
        "XOLO" to R.raw.xolo,
        "YAK" to R.raw.yak,
        "YOYO" to R.raw.yoyo,
        "ZEBRA" to R.raw.zebra,
        "ZAPATOS" to R.raw.zapatos
    )

    // Lista de todas las palabras posibles
    private val palabras = listOf(
        "Axolote", "Ballena", "Balón", "Casa", "Conejo", "Dado",
        "Flor", "Foca", "Gato", "Helado",
        "Iguana", "Imán", "Jaguar", "Kiwi", "Koala", "León", "Luna",
        "Manzana", "Nube", "Nutria", "Ojo", "Oso", "Pato", "Pizza", "Queso", "Quetzal", "Rana",
        "Reloj", "Taco", "Toro", "Uvas", "Vaca", "Ventana",
        "Waffle", "Wombat", "Xolo", "Yak", "Yoyo", "Zebra", "Zapatos"
    )

    // Inicialización de la palabra y las opciones
    init {
        // Establecer los sonidos
        mediaPlayerCorrecto = MediaPlayer.create(context, R.raw.correct)
        mediaPlayerIncorrecto = MediaPlayer.create(context, R.raw.error)
        mediaPlayerFinish = MediaPlayer.create(context, R.raw.finish)

        // Establecer la palabra y las opciones de letras
        palabraActual = obtenerPalabraAleatoria().toUpperCase() // Convertir a mayúsculas
        palabraCompleta = palabraActual
        letraFaltante = palabraActual.random()  // Seleccionamos una letra faltante aleatoria
        letrasPosibles = generarLetrasPosibles()
    }

    // Método para cambiar la imagen asociada a la palabra
    private fun cambiarImagen() {
        val imagenResId = imagenesPalabras[palabraActual] ?: return  // Obtener la imagen desde el mapa
        imageViewObjeto.setImageResource(imagenResId)  // Actualizar la imagen en el ImageView
    }

    // Método para cambiar la palabra, la imagen y las letras
    private fun cambiarPalabraYImagen() {
        // Comprobar si ya se han completado todas las palabras
        if (palabrasUsadas.size == palabras.size) {
            // Si todas las palabras se han usado, mostramos la alerta de finalización
            onCompleteGame()
            return // No cambiamos de palabra
        }

        // Obtener una nueva palabra aleatoria que no haya sido usada antes
        palabraActual = obtenerPalabraAleatoria()  // Aquí es donde se obtiene la nueva palabra
        letraFaltante = palabraActual.random()  // Nueva letra faltante
        letrasPosibles = generarLetrasPosibles()  // Generar nuevas letras posibles

        // Reiniciar la letra seleccionada correctamente
        letraSeleccionadaCorrecta = ' '  // Restablecer la letra correcta

        // Cambiar la imagen asociada a la palabra actual
        cambiarImagen()

        invalidate()  // Redibujar la vista
    }

    // Método para generar las letras posibles
    private fun generarLetrasPosibles(): MutableList<Char> {
        val letras = mutableListOf<Char>()
        letras.add(letraFaltante)  // Añadir la letra faltante

        // Añadir dos letras aleatorias incorrectas
        while (letras.size < 3) {
            val letraAleatoria = ('A'..'Z').random()
            if (!letras.contains(letraAleatoria)) {
                letras.add(letraAleatoria)
            }
        }

        // Barajar las letras para que las opciones sean aleatorias
        letras.shuffle()

        return letras
    }

    // Método para obtener una palabra aleatoria
    private fun obtenerPalabraAleatoria(): String {
        // Filtrar las palabras que ya han sido usadas
        val palabrasDisponibles = palabras.filter { !palabrasUsadas.contains(it.toUpperCase()) }

        if (palabrasDisponibles.isEmpty()) {
            // Si ya no hay palabras disponibles, reiniciamos el conjunto de palabras usadas
            palabrasUsadas.clear()
        }

        // Elegir una palabra aleatoria de las palabras disponibles
        val palabraAleatoria = palabrasDisponibles.random().toUpperCase()

        // Marcar la palabra como usada
        palabrasUsadas.add(palabraAleatoria)

        return palabraAleatoria
    }

    // Método para cambiar la imagen asociada a la palabra (llamarlo desde la actividad)
    fun cambiarImagen(imageView: ImageView) {
        this.imageViewObjeto = imageView  // Establecemos la referencia al ImageView
        cambiarImagen()  // Cambiamos la imagen con el método definido
    }

    // Variable para almacenar la letra seleccionada correctamente
    private var letraSeleccionadaCorrecta: Char = ' '

    // Dibujar la vista con las letras y opciones
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Centrar la palabra
        val totalAnchoPalabra = (palabraActual.length * (anchoRectangulo + espacioEntreRectangulos)) - espacioEntreRectangulos
        var xPos = (width - totalAnchoPalabra) / 2f  // Calculamos la posición X para centrar la palabra

        // Dibujar la palabra con la letra faltante
        for (i in palabraActual.indices) {
            val letra = palabraActual[i]
            val rect = RectF(xPos, 100f, xPos + anchoRectangulo, 100f + altoRectangulo)

            if (letra == letraFaltante) {
                // Dibuja el rectángulo gris para la letra faltante
                canvas.drawRoundRect(rect, 15f, 15f, pRectanguloFaltante)
            } else {
                // Dibuja el rectángulo blanco con la letra
                canvas.drawRoundRect(rect, 15f, 15f, pRectangulo)

                // Si la letra es la que se acaba de completar, usar el color rojo
                if (letra == letraSeleccionadaCorrecta) {
                    canvas.drawText(letra.toString(), xPos + anchoRectangulo / 2, 100f + altoRectangulo / 2 - (pTextoCorrecto.descent() + pTextoCorrecto.ascent()) / 2, pTextoCorrecto)
                } else {
                    // Si no es la letra completada, usar el color normal (negro)
                    canvas.drawText(letra.toString(), xPos + anchoRectangulo / 2, 100f + altoRectangulo / 2 - (pTexto.descent() + pTexto.ascent()) / 2, pTexto)
                }
            }
            xPos += anchoRectangulo + espacioEntreRectangulos
        }

        // Dibujar las opciones de letras con el color de fondo y la letra en blanco
        val totalAnchoOpciones = letrasPosibles.size * (anchoRectanguloOpciones + espacioEntreRectangulos) - espacioEntreRectangulos
        var opcionX = (width - totalAnchoOpciones) / 2f  // Centrar las opciones de letras horizontalmente
        var yPos = 350f  // Posición Y para las opciones de letras

        for (i in letrasPosibles.indices) {
            // Usamos los nuevos tamaños para los rectángulos
            val rect = RectF(opcionX, yPos, opcionX + anchoRectanguloOpciones, yPos + altoRectanguloOpciones)

            // Determinamos el color de fondo
            val colorFondo = if (letraSeleccionadaCorrecta == letrasPosibles[i]) {
                // Si la letra seleccionada es la correcta, usamos el color oscurecido
                darkenColor(coloresOpciones[i % coloresOpciones.size])
            } else {
                // Si no es la correcta, usamos el color original
                coloresOpciones[i % coloresOpciones.size]
            }

            // Dibujar el rectángulo con el color de fondo
            canvas.drawRoundRect(rect, 15f * escala, 15f * escala, Paint().apply { color = colorFondo })

            // Dibujar la letra en blanco (con el tamaño grande)
            canvas.drawText(letrasPosibles[i].toString(), opcionX + anchoRectanguloOpciones / 2, yPos + altoRectanguloOpciones / 2 - (pTextoOpciones.descent() + pTextoOpciones.ascent()) / 2, pTextoOpciones)

            // Mover la posición X para la siguiente opción
            opcionX += anchoRectanguloOpciones + espacioEntreRectangulos
        }
    }

    // Detectar el toque en las opciones de letras
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y

                // Verificar si el toque fue en alguna de las opciones de letras
                if (y in 350f..(350f + altoRectanguloOpciones)) {
                    var opcionX = (width - (letrasPosibles.size * (anchoRectanguloOpciones + espacioEntreRectangulos) - espacioEntreRectangulos)) / 2f  // Calculamos el desplazamiento en X para centrar las letras

                    for (i in letrasPosibles.indices) {
                        val xPos = opcionX + i * (anchoRectanguloOpciones + espacioEntreRectangulos)

                        // Verificar si el toque está dentro de uno de los rectángulos de las letras
                        if (x in xPos..(xPos + anchoRectanguloOpciones)) {
                            val letraSeleccionada = letrasPosibles[i]
                            // Si la letra seleccionada es correcta
                            if (letraSeleccionada == letraFaltante) {
                                // Reproducir sonido correcto
                                mediaPlayerCorrecto?.start()
                                Toast.makeText(context, "¡Correcto!", Toast.LENGTH_SHORT).show()

                                // Guardar la letra que fue completada correctamente
                                letraSeleccionadaCorrecta = letraSeleccionada

                                // Actualizar la palabra con la letra seleccionada
                                palabraActual = palabraActual.replaceFirst(letraFaltante, letraSeleccionada)
                                letraFaltante = ' '  // Eliminar la letra faltante una vez completada

                                invalidate()  // Redibujar la vista para reflejar la letra seleccionada

                                // Reproducir el sonido del animal correspondiente a la palabra actual
                                val sonidoAnimalResId = sonidosPalabras[palabraActual]
                                sonidoAnimalResId?.let {
                                    MediaPlayer.create(context, it).start()
                                }

                                // Verificar si todas las palabras han sido completadas
                                if (palabrasUsadas.size == palabras.size) {
                                    // Si todas las palabras se han completado, mostrar la alerta
                                    Handler().postDelayed({
                                        onCompleteGame()
                                    }, 1500)  // Esperamos 1.5 segundos antes de mostrar la alerta
                                } else {
                                    // Retrasar el cambio de palabra y la actualización de la imagen
                                    Handler().postDelayed({
                                        // Cambiar la palabra, imagen y letras
                                        cambiarPalabraYImagen()
                                    }, 1500)  // Esperar 1.5 segundos
                                }
                            } else {
                                // Reproducir sonido incorrecto
                                mediaPlayerIncorrecto?.start()
                                Toast.makeText(context, "Intenta de nuevo", Toast.LENGTH_SHORT).show()
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

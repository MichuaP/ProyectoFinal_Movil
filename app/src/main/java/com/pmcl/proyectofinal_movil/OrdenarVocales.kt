package com.pmcl.proyectofinal_movil

import android.content.Context
import android.graphics.*
import android.media.MediaPlayer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class OrdenarVocales(context: Context, attrs: AttributeSet) : View(context, attrs), onSelectObject {

    private val paint = Paint()
    private val rects = mutableListOf<RectF>()
    private val targetRects = mutableListOf<RectF>()
    private val vocalesNames = mutableListOf<String>()
    private val randomizedNames = mutableListOf<String>()
    private val rectColors = mutableListOf<Int>()
    private var draggingIndex: Int? = null
    private var originalPosition: PointF? = null
    private var lockedRects = mutableSetOf<Int>()
    private var allCorrect = false

    private var mediaCorrecta: MediaPlayer
    private var mediaFinal: MediaPlayer
    private var mediaError: MediaPlayer

    companion object {
        private val pastelColors = intArrayOf(
            Color.parseColor("#FDED32"),
            Color.parseColor("#FD4343"),
            Color.parseColor("#70d6ff")
        )
    }

    private fun getRandomPastelColor(): Int {
        return pastelColors.random()
    }

    init {
        val offsetX = -50f // Desplazamiento hacia la derecha

        // Posiciones de destino (arriba: 3 rectángulos)
        targetRects.add(RectF(100f + offsetX, 100f, 400f + offsetX, 400f))  // Destino 1
        targetRects.add(RectF(450f + offsetX, 100f, 750f + offsetX, 400f))  // Destino 2
        targetRects.add(RectF(800f + offsetX, 100f, 1100f + offsetX, 400f))  // Destino 3

        // Posiciones de destino (abajo: 2 rectángulos)
        targetRects.add(RectF(225f + offsetX, 500f, 525f + offsetX, 800f))  // Destino 4
        targetRects.add(RectF(575f + offsetX, 500f, 875f + offsetX, 800f))  // Destino 5

        // Posiciones iniciales de las vocales (3 abajo y 2 en una fila más abajo)
        rects.add(RectF(100f + offsetX, 900f, 400f + offsetX, 1200f))  // Vocal 1
        rects.add(RectF(450f + offsetX, 900f, 750f + offsetX, 1200f))  // Vocal 2
        rects.add(RectF(800f + offsetX, 900f, 1100f + offsetX, 1200f))  // Vocal 3

        rects.add(RectF(225f + offsetX, 1300f, 525f + offsetX, 1600f)) // Vocal 4
        rects.add(RectF(575f + offsetX, 1300f, 875f + offsetX, 1600f)) // Vocal 5


        vocalesNames.addAll(listOf("A", "E", "I", "O", "U"))
        randomizedNames.addAll(vocalesNames.shuffled())
        repeat(rects.size) {
            rectColors.add(getRandomPastelColor())
        }

        paint.style = Paint.Style.FILL
        paint.textSize = 160f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.CENTER

        // Inicializa los MediaPlayers
        mediaCorrecta = MediaPlayer.create(context, R.raw.correct)
        mediaFinal = MediaPlayer.create(context, R.raw.finish)
        mediaError = MediaPlayer.create(context, R.raw.error)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.GRAY
        for (target in targetRects) {
            canvas.drawRoundRect(target, 50f, 50f, paint)
        }

        for (i in rects.indices) {
            paint.color = rectColors[i]
            canvas.drawRoundRect(rects[i], 50f, 50f, paint)

            paint.color = Color.WHITE
            val centerX = rects[i].centerX()
            val centerY = rects[i].centerY() - (paint.descent() + paint.ascent()) / 2
            canvas.drawText(randomizedNames[i], centerX, centerY, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (allCorrect) return true // No permitir más interacciones si ya está completo

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                draggingIndex = getTouchedRectIndex(event.x, event.y)
                draggingIndex?.let { index ->
                    // No permitir mover si ya está bloqueado
                    if (lockedRects.contains(index)) {
                        draggingIndex = null
                        return@let
                    }
                    // Guardar la posición original
                    originalPosition = PointF(rects[index].left, rects[index].top)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                draggingIndex?.let { index ->
                    // Mover la vocal mientras se arrastra
                    val left = event.x - rects[index].width() / 2
                    val top = event.y - rects[index].height() / 2
                    rects[index].offsetTo(left, top)
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                draggingIndex?.let { index ->
                    // Validar si se soltó en una posición correcta
                    val targetIndex = vocalesNames.indexOf(randomizedNames[index])
                    if (validateDrop(rects[index], targetRects[targetIndex])) {
                        // Posicionar el rectángulo en el centro de su posición de destino
                        val target = targetRects[targetIndex]
                        val left = target.left + (target.width() - rects[index].width()) / 2
                        val top = target.top + (target.height() - rects[index].height()) / 2
                        rects[index].offsetTo(left, top)
                        lockedRects.add(index) // Bloquear el rectángulo correctamente colocado

                        // Reproducir el sonido cuando se coloca correctamente
                        onLetterCorrect()
                    } else {
                        // Regresar la vocal a su posición original
                        originalPosition?.let {
                            rects[index].offsetTo(it.x, it.y)
                        }
                        mediaError.start()
                    }
                    // Verificar si todas las vocales están bien organizados
                    allCorrect = rects.indices.all { validateDrop(rects[it], targetRects[vocalesNames.indexOf(randomizedNames[it])]) }
                    invalidate()

                    // Si todos los elementos están en su lugar, mostrar el AlertDialog
                    if (allCorrect) {
                        onCompleteGame() // Método para mostrar el AlertDialog
                    }
                }
                draggingIndex = null
            }
        }
        return true
    }

    private fun getTouchedRectIndex(x: Float, y: Float): Int? {
        rects.forEachIndexed { index, rect ->
            if (rect.contains(x, y)) {
                return index
            }
        }
        return null
    }

    private fun onLetterCorrect() {
        // Reproducir el sonido de la letra correcta
        mediaCorrecta.start()
    }

    private fun onCompleteGame() {
        // Reproducir el sonido final
        mediaFinal.start()

        // Mostrar el AlertDialog al completar el juego
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Felicidades")
            .setIcon(R.drawable.baseline_check_24)
            .setMessage("Haz completado el juego")
            .setPositiveButton("Salir") { dialog, id ->
                // Finalizar la actividad o hacer lo que desees
                (context as? AppCompatActivity)?.finish()
            }
        builder.show()
    }

    override fun validateDrop(viewRect: RectF, targetRect: RectF): Boolean {
        val tolerance = 50f
        val xCorrect = Math.abs(viewRect.left - targetRect.left) < tolerance
        val yCorrect = Math.abs(viewRect.top - targetRect.top) < tolerance
        return xCorrect && yCorrect
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mediaCorrecta.release()
        mediaFinal.release()
        mediaError.release()
    }
}

interface onSelectObject {
    fun validateDrop(viewRect: RectF, targetRect: RectF): Boolean
}
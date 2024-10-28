package com.pmcl.proyectofinal_movil

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.media.MediaPlayer
import android.util.AttributeSet
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

class NumerosLeccion : AppCompatButton {
    var escala: Float = resources.displayMetrics.density
    var pFondo: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var pFondoPress: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var pTexto: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    val colores = arrayOf(
        Color.parseColor("#70ff99"), //verde
        Color.parseColor("#ff7070"), //rojo
        Color.parseColor("#fd9432"), //naranja
        Color.parseColor("#FDED32"), //amarillo
        Color.parseColor("#70d6ff"), //azul
        Color.parseColor("#ff70ff"), //rosa
        Color.parseColor("#ff70d2"), //fiusha
    )

    private var mediaPlayer: MediaPlayer? = null
    private var sonidoId: Int = 0

    constructor(context: Context) : super(context) {
        inicializa()
        configurarListener()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inicializa()
        configurarListener()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context!!, attrs, defStyleAttr) {
        inicializa()
        configurarListener()
    }

    private fun inicializa() {
        pFondo.style = Paint.Style.FILL
        pFondoPress.color = Color.YELLOW
        pFondoPress.style = Paint.Style.FILL
        pTexto.color = Color.WHITE
        pTexto.textSize = 100f
        mediaPlayer = MediaPlayer.create(context, R.raw.uno)
        val color = colores.random() //color aleatorio del array
        pFondo.color = color
        pTexto.typeface = Typeface.DEFAULT_BOLD
    }

    private fun configurarListener() {
        setOnClickListener {
            Toast.makeText(context, "Presionaste: $text", Toast.LENGTH_SHORT).show()
            //se libera el media player
            mediaPlayer?.release()
            //Se inicializa mediaplayer con el audio establecido en main
            mediaPlayer = MediaPlayer.create(context, sonidoId)
            //Reproducir el sonido
            mediaPlayer?.start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun setSonido(resId: Int) {
        sonidoId = resId
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(width, height) / 2f
        val paint = if (this.isPressed) pFondoPress else pFondo
        if (isPressed) {
            canvas.drawCircle(centerX, centerY, radius, pFondoPress)
        } else {
            canvas.drawCircle(centerX, centerY, radius, pFondo)
        }

        //canvas.drawCircle(centerX, centerY, radius, paint)

        val xPos = centerX - (pTexto.measureText(text.toString()) / 2)
        val yPos = centerY - ((pTexto.descent() + pTexto.ascent()) / 2)
        canvas.drawText(text.toString(), xPos, yPos, pTexto)
    }
}
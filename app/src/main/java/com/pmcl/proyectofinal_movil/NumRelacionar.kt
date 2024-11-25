package com.pmcl.proyectofinal_movil

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.media.MediaPlayer
import android.util.AttributeSet
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import kotlin.math.roundToInt

class NumRelacionar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : AppCompatButton(context, attrs) {

    private val escala = resources.displayMetrics.density
    private var currentColor = getRandomPastelColor()
    private val pFondo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = currentColor
        style = Paint.Style.FILL
    }
    private val pTexto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 30f * escala
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    private val cornerRadius = 15f * escala
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        private val pastelColors = intArrayOf(
            Color.parseColor("#ff7070"), //rojo
            Color.parseColor("#fd9432"), //naranja
            Color.parseColor("#70d6ff"), //azul
            Color.parseColor("#ff70ff"), //rosa
            Color.parseColor("#ff70d2"), //fiusha
        )
    }

    private fun getRandomPastelColor(): Int {
        return pastelColors.random()
    }

    private fun darkenColor(color: Int): Int {
        val factor = 0.8f
        val r = (Color.red(color) * factor).roundToInt()
        val g = (Color.green(color) * factor).roundToInt()
        val b = (Color.blue(color) * factor).roundToInt()
        return Color.rgb(r, g, b)
    }

    fun changeColor(newColor: Int) {
        currentColor = newColor
        pFondo.color = newColor
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, pFondo)

        val textX = width / 2f
        val textY = height / 2f - (pTexto.descent() + pTexto.ascent()) / 2
        canvas.drawText(text.toString(), textX, textY, pTexto)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}
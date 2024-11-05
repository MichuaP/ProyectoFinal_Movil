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

class Alfabeto @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatButton(context, attrs) {

    private val escala = resources.displayMetrics.density
    private val pFondo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = getRandomPastelColor()
        style = Paint.Style.FILL
    }
    private val pFondoPress = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = darkenColor(pFondo.color)
        style = Paint.Style.FILL
    }
    private val pTexto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 100f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }
    private val cornerRadius = 15f * escala
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        private val pastelColors = intArrayOf(
            Color.parseColor("#FDED32"), // Amarillo
            Color.parseColor("#FD4343"), // Rojo
            Color.parseColor("#70d6ff")  // Azul pastel
        )
    }

    fun setSoundResource(soundResId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, soundResId)
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())

        if (isPressed) {
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, pFondoPress)
            showToast()
            if (mediaPlayer != null && mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
            }
        } else {
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, pFondo)
        }

        val textX = width / 2f
        val textY = height / 2f - (pTexto.descent() + pTexto.ascent()) / 2
        canvas.drawText(text.toString(), textX, textY, pTexto)
    }

    private fun showToast() {
        Toast.makeText(context, "Letra: ${text.toString()}", Toast.LENGTH_SHORT).show()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

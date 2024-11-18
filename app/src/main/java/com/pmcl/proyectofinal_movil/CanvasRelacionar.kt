package com.pmcl.proyectofinal_movil

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.graphics.*
import android.util.Log

class CanvasRelacionar(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    //almacenar las coordenadas de las líneas a dibujar
    val lineasDibujadas = mutableListOf<Pair<Pair<Float, Float>, Pair<Float, Float>>>()

    //guardar las coordenadas de las líneas
    fun addLinea(startX: Float, startY: Float, endX: Float, endY: Float) {
        lineasDibujadas.add(Pair(Pair(startX, startY), Pair(endX, endY)))
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d("CanvasRelacionar", "Canvas Width: ${canvas.width}, Canvas Height: ${canvas.height}")
        val paint = Paint().apply {
            color = Color.parseColor("#70ff99")
            strokeWidth = 15f
        }

        //Dibuja todas las líneas
        for (line in lineasDibujadas) {
            val (start, end) = line
            Log.d("CanvasRelacionar", "Drawing line from ${start.first},${start.second} to ${end.first},${end.second}")
            canvas.drawLine(start.first, start.second, end.first, end.second, paint)
        }
    }
}

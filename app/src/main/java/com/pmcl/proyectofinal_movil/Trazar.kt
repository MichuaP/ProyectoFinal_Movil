package com.pmcl.proyectofinal_movil

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat

class Trazar(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var drawColor: Int = 0
    private var path: Path = Path()
    private var paint: Paint = Paint()
    private var paintText: Paint = Paint()
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    // Lista de formas dibujadas
    private val shapes = mutableListOf<Pair<String, Any>>()

    // Imagen de fondo
    private var backgroundImage: Bitmap? = null

    init {
        inicializa()
    }

    private fun inicializa() {
        // Inicializar el color de dibujo
        drawColor = ResourcesCompat.getColor(resources, R.color.colorPrint, null)

        // Configuración del paint para dibujar
        paint.color = drawColor
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 12f

        // Configuración para el texto (si es necesario)
        paintText.color = Color.argb(255, 100, 100, 100)
        paintText.textSize = 40f
    }

    // Método para actualizar la imagen de fondo
    fun setBackgroundImage(imageResId: Int) {
        // Cargar la imagen de recursos
        val originalBitmap = BitmapFactory.decodeResource(context.resources, imageResId)

        // Definir el tamaño deseado (70% de su tamaño original para hacerla un poco más pequeña)
        val scaleFactor = 0.2f
        val scaledWidth = (originalBitmap.width * scaleFactor).toInt()
        val scaledHeight = (originalBitmap.height * scaleFactor).toInt()

        // Escalar la imagen
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, scaledWidth, scaledHeight, false)

        // Asignar la imagen escalada al fondo
        backgroundImage = scaledBitmap
        invalidate()  // Redibujar el lienzo para mostrar la nueva imagen de fondo
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Dibujar la imagen de fondo centrada
        backgroundImage?.let {
            // Calcular la posición para centrar la imagen
            val centerX = (width - it.width) / 2f
            val centerY = (height - it.height) / 2f

            // Dibujar la imagen escalada en el centro del lienzo
            canvas.drawBitmap(it, centerX, centerY, null)
        }

        // Redibuja todas las formas almacenadas (dibujo libre en este caso)
        for ((shape, data) in shapes) {
            when (shape) {
                "libre" -> {
                    val path = data as Path
                    canvas.drawPath(path, paint)
                }
            }
        }
    }

    // Manejo del toque en el lienzo
    private fun touchStart() {
        path = Path()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        shapes.add(Pair("libre", path))
    }

    private fun touchMove() {
        path.lineTo(motionTouchEventX, motionTouchEventY)
        invalidate()
    }

    private fun touchUp() {
        // No es necesario hacer nada al levantar el dedo
    }

    // Sobrescribir el método onTouchEvent para manejar eventos de toque
    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    // Función para borrar
    fun clearCanvas() {
        shapes.clear()
        invalidate()  // Redibujar el lienzo vacío
    }
}
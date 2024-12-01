package com.pmcl.proyectofinal_movil

import android.os.Bundle
import android.widget.GridLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class ContarObjetosActivity : AppCompatActivity() {

    private lateinit var contarControl: ContarObjetos
    private lateinit var linearLayoutImagenes: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contar_objetos)

        val btnVolver: ImageButton = findViewById(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior
        }

        // Inicializa el GridLayout donde se mostrarán las imágenes
        linearLayoutImagenes = findViewById(R.id.linearLayoutImagenes)

        // Establece las propiedades de GridLayout
        linearLayoutImagenes.columnCount = 3  // Por ejemplo, 3 columnas
        linearLayoutImagenes.rowCount = 3     // 3 filas, ajustado según la cantidad de imágenes

        // Inicializa el control de contar objetos y pasa el GridLayout
        contarControl = findViewById(R.id.miControlContar)
        contarControl.initLinearLayoutImagenes(linearLayoutImagenes)
    }
}

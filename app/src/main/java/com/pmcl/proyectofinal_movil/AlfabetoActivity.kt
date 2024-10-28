package com.pmcl.proyectofinal_movil

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AlfabetoActivity : AppCompatActivity() {
    private lateinit var buttonA: Alfabeto
    private lateinit var buttonB: Alfabeto
    private lateinit var buttonC: Alfabeto
    private lateinit var buttonD: Alfabeto
    private lateinit var buttonE: Alfabeto
    private lateinit var buttonF: Alfabeto
    private lateinit var buttonG: Alfabeto
    private lateinit var buttonH: Alfabeto
    private lateinit var buttonI: Alfabeto
    private lateinit var buttonJ: Alfabeto
    private lateinit var buttonK: Alfabeto
    private lateinit var buttonL: Alfabeto
    private lateinit var buttonM: Alfabeto
    private lateinit var buttonN: Alfabeto
    private lateinit var buttonÑ: Alfabeto
    private lateinit var buttonO: Alfabeto
    private lateinit var buttonP: Alfabeto
    private lateinit var buttonQ: Alfabeto
    private lateinit var buttonR: Alfabeto
    private lateinit var buttonS: Alfabeto
    private lateinit var buttonT: Alfabeto
    private lateinit var buttonU: Alfabeto
    private lateinit var buttonV: Alfabeto
    private lateinit var buttonW: Alfabeto
    private lateinit var buttonX: Alfabeto
    private lateinit var buttonY: Alfabeto
    private lateinit var buttonZ: Alfabeto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.alfabeto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnVolver: ImageButton = findViewById(R.id.btnVolver)

        // Configura el Click Listener para el botón de volver
        btnVolver.setOnClickListener {
            finish() // Cierra la actividad actual y regresa a la anterior
        }

        // Initialize buttons
        buttonA = findViewById(R.id.buttonA)
        buttonB = findViewById(R.id.buttonB)
        buttonC = findViewById(R.id.buttonC)
        buttonD = findViewById(R.id.buttonD)
        buttonE = findViewById(R.id.buttonE)
        buttonF = findViewById(R.id.buttonF)
        buttonG = findViewById(R.id.buttonG)
        buttonH = findViewById(R.id.buttonH)
        buttonI = findViewById(R.id.buttonI)
        buttonJ = findViewById(R.id.buttonJ)
        buttonK = findViewById(R.id.buttonK)
        buttonL = findViewById(R.id.buttonL)
        buttonM = findViewById(R.id.buttonM)
        buttonN = findViewById(R.id.buttonN)
        buttonÑ = findViewById(R.id.buttonÑ)
        buttonO = findViewById(R.id.buttonO)
        buttonP = findViewById(R.id.buttonP)
        buttonQ = findViewById(R.id.buttonQ)
        buttonR = findViewById(R.id.buttonR)
        buttonS = findViewById(R.id.buttonS)
        buttonT = findViewById(R.id.buttonT)
        buttonU = findViewById(R.id.buttonU)
        buttonV = findViewById(R.id.buttonV)
        buttonW = findViewById(R.id.buttonW)
        buttonX = findViewById(R.id.buttonX)
        buttonY = findViewById(R.id.buttonY)
        buttonZ = findViewById(R.id.buttonZ)

        // Assign sounds to each button
        buttonA.setSoundResource(R.raw.a)
        buttonB.setSoundResource(R.raw.b)
        buttonC.setSoundResource(R.raw.c)
        buttonD.setSoundResource(R.raw.d)
        buttonE.setSoundResource(R.raw.e)
        buttonF.setSoundResource(R.raw.f)
        buttonG.setSoundResource(R.raw.g)
        buttonH.setSoundResource(R.raw.h)
        buttonI.setSoundResource(R.raw.i)
        buttonJ.setSoundResource(R.raw.j)
        buttonK.setSoundResource(R.raw.k)
        buttonL.setSoundResource(R.raw.l)
        buttonM.setSoundResource(R.raw.m)
        buttonN.setSoundResource(R.raw.n)
        buttonÑ.setSoundResource(R.raw.nn)
        buttonO.setSoundResource(R.raw.o)
        buttonP.setSoundResource(R.raw.p)
        buttonQ.setSoundResource(R.raw.q)
        buttonR.setSoundResource(R.raw.r)
        buttonS.setSoundResource(R.raw.s)
        buttonT.setSoundResource(R.raw.t)
        buttonU.setSoundResource(R.raw.u)
        buttonV.setSoundResource(R.raw.v)
        buttonW.setSoundResource(R.raw.w)
        buttonX.setSoundResource(R.raw.x)
        buttonY.setSoundResource(R.raw.y)
        buttonZ.setSoundResource(R.raw.z)
    }
}

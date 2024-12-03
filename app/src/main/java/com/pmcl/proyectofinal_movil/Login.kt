package com.pmcl.proyectofinal_movil

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    //Variables de la base de datos
    private lateinit var db: DBSQLite

    //Información ingresada
    private lateinit var user : EditText
    private lateinit var password : EditText

    // Declara el MediaPlayer como una variable de clase
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //Vincular
        user = findViewById(R.id.nomUs)
        password = findViewById(R.id.password)

        // Inicializar la base de datos y la vista
        db = DBSQLite(this)

        val btnIniciaSesion = findViewById<Button>(R.id.iniciaSesion)
        val btnCrearCuenta = findViewById<Button>(R.id.crearCuenta)

        btnIniciaSesion.setOnClickListener{
            iniciarSesion()
        }
        btnCrearCuenta.setOnClickListener{
            //Redirigir a signup
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)

            //empezar sonido
            //mediaPlayer.start()
            finish()

        }

    }

    fun iniciarSesion(){
        //Obtener info de los edit text
        var usuarioActual = user.text.toString()
        var passwordActual = password.text.toString()

        // Validar campos vacíos
        if (usuarioActual.isEmpty() || passwordActual.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (db.loginUser(usuarioActual, passwordActual)) {
            // Usuario válido, navegar a la siguiente pantalla
            Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show()

            //Guardar usuario en sharedPreferences
            SaveSharedPreference.setUserName(this, usuarioActual)

            mediaPlayer = MediaPlayer.create(this, R.raw.info)
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            mediaPlayer.start()

            finish()

        } else {
            // Credenciales incorrectas
            Toast.makeText(this, "Alias o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }

    }

}
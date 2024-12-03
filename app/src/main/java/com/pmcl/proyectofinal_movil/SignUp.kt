package com.pmcl.proyectofinal_movil

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUp : AppCompatActivity() {

    //Variables de la base de datos
    private lateinit var db: DBSQLite

    //Información ingresada
    private lateinit var user : EditText
    private lateinit var password : EditText
    private lateinit var edad: EditText

    // Declara el MediaPlayer como una variable de clase
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        // Inicializar la base de datos y la vista
        db = DBSQLite(this)

        //Vincular vistas
        user = findViewById(R.id.nomUs)
        password = findViewById(R.id.password)
        edad = findViewById(R.id.edad)

        val btnCrearCuenta = findViewById<Button>(R.id.crearCuenta)
        val btnSalir = findViewById<ImageButton>(R.id.btnVolver)

        btnCrearCuenta.setOnClickListener{ registrarUsuario() }
        btnSalir.setOnClickListener{ finish() }

    }

    private fun registrarUsuario() {
        val alias = user.text.toString().trim()
        val password = password.text.toString().trim()
        val edad = edad.text.toString().trim()

        //Validar campos vacíos
        if (alias.isEmpty() || password.isEmpty() || edad.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Convertir edad a entero
        val edadInt = edad.toIntOrNull()
        if (edadInt == null || edadInt <= 0 || edadInt>=100) {
            Toast.makeText(this, "Ingrese una edad válida", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear usuario
        val resultado = db.createUser(alias, password, edadInt)
        if (resultado) {

            //Guardar usuario en sharedPreference
            SaveSharedPreference.setUserName(this, alias)

            //Exito, navegar a la siguiente pantalla
            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
            mediaPlayer = MediaPlayer.create(this, R.raw.info)
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            mediaPlayer.start()
            //Terminar signup
            finish()

        } else {
            Toast.makeText(this, "Error: Usuario ya existente", Toast.LENGTH_SHORT).show()
        }
    }

}
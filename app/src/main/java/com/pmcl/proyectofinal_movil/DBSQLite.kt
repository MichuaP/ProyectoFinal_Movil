package com.pmcl.proyectofinal_movil

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Vector

class DBSQLite (context: Context?): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        //Metodos sqlite opener
        //Si se cambia la estructura de la BD se debe de incrementar la version
        //de la BD
        val DATABASE_VERSION : Int = 1
        val DATABASE_NAME : String = "DBMinimentes"
        val TAB_USUARIO : String = "usuario"
        val TAB_ACTIVIDAD : String = "datos"
        val TAB_PROGRESO : String = "progreso"
    }

    override fun onCreate(db: SQLiteDatabase) {

        //Habilitar llaves foraneas
        db.execSQL("PRAGMA foreign_keys = ON;")

        // Crear tabla usuario
        db.execSQL(
            "CREATE TABLE $TAB_USUARIO (" +
                    "alias TEXT PRIMARY KEY, " +
                    "password TEXT, " +
                    "edad INTEGER)"
        )

        // Crear tabla actividad
        db.execSQL(
            "CREATE TABLE $TAB_ACTIVIDAD (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT)"
        )

        // Crear tabla progreso
        db.execSQL(
            "CREATE TABLE $TAB_PROGRESO (" +
                    "aliasUs TEXT, " +
                    "nombreAct TEXT, " +
                    "progreso INTEGER, " +
                    "terminada TEXT, " +
                    "FOREIGN KEY (aliasUs) REFERENCES $TAB_USUARIO(alias), " +
                    "FOREIGN KEY (nombreAct) REFERENCES $TAB_ACTIVIDAD(nombre))"
        )
        // Insertar usuario administrador
        db.execSQL(
            "INSERT OR IGNORE INTO $TAB_USUARIO (alias, password, edad) VALUES (?, ?, ?)",
            arrayOf("admin", "admin123", 21)
        )

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int ) {

    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int ) {

    }

    // Función para iniciar sesión
    fun loginUser(alias: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TAB_USUARIO WHERE alias = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(alias, password))
        val isValidUser = cursor.count > 0 // Verifica si hay algún registro que coincide
        cursor.close()
        return isValidUser
    }

    // Función para guardar progreso
    fun saveProgress(alias: String, actividad: String, progreso: Int, terminada: String) {
        val db = writableDatabase
        val query = "INSERT INTO $TAB_PROGRESO (aliasUs, nombreAct, progreso, terminada) " +
                "VALUES (?, ?, ?, ?)"
        db.execSQL(query, arrayOf(alias, actividad, progreso, terminada))
        db.close()
    }

    // Función para eliminar progreso específico
    fun deleteProgress(alias: String, actividad: String) {
        val db = writableDatabase
        val query = "DELETE FROM $TAB_PROGRESO WHERE aliasUs = ? AND nombreAct = ?"
        db.execSQL(query, arrayOf(alias, actividad))
        db.close()
    }

    // Función para mostrar progreso
    fun showProgress(alias: String): List<String> {
        val result = mutableListOf<String>()
        val db = readableDatabase
        val query = "SELECT nombreAct, progreso, terminada FROM $TAB_PROGRESO WHERE aliasUs = ?"
        val cursor = db.rawQuery(query, arrayOf(alias))
        while (cursor.moveToNext()) {
            val actividad = cursor.getString(0)
            val progreso = cursor.getInt(1)
            val terminada = cursor.getString(2)
            result.add("$actividad: $progreso% - Terminada: $terminada")
        }
        cursor.close()
        return result
    }

    // Agregar usuario
    fun createUser(alias: String, password: String, edad: Int): Boolean {
        return try {
            val db = writableDatabase
            val query = "INSERT INTO $TAB_USUARIO (alias, password, edad) VALUES (?, ?, ?)"
            db.execSQL(query, arrayOf(alias, password, edad))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false // Error
        }
    }

}
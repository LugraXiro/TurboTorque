package com.example.tiendaturbotorque

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Clase que crea y actualiza la base de datos SQLite para usuarios.
 * Tabla: usuarios (id, nombre, contrasenha)
 */
class UsuariosDBHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        // Sentencia SQL para crear la tabla de usuarios
        val sqlCreateTable = """
            CREATE TABLE $TABLE_USUARIOS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT NOT NULL UNIQUE,
                $COL_CONTRASENHA TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(sqlCreateTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Para desarrollo: borramos y recreamos la tabla
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        onCreate(db)
    }

    // ====== MÉTODOS CRUD ======

    /**
     * Inserta un nuevo usuario en la base de datos
     * @return true si se insertó correctamente, false si hubo error
     */
    fun insertarUsuario(nombre: String, contrasenha: String): Boolean {
        val db = this.writableDatabase

        return try {
            // Creamos un objeto ContentValues con los datos
            val valores = android.content.ContentValues().apply {
                put(COL_NOMBRE, nombre)
                put(COL_CONTRASENHA, contrasenha)
            }

            // Insertamos en la tabla
            val resultado = db.insert(TABLE_USUARIOS, null, valores)

            // Si resultado es -1, hubo error. Si es mayor, es el ID del nuevo registro
            resultado != -1L
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    /**
     * Valida si el usuario y contraseña son correctos
     * @return true si las credenciales son válidas
     */
    fun validarUsuario(nombre: String, contrasenha: String): Boolean {
        val db = this.readableDatabase

        return try {
            // Consulta SQL para buscar el usuario con esa contraseña
            val query = "SELECT * FROM $TABLE_USUARIOS WHERE $COL_NOMBRE = ? AND $COL_CONTRASENHA = ?"
            val cursor = db.rawQuery(query, arrayOf(nombre, contrasenha))

            // Si el cursor tiene al menos 1 fila, el usuario existe
            val existe = cursor.count > 0
            cursor.close()
            existe
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    /**
     * Comprueba si un nombre de usuario ya existe en la base de datos
     * @return true si el usuario existe
     */
    fun existeUsuario(nombre: String): Boolean {
        val db = this.readableDatabase

        return try {
            val query = "SELECT * FROM $TABLE_USUARIOS WHERE $COL_NOMBRE = ?"
            val cursor = db.rawQuery(query, arrayOf(nombre))

            val existe = cursor.count > 0
            cursor.close()
            existe
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    companion object {
        const val DATABASE_NAME = "tienda.db"
        const val DATABASE_VERSION = 1

        const val TABLE_USUARIOS = "usuarios"
        const val COL_ID = "id"
        const val COL_NOMBRE = "nombre"
        const val COL_CONTRASENHA = "contrasenha"
    }
}
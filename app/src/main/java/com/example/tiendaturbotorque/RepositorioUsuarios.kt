package com.example.tiendaturbotorque

import android.content.Context

object RepositorioUsuarios {

    // Helper de la base de datos
    private lateinit var dbHelper: UsuariosDBHelper

    /**
     * IMPORTANTE: Llamar este método desde MainActivity o SplashActivity
     * antes de usar cualquier otra función de este repositorio
     */
    fun inicializar(context: Context) {
        dbHelper = UsuariosDBHelper(context)
        // Insertar usuarios de prueba si la BD está vacía
        insertarUsuariosDePrueba()
    }

    /**
     * Inserta algunos usuarios de prueba la primera vez
     */
    private fun insertarUsuariosDePrueba() {
        // Solo insertar si no existen ya
        if (!dbHelper.existeUsuario("admin")) {
            dbHelper.insertarUsuario("admin", "admin")
        }
        if (!dbHelper.existeUsuario("Jose")) {
            dbHelper.insertarUsuario("Jose", "abcd1234#")
        }
        if (!dbHelper.existeUsuario("Manuel")) {
            dbHelper.insertarUsuario("Manuel", "qwerty1#")
        }
    }

    /**
     * Valida si user y password son correctos
     */
    fun validarUsuario(usuario: String, contrasenha: String): Boolean {
        return dbHelper.validarUsuario(usuario, contrasenha)
    }

    /**
     * Registra un nuevo usuario en la base de datos
     */
    fun registrarUsuario(usuario: String, contrasenha: String): Boolean {
        val validador = PasswordValidator()

        // Validar contraseña
        if (!validador.esValida(contrasenha)) {
            println("Error: ${validador.obtenerMotivoInvalidez(contrasenha)}")
            return false
        }

        // Comprobar si ya existe
        if (dbHelper.existeUsuario(usuario)) {
            return false
        }

        // Insertar en la base de datos
        return dbHelper.insertarUsuario(usuario, contrasenha)
    }

    /**
     * Comprueba si el nombre de usuario está libre
     */
    fun nombreUsuarioLibre(nombreInsertado: String): Boolean {
        return !dbHelper.existeUsuario(nombreInsertado)
    }
}
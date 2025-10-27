package com.example.tiendaturbotorque

object RepositorioUsuarios {
    // Defino un mapa que se modificable (mutable) para las credenciales
    // Ambos datos son String
    private val credencialesUsuario = mutableMapOf(
        "Jose" to "abcd1234#",
        "Manuel" to "qwerty1#"
    )

    // Validar si user y password están en el mutableMapOf
    fun validarUsuario(usuario: String, contrasenha: String): Boolean {
        return credencialesUsuario[usuario] == contrasenha
    }

    // Valida si el registro se realizó con éxito o no.
    fun registrarUsuario(usuario: String, contrasenha: String): Boolean {
        val validador = PasswordValidator()

        if (!validador.esValida(contrasenha)) {
            println("Error: Jose${validador.obtenerMotivoInvalidez(contrasenha)}")
            return false
        }
        if (credencialesUsuario.containsKey(usuario)) return false

        credencialesUsuario[usuario] = contrasenha
        return true
    }

    // Comprueba si ya existe el usuario o no. True si se puede aplicar, False si ese nombre está ocupado
    fun nombreUsuarioLibre(nombreInsertado: String): Boolean {
        return !credencialesUsuario.containsKey(nombreInsertado)
    }
}
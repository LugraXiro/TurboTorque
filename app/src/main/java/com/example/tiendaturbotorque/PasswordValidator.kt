package com.example.tiendaturbotorque

/**
 * Clase encargada de validar contraseñas según reglas básicas.
 *
 * Reglas:
 * - Longitud mínima: 8 caracteres.
 * - Debe contener al menos un dígito [0-9].
 * - Debe contener al menos uno de estos símbolos: @, #, %
 */
class PasswordValidator {

    /**
     * Valida si la contraseña cumple las reglas establecidas.
     * @param contrasenha Contraseña a evaluar.
     * @return true si es válida, false si no cumple los requisitos.
     */
    fun esValida(contrasenha: String): Boolean {
        val longitudOk = contrasenha.length >= 8
        val tieneNumero = contrasenha.any { it.isDigit() }
        val tieneSimbolo = contrasenha.any { it in "@#%" }

        val resultado = longitudOk && tieneNumero && tieneSimbolo
        println("Validación -> longitudOk=$longitudOk, tieneNumero=$tieneNumero, tieneSimbolo=$tieneSimbolo, resultado=$resultado")

        return resultado
    }

    /**
     * Devuelve un mensaje descriptivo con los fallos detectados.
     * @param contrasenha Contraseña a evaluar.
     * @return Texto con las causas de invalidez (vacío si es válida).
     */
    fun obtenerMotivoInvalidez(contrasenha: String): String {
        val errores = mutableListOf<String>()

        if (contrasenha.length < 8) errores.add("Debe tener al menos 8 caracteres.")
        if (!contrasenha.any { it.isDigit() }) errores.add("Debe incluir al menos un número.")
        if (!contrasenha.any { it in "@#%" }) errores.add("Debe incluir uno de los símbolos @, # o %.")

        return errores.joinToString(" ")
    }
}

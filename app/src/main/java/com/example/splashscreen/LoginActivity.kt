package com.example.splashscreen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsuario = findViewById<EditText>(R.id.insertUser)
        val etContrasenha = findViewById<EditText>(R.id.insertPassword)
        val btnLogIn = findViewById<Button>(R.id.buttonLogIn)

        //Pulsar el botón de Login
        btnLogIn.setOnClickListener {
            val usuario = etUsuario.text.toString()
            val contrasenha = etContrasenha.text.toString()

            if (usuario.isEmpty() || contrasenha.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            } else {

                /*
                Lógica de validación de usuario y contraseña.
                 */

                if(RepositorioUsuarios.validarUsuario(usuario, contrasenha)) {
                    Toast.makeText(this, "Login con $usuario", Toast.LENGTH_SHORT).show()

                    // Lanzar MainActivity. Intent se usa para lanzar Activitys, iniciar servicios o mandar info entre componentes.
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() //cierra LoginActivity para que no se pueda volver al darle al botón de atrás.
                } else {
                    Toast.makeText(this, "Usuario o contrasña incorrectos", Toast.LENGTH_SHORT).show()
                }


            }
        }
    }
}
package com.example.splashscreen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.addCallback
import androidx.core.content.ContextCompat

class RegistrateActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrate)

        window.statusBarColor = ContextCompat.getColor(this, R.color.fondoBarraEstadoAndroid)

        val etUsuario = findViewById<EditText>(R.id.etInsertarUsuario)
        val etContrasenha = findViewById<EditText>(R.id.etInsertarContrasenha)
        val etContrasenhaOtraVez = findViewById<EditText>(R.id.etInsertarContrasenhaOtraVez)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)

        // Pulsar el botón de Login
        btnRegistrarse.setOnClickListener {
            val usuario = etUsuario.text.trim().toString()
            val contrasenha = etContrasenha.text.trim().toString()
            val contrasenhaOtraVez = etContrasenhaOtraVez.text.trim().toString()

            /*
            Lógica de registro
             */

            //Comprobar que ningún campo está vacío
            if (usuario.isEmpty() || contrasenha.isEmpty() || contrasenhaOtraVez.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }

            //Comprobar que las contraseñas coincidan
            else if(contrasenha != contrasenhaOtraVez){
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            }

            //Se procede a comprobar que el nombre de usuario no está repetido en RepositorioUsuarios.kt
            // y se continúa hacia MainActivity
            else if (RepositorioUsuarios.nombreUsuarioLibre(usuario)){
                RepositorioUsuarios.registrarUsuario(usuario, contrasenha)
                Toast.makeText(this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show()

                // Se redirige a MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            else {
                Toast.makeText(this, "El usuario ya existe.", Toast.LENGTH_SHORT).show()
            }

        }

        // Regresar ala pantalla activity_login al pulsar los controles de Android
        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@RegistrateActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
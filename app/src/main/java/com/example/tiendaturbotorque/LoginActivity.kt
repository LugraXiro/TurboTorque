package com.example.tiendaturbotorque

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.statusBarColor = ContextCompat.getColor(this, R.color.fondoBarraEstadoAndroid)

        val etUsuario = findViewById<EditText>(R.id.etInsertarUsuario)
        val etContrasenha = findViewById<EditText>(R.id.etInsertarContrasenha)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        val btnRegistrarse = findViewById<TextView>(R.id.tvRegistrate)





        //Pulsar el botón de Login
        btnIniciarSesion.setOnClickListener {
            val usuario = etUsuario.text.trim().toString()
            val contrasenha = etContrasenha.text.trim().toString()

            if (usuario.isEmpty() || contrasenha.isEmpty()){
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            } else {

                /*
                Lógica de validación de usuario y contraseña.
                 */

                if(RepositorioUsuarios.validarUsuario(usuario, contrasenha)) {
                    Toast.makeText(this, "Login con $usuario", Toast.LENGTH_SHORT).show()

                    // Lanzar MainActivity. Intent se usa para lanzar Activitys, iniciar servicios o mandar info entre componentes.
                    val intent = Intent(this, TiendaActivity::class.java)
                    startActivity(intent)
                    finish() //cierra LoginActivity para que no se pueda volver al darle al botón de atrás.
                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }



        //Pulsar el texto "Registrarse" -> Redirección a pantalla de registro
        btnRegistrarse.setOnClickListener {
            val intentRegistrateActivity = Intent(this, RegistrateActivity::class.java)
            startActivity(intentRegistrateActivity)
            finish()
        }
    }
}

package com.example.tiendaturbotorque

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // Instala la SplashScreen del sistema
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Aquí defines la Activity a la que redirigirá
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

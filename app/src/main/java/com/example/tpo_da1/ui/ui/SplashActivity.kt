package com.example.tpo_da1.ui.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import com.example.tpo_da1.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private val TimeOut: Long = 4000
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // El usuario está autenticado
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                // El usuario no autenticado
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, TimeOut)
    }
}
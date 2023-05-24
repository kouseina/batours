package com.example.batours

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    private lateinit var tvRegister: TextView
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        tvRegister = findViewById(R.id.tv_register)
        btnLogin = findViewById(R.id.btn_login)

        tvRegister.setOnClickListener{view ->
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener{view ->
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
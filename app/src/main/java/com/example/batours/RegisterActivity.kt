package com.example.batours

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    private lateinit var tvLogin : TextView
    private  lateinit var btnRegister : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        tvLogin = findViewById(R.id.tv_login)
        btnRegister = findViewById(R.id.btn_register)


        tvLogin.setOnClickListener{view ->
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener{view ->
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}
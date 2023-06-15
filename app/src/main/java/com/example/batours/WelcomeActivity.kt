package com.example.batours

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.batours.storage.SharedPrefManager

class WelcomeActivity : AppCompatActivity() {

    private lateinit var btnWelcome : Button

    override fun onStart() {
        super.onStart()

        if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        supportActionBar?.hide()


        btnWelcome = findViewById(R.id.btn_welcome)
        btnWelcome.setOnClickListener{
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
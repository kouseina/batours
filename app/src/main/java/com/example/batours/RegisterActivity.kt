package com.example.batours

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.batours.api.RetrofitClient
import com.example.batours.models.DefaultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var tvLogin : TextView
    private lateinit var btnRegister : Button
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextFullName: EditText
    private lateinit var editTextPhone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        tvLogin = findViewById(R.id.tv_login)
        btnRegister = findViewById(R.id.btn_register)
        editTextEmail = findViewById(R.id.et_email)
        editTextPassword = findViewById(R.id.et_password)
        editTextFullName = findViewById(R.id.et_fullName)
        editTextPhone = findViewById(R.id.et_phone)


        tvLogin.setOnClickListener{view ->
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener{view ->
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()
            val fullName = editTextFullName.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()

            if(fullName.isEmpty()){
                editTextFullName.error = "Name required"
                editTextFullName.requestFocus()
                return@setOnClickListener
            }

            if(email.isEmpty()){
                editTextEmail.error = "Email required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }

            if(phone.isEmpty()){
                editTextPhone.error = "Phone required"
                editTextPhone.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()){
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.createUser(email, fullName, phone, password, "", authHeader = RetrofitClient.BASIC_AUTH)
                .enqueue(object: Callback<DefaultResponse>{
                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        if(response.code() == 200) {
                            Toast.makeText(applicationContext, "Register succeed", Toast.LENGTH_LONG).show()
                            var intent = Intent(applicationContext,LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }

                        else {
                            Toast.makeText(applicationContext, "Register fail", Toast.LENGTH_LONG).show()
                        }
                    }

                })
        }
    }


}
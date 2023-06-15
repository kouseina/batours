package com.example.batours

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.batours.api.RetrofitClient
import com.example.batours.models.LoginResponse
import com.example.batours.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private lateinit var tvRegister: TextView
    private lateinit var btnLogin: Button
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        tvRegister = findViewById(R.id.tv_register)
        btnLogin = findViewById(R.id.btn_login)
        editTextEmail = findViewById(R.id.et_email)
        editTextPassword = findViewById(R.id.et_password)

        tvRegister.setOnClickListener{view ->
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener{view ->

            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if(email.isEmpty()){
                editTextEmail.error = "Email required"
                editTextEmail.requestFocus()
                return@setOnClickListener
            }


            if(password.isEmpty()){
                editTextPassword.error = "Password required"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            try {
                RetrofitClient.instance.userLogin(email, password, authHeader = RetrofitClient.BASIC_AUTH)
                    .enqueue(object: Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                        }

                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                            if(response.code() == 200){

                                SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.data)
                                SharedPrefManager.getInstance(applicationContext).saveToken(response.body()?.token)

                                Toast.makeText(applicationContext, "Login succeed", Toast.LENGTH_LONG).show()

                                val intent = Intent(applicationContext, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            }

                            else if (response.code() == 204 || response.code() == 403){
                                Toast.makeText(applicationContext, "email or password is incorrect", Toast.LENGTH_LONG).show()
                            }

                            else{
                                Toast.makeText(applicationContext, response.body().toString(), Toast.LENGTH_LONG).show()
                            }

                        }
                    })
            } catch (e: Exception) {
                Log.e("Exception on login : ", e.message.toString())
            }
        }
    }
}
package com.example.batours

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.batours.api.RetrofitClient
import com.example.batours.models.DetailDestinationResponse
import com.example.batours.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class DetailActivity : AppCompatActivity() {
    var id: Int? = null
    lateinit var tvBack: TextView
    lateinit var tvTitle: TextView
    lateinit var tvDesc: TextView
    lateinit var img: ImageView
    lateinit var tvAddress: TextView
    lateinit var tvPrice: TextView
    lateinit var tvInfo: TextView
    lateinit var btnMap: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar?.hide()

        tvBack = findViewById(R.id.tv_back)
        tvTitle = findViewById(R.id.tv_title)
        tvDesc = findViewById(R.id.tv_desc)
        img = findViewById(R.id.img)
        tvAddress = findViewById(R.id.tv_address)
        tvPrice = findViewById(R.id.tv_price)
        tvInfo = findViewById(R.id.tv_info)
        btnMap = findViewById(R.id.btn_map)

        tvBack.setOnClickListener{view ->
            onBackPressed()
        }

        id = intent.getIntExtra("id", 0)

        if (id != null) {
            getDetailDestination()
        }

        Log.d("DetailActivity", "id : $id")
    }

    private fun getDetailDestination() {
        RetrofitClient.instance.getDetailDestination("Bearer ${SharedPrefManager.getInstance(applicationContext).token}", id ?: 0)
            .enqueue(object : Callback<DetailDestinationResponse> {
                override fun onFailure(call: Call<DetailDestinationResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<DetailDestinationResponse>,
                    response: Response<DetailDestinationResponse>
                ) {
                    if (response.code() == 200) {
                        val data = response.body()?.data

                        tvTitle.text = data?.name

                        Glide.with(applicationContext)
                            .load(data?.image_url)
                            .centerCrop()
                            .into(img);

                        tvDesc.text = data?.description
                        tvPrice.text = data?.price
                        tvAddress.text = data?.category
                        tvInfo.text = data?.rating
                    }
                }
            })
    }
}
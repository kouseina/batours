package com.example.batours

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.batours.api.RetrofitClient
import com.example.batours.models.DefaultResponse
import com.example.batours.models.DetailDestinationResponse
import com.example.batours.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailActivity : AppCompatActivity() {
    var id: Int? = null
    lateinit var tvBack: TextView
    lateinit var tvTitle: TextView
    lateinit var tvDesc: TextView
    lateinit var img: ImageView
    lateinit var tvAddress: TextView
    lateinit var tvPrice: TextView
    lateinit var tvRating: TextView
    lateinit var btnMap: Button
    lateinit var imgBookmark: ImageView

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
        tvRating = findViewById(R.id.tv_rating)
        btnMap = findViewById(R.id.btn_map)
        imgBookmark = findViewById(R.id.img_bookmark)

        tvBack.setOnClickListener{view ->
            onBackPressed()
        }

        imgBookmark.setOnClickListener{view ->
            saveBookmark()
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
                        tvPrice.text = (data?.price ?: 0).toString()
                        tvAddress.text = data?.category
                        tvRating.text = data?.rating

                        btnMap.setOnClickListener { view ->
                            val gmmIntentUri = Uri.parse(data?.maps_url)
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            startActivity(mapIntent)
                        }
                    }
                }
            })
    }

    private fun saveBookmark() {
        RetrofitClient.instance.saveBookmark("Bearer ${SharedPrefManager.getInstance(applicationContext).token}", id =  id ?: 0)
            .enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if(response.code() == 200) {
                        Toast.makeText(applicationContext, "Saved", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

            })
    }
}
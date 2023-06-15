package com.example.batours

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.example.batours.adapters.DestinationGridRVAdapter
import com.example.batours.api.RetrofitClient
import com.example.batours.models.AllDestinationResponse
import com.example.batours.models.DestinationItem
import com.example.batours.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCategoryActivity : AppCompatActivity() {

    lateinit var mainScrollview: ScrollView
    lateinit var gvDestination: ExpandableHeightGridView
    lateinit var destinationList: List<DestinationItem>
    lateinit var pbDestination: ProgressBar
    lateinit var tvTitle: TextView
    lateinit var tvBack: TextView

    var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_category)

        supportActionBar?.hide()

        mainScrollview = findViewById(R.id.main_scrollview)

        gvDestination = findViewById(R.id.gv_destination)
        gvDestination.isExpanded = true
        pbDestination = findViewById(R.id.pb_destination)
        tvTitle = findViewById(R.id.tv_title)
        tvBack = findViewById(R.id.tv_back)

        tvBack.setOnClickListener{view ->
            onBackPressed()
        }

        category = intent.getStringExtra("category")

        if(category != null) {
            tvTitle.text = category
            getAllDestination()
        }
    }

    private fun getAllDestination() {
        RetrofitClient.instance.getDestinationByCategory(
            "Bearer ${SharedPrefManager.getInstance(applicationContext).token}", category = category ?: "")
            .enqueue(object : Callback<AllDestinationResponse> {

            override fun onFailure(call: Call<AllDestinationResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                pbDestination.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<AllDestinationResponse>,
                response: Response<AllDestinationResponse>
            ) {
                pbDestination.visibility = View.GONE
                Log.d("getAllDestination", response.body().toString())

                if(response.code() == 200) {
                    val data = response.body()?.data

                    if (data != null) {
                        destinationList = data

                        // on below line we are initializing our course adapter
                        // and passing course list and context.
                        val destinationAdapter = DestinationGridRVAdapter(destinationList = destinationList, applicationContext)

                        // on below line we are setting adapter to our grid view.
                        gvDestination.adapter = destinationAdapter

                        gvDestination.setOnTouchListener { _, event ->

                            // event action is set as ACTION_MOVE
                            event.action == MotionEvent.ACTION_MOVE
                        }

                        // on below line we are adding on item
                        // click listener for our grid view.
                        gvDestination.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                            // inside on click method we are simply displaying
                            // a toast message with course name.
                            var intent = Intent(applicationContext, DetailActivity::class.java).putExtra("id", destinationList[position].id)
                            startActivity(intent)
                        }
                    }
                }
            }

        })
    }
}
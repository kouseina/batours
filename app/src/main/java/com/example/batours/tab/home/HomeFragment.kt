package com.example.batours.tab.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.batours.*
import com.example.batours.GridRVAdapter
import com.example.batours.api.RetrofitClient
import com.example.batours.databinding.FragmentHomeBinding
import com.example.batours.models.AllDestinationResponse
import com.example.batours.models.DestinationItem
import com.example.batours.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var gvDestination: ExpandableHeightGridView
    lateinit var destinationList: List<DestinationItem>
    lateinit var mainScrollview: ScrollView
    lateinit var progressLoader: ProgressBar

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gvDestination = view.findViewById(R.id.gv_destination)
        gvDestination.isExpanded = true
        mainScrollview = view.findViewById(R.id.main_scrollview)
        progressLoader = view.findViewById(R.id.progress_loader)

        getDestinationList()

        // on below line we are adding data to
        // our course list with image and course name.
//        destinationList = listOf(
//            GridViewModal("Gedung Sate", "https://vervalyayasan.data.kemdikbud.go.id/upload/file/9F/9FD0/80640-7789672821873048630.jpg"),
//            GridViewModal("Sahyang Heulut","https://vervalyayasan.data.kemdikbud.go.id/upload/file/9F/9FD0/80640-7789672821873048630.jpg"),
//            GridViewModal("Gedung Sate Padang", "https://vervalyayasan.data.kemdikbud.go.id/upload/file/9F/9FD0/80640-7789672821873048630.jpg"),
//            GridViewModal("Sahyang Heulut", "https://vervalyayasan.data.kemdikbud.go.id/upload/file/9F/9FD0/80640-7789672821873048630.jpg"),
//            GridViewModal("Gedung Sate", "https://vervalyayasan.data.kemdikbud.go.id/upload/file/9F/9FD0/80640-7789672821873048630.jpg"),
//            GridViewModal("Sahyang Heulut", "https://vervalyayasan.data.kemdikbud.go.id/upload/file/9F/9FD0/80640-7789672821873048630.jpg"),
//            GridViewModal("Gedung Sate Padang", "https://vervalyayasan.data.kemdikbud.go.id/upload/file/9F/9FD0/80640-7789672821873048630.jpg"),
//            GridViewModal("Sahyang Heulut", "https://vervalyayasan.data.kemdikbud.go.id/upload/file/9F/9FD0/80640-7789672821873048630.jpg"),
//        )
    }

    private fun getDestinationList() {
        RetrofitClient.instance.getAllDestination("Bearer ${context?.let {
            SharedPrefManager.getInstance(
                it
            ).token
        }}").enqueue(object : Callback<AllDestinationResponse> {

            override fun onFailure(call: Call<AllDestinationResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                progressLoader.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<AllDestinationResponse>,
                response: Response<AllDestinationResponse>
            ) {
                progressLoader.visibility = View.GONE
//                Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show()
                Log.d("HomeFragment", response.body().toString())

                if(response.code() == 200) {
                    val data = response.body()?.data

                    Log.d("HomeFragment", data.toString())

                    if (data != null) {
//                        var tempList = ArrayList<DestinationItem>()
//
//                        repeat(data.count()) {
//                            val item = data[it]
//                            tempList.add(DestinationItem(title = item.name ?: "", img = item.image_url ?: ""))
//                        }

                        destinationList = data

                        // on below line we are initializing our course adapter
                        // and passing course list and context.
                        val destinationAdapter = activity?.let { GridRVAdapter(destinationList = destinationList, it.applicationContext) }

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
                            var intent = Intent(context, DetailActivity::class.java).putExtra("id", destinationList[position].id)
                            startActivity(intent)
                        }
                    }
                }
            }

        })
    }
}

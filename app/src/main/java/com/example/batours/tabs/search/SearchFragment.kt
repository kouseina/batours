package com.example.batours.tabs.search

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.batours.DetailActivity
import com.example.batours.ExpandableHeightGridView
import com.example.batours.R
import com.example.batours.adapters.DestinationGridRVAdapter
import com.example.batours.api.RetrofitClient
import com.example.batours.databinding.FragmentSearchBinding
import com.example.batours.models.AllDestinationResponse
import com.example.batours.models.DestinationItem
import com.example.batours.storage.SharedPrefManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    lateinit var btnFilter: Button
    private lateinit var mainScrollview: ScrollView

    lateinit var gvDestination: ExpandableHeightGridView
    var destinationList = listOf <DestinationItem>()
    lateinit var pbDestination: ProgressBar

    lateinit var llRecommendationDestination: LinearLayout

    lateinit var gvRecommendationDestination: ExpandableHeightGridView
    var recommendationDestinationList = listOf <DestinationItem>()
    lateinit var pbRecommendationDestination: ProgressBar

    lateinit var etSearch: EditText

    lateinit var alertDialog: AlertDialog
    lateinit var btnDialogClose: TextView

    lateinit var cgRating: ChipGroup
    lateinit var crLowest: Chip
    lateinit var crHighest: Chip

    lateinit var cgCategory: ChipGroup
    lateinit var ccCagarAlam: Chip
    lateinit var ccBudaya: Chip
    lateinit var ccTamanHiburan: Chip
    lateinit var ccTempatIbadah: Chip
    lateinit var ccPusatPerbelanjaan: Chip

    lateinit var cgPrice: ChipGroup
    lateinit var cpCheapest: Chip
    lateinit var cpMostExpensive: Chip

    private var columnParam: String? = null
    private var filterParam: String? = null
    private var categoryParam: String? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnFilter = view.findViewById(R.id.btn_filer)
        mainScrollview = view.findViewById(R.id.main_scrollview)
        llRecommendationDestination = view.findViewById(R.id.ll_recommendation_destination)

        gvRecommendationDestination = view.findViewById(R.id.gv_recommendation_destination)
        gvRecommendationDestination.isExpanded = true
        pbRecommendationDestination = view.findViewById(R.id.pb_recommendation_destination)

        gvDestination = view.findViewById(R.id.gv_destination)
        gvDestination.isExpanded = true
        pbDestination = view.findViewById(R.id.pb_destination)

        etSearch = view.findViewById(R.id.et_search)

        showAlertDialog()

        etSearch.setOnEditorActionListener { v, actionId, event ->
            var handled = false

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getAllDestination()

                handled = true
            }

            handled
        }

        getRecommendationDestination()
    }

    private fun showAlertDialog() {
        val view = layoutInflater.inflate(R.layout.filter_dialog_layout, null)
        btnDialogClose = view.findViewById(R.id.btn_close)

        cgRating = view.findViewById(R.id.cg_rating)
        crLowest = view.findViewById(R.id.cr_lowest)
        crHighest = view.findViewById(R.id.cr_highest)

        cgCategory = view.findViewById(R.id.cg_category)
        ccCagarAlam = view.findViewById(R.id.cc_cagar_alam)
        ccBudaya = view.findViewById(R.id.cc_budaya)
        ccTamanHiburan = view.findViewById(R.id.cc_taman_hiburan)
        ccTempatIbadah = view.findViewById(R.id.cc_tempat_ibadah)
        ccPusatPerbelanjaan = view.findViewById(R.id.cc_pusat_perbelanjaan)

        cgPrice = view.findViewById(R.id.cg_price)
        cpCheapest = view.findViewById(R.id.cp_cheapest)
        cpMostExpensive = view.findViewById(R.id.cp_most_expensive)

        val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        alertDialog = builder.setView(view).create()

        btnFilter.setOnClickListener{view ->
            alertDialog.show()
        }

        btnDialogClose.setOnClickListener{view ->
            alertDialog.dismiss()
        }

        alertDialog.setOnDismissListener {
            getAllDestination()
        }

        cgRating.setOnCheckedStateChangeListener { group, checkedIds ->
            println("checked id cg rating : ${group.checkedChipId}")
            println("is lowest rating chip checked : ${crLowest.isChecked}")
            println("is highest rating chip checked : ${crHighest.isChecked}")

            if(checkedIds.isEmpty()) {
                columnParam = null
                filterParam = null
            }

            crLowest.setOnCheckedChangeListener { _, selected ->
                if (selected) {
                    crLowest.setChipStrokeColorResource(R.color.blue)

                    columnParam = "rating"
                    filterParam = "asc"

                    println("column param : ${columnParam}")
                    println("filter param : ${filterParam}")
                }

                else crLowest.setChipStrokeColorResource(R.color.grey)
            }

            crHighest.setOnCheckedChangeListener { _, selected ->
                if (selected) {
                    crHighest.setChipStrokeColorResource(R.color.blue)

                    columnParam = "rating"
                    filterParam = "desc"

                    println("column param : ${columnParam}")
                    println("filter param : ${filterParam}")
                }

                else crHighest.setChipStrokeColorResource(R.color.grey)
            }
        }

        cgCategory.setOnCheckedStateChangeListener { group, checkedIds ->
            println("checked id cg rating : ${group.checkedChipId}")
            println("is cagar alam category chip checked : ${ccCagarAlam.isChecked}")
            println("is budaya category chip checked : ${ccBudaya.isChecked}")
            println("is taman hiburan category chip checked : ${ccTamanHiburan.isChecked}")
            println("is tempat ibadah category chip checked : ${ccTempatIbadah.isChecked}")
            println("is pusat perbelanjaan category chip checked : ${ccPusatPerbelanjaan.isChecked}")

            if(checkedIds.isEmpty()) {
                categoryParam = null
            }

            listOf(ccCagarAlam, ccBudaya, ccTamanHiburan, ccTempatIbadah, ccPusatPerbelanjaan).map {
                it.setOnCheckedChangeListener { _, selected ->
                    if (selected) {
                        it.setChipStrokeColorResource(R.color.blue)

                        when (it) {
                            ccCagarAlam -> categoryParam = "Cagar Alam"
                            ccBudaya -> categoryParam = "Budaya"
                            ccTamanHiburan -> categoryParam = "Taman Hiburan"
                            ccTempatIbadah -> categoryParam = "Tempat Ibadah"
                            ccPusatPerbelanjaan -> categoryParam = "Pusat Perbelanjaan"
                        }

                        println("category param : ${categoryParam}")
                    }
                    else it.setChipStrokeColorResource(R.color.grey)
                }
            }
        }

        cgPrice.setOnCheckedStateChangeListener { group, checkedIds ->
            println("checked id cg price : ${group.checkedChipId}")
            println("is cheapest price chip checked : ${cpCheapest.isChecked}")
            println("is most expensive price chip checked : ${cpMostExpensive.isChecked}")

            if(checkedIds.isEmpty()) {
                columnParam = null
                filterParam = null
            }

            listOf(cpCheapest, cpMostExpensive).map {
                it.setOnCheckedChangeListener { _, selected ->
                    if (selected) {
                        it.setChipStrokeColorResource(R.color.blue)

                        columnParam = "price"

                        when (it) {
                            cpCheapest -> filterParam = "asc"
                            cpMostExpensive -> filterParam = "desc"
                        }

                        println("column param : ${columnParam}")
                        println("filter param : ${filterParam}")
                    }
                    else it.setChipStrokeColorResource(R.color.grey)
                }
            }
        }
    }

    private fun getRecommendationDestination() {
        RetrofitClient.instance.getRecommendationDestination("Bearer ${context?.applicationContext?.let {
            SharedPrefManager.getInstance(
                it
            ).token
        }}")
            .enqueue(object : Callback<AllDestinationResponse> {

                override fun onFailure(call: Call<AllDestinationResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                    pbRecommendationDestination.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<AllDestinationResponse>,
                    response: Response<AllDestinationResponse>
                ) {
                    pbRecommendationDestination.visibility = View.GONE

                    if(response.code() == 200) {
                        val data = response.body()?.data

                        if (data != null) {

                            if (data.isNotEmpty()) {
                                llRecommendationDestination.visibility = View.VISIBLE
                            }

                            recommendationDestinationList = data

                            // on below line we are initializing our course adapter
                            // and passing course list and context.
                            val recommendationDestinationAdapter = activity?.let { DestinationGridRVAdapter(destinationList = recommendationDestinationList, it.applicationContext) }

                            // on below line we are setting adapter to our grid view.
                            gvRecommendationDestination.adapter = recommendationDestinationAdapter

                            gvRecommendationDestination.setOnTouchListener { _, event ->

                                // event action is set as ACTION_MOVE
                                event.action == MotionEvent.ACTION_MOVE
                            }

                            // on below line we are adding on item
                            // click listener for our grid view.
                            gvRecommendationDestination.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                                // inside on click method we are simply displaying
                                // a toast message with course name.
                                var intent = Intent(context, DetailActivity::class.java).putExtra("id", recommendationDestinationList[position].id)
                                startActivity(intent)
                            }
                        }
                    }
                }

            })
    }

    private fun getAllDestination() {
        pbDestination.visibility = View.VISIBLE
        gvDestination.visibility = View.GONE

        RetrofitClient.instance.getAllDestination("Bearer ${context?.applicationContext?.let {
            SharedPrefManager.getInstance(
                it
            ).token
        }}", columnParam = columnParam ?: "rating", filterParam = filterParam ?: "desc", categoryParam = categoryParam, searchParam = etSearch.text.toString())
            .enqueue(object : Callback<AllDestinationResponse> {

            override fun onFailure(call: Call<AllDestinationResponse>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                pbDestination.visibility = View.GONE
                gvDestination.visibility = View.VISIBLE
            }

            override fun onResponse(
                call: Call<AllDestinationResponse>,
                response: Response<AllDestinationResponse>
            ) {
                pbDestination.visibility = View.GONE
                gvDestination.visibility = View.VISIBLE

                if(response.code() == 200) {
                    val data = response.body()?.data

                    if (data != null) {

                        destinationList = data.filter { item -> item.name?.lowercase()?.contains(etSearch.text.toString().lowercase()) ?: false}

                        // on below line we are initializing our course adapter
                        // and passing course list and context.
                        val destinationAdapter = activity?.let { DestinationGridRVAdapter(destinationList = destinationList, it.applicationContext) }

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
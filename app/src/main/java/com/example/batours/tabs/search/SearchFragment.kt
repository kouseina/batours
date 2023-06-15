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
    lateinit var etSearch: EditText

    lateinit var alertDialog: AlertDialog
    lateinit var btnDialogClose: TextView
    lateinit var cgRating: ChipGroup
    lateinit var cgCategory: ChipGroup
    lateinit var cgPrice: ChipGroup

    private val token: String = "Bearer ${context?.let {
        SharedPrefManager.getInstance(
            it
        ).token
    }}"

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

    }

    private fun showAlertDialog() {
        val view = layoutInflater.inflate(R.layout.filter_dialog_layout, null)
        btnDialogClose = view.findViewById(R.id.btn_close)
        cgRating = view.findViewById(R.id.cg_rating)
        cgCategory = view.findViewById(R.id.cg_category)
        cgPrice = view.findViewById(R.id.cg_price)

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
            if(checkedIds.isNotEmpty()) {
                val idx = checkedIds.first() - 1

                println("index : $idx")
                val cgItem = group.getChildAt(idx) as Chip

                cgItem.setOnCheckedChangeListener { compoundButton, selected ->
                    if (selected) {
                        cgItem.setChipStrokeColorResource(R.color.blue)

                        columnParam = "rating"

                        when(idx) {
                            0 -> filterParam = "asc"
                            1 -> filterParam = "desc"
                        }
                    }

                    else cgItem.setChipStrokeColorResource(R.color.grey)

                }
            }

            else {
                columnParam = null
                filterParam = null
            }
        }

        cgCategory.setOnCheckedStateChangeListener { group, checkedIds ->
            if(checkedIds.isNotEmpty()) {
                val idx = checkedIds.first() - 1 - cgRating.childCount
                val cgItem = group.getChildAt(idx) as Chip

                cgItem.setOnCheckedChangeListener { compoundButton, selected ->
                    if (selected) {
                        cgItem.setChipStrokeColorResource(R.color.blue)

                        when(idx) {
                            0 -> categoryParam = "Cagar Alam"
                            1 -> categoryParam = "Budaya"
                            2 -> categoryParam = "Taman Hiburan"
                            3 -> categoryParam = "Tempat Ibadah"
                            4 -> categoryParam = "Pusat Perbelanjaan"
                        }
                    }

                    else cgItem.setChipStrokeColorResource(R.color.grey)
                }
            }

            else categoryParam = null
        }

        cgPrice.setOnCheckedStateChangeListener { group, checkedIds ->
            if(checkedIds.isNotEmpty()) {
                val idx = checkedIds.first() - 1 - cgRating.childCount - cgCategory.childCount
                val cgItem = group.getChildAt(idx) as Chip

                cgItem.setOnCheckedChangeListener { compoundButton, selected ->
                    if (selected) {
                        cgItem.setChipStrokeColorResource(R.color.blue)

                        columnParam = "price"

                        when(idx) {
                            0 -> filterParam = "asc"
                            1 -> filterParam = "desc"
                        }
                    }

                    else cgItem.setChipStrokeColorResource(R.color.grey)
                }
            }

            else {
                columnParam = null
                filterParam = null
            }
        }
    }

    private fun getAllDestination() {
        pbDestination.visibility = View.VISIBLE
        gvDestination.visibility = View.GONE

        RetrofitClient.instance.getAllDestination(token, columnParam = columnParam ?: "rating", filterParam = filterParam ?: "desc", categoryParam = categoryParam)
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
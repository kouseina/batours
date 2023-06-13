package com.example.batours.tabs.search

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.batours.R
import com.example.batours.databinding.FragmentSearchBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    lateinit var btnFilter: Button

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

        showAlertDialog()

    }

    private fun showAlertDialog() {
        val view = layoutInflater.inflate(R.layout.filter_dialog_layout, null)

        val builder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        var dialog = builder.setView(view).create()

        btnFilter.setOnClickListener{view ->
            dialog.show()
        }

        val btnDialogClose: TextView = view.findViewById(R.id.btn_close)
        val cgRating: ChipGroup = view.findViewById(R.id.cg_rating)
        val cgCategory: ChipGroup = view.findViewById(R.id.cg_category)
        val cgPrice: ChipGroup = view.findViewById(R.id.cg_price)

        btnDialogClose.setOnClickListener{view ->
            dialog.dismiss()
        }

        cgRating.setOnCheckedStateChangeListener { group, checkedIds ->
            if(checkedIds.isNotEmpty()) {
                val cgItem = group.getChildAt(checkedIds.first() - 1) as Chip

                cgItem.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) cgItem.setChipStrokeColorResource(R.color.blue) else cgItem.setChipStrokeColorResource(R.color.grey)
                }
            }
        }

        cgCategory.setOnCheckedStateChangeListener { group, checkedIds ->
            if(checkedIds.isNotEmpty()) {
                val cgItem = group.getChildAt(checkedIds.first() - 1 - cgRating.childCount) as Chip

                cgItem.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) cgItem.setChipStrokeColorResource(R.color.blue) else cgItem.setChipStrokeColorResource(R.color.grey)
                }
            }
        }

        cgPrice.setOnCheckedStateChangeListener { group, checkedIds ->
            if(checkedIds.isNotEmpty()) {
                val cgItem = group.getChildAt(checkedIds.first() - 1 - cgRating.childCount - cgCategory.childCount) as Chip

                cgItem.setOnCheckedChangeListener { compoundButton, b ->
                    if (b) cgItem.setChipStrokeColorResource(R.color.blue) else cgItem.setChipStrokeColorResource(R.color.grey)
                }
            }
        }
    }
}
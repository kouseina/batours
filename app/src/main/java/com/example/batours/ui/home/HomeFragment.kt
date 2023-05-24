package com.example.batours.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.batours.*
import com.example.batours.GridRVAdapter
import com.example.batours.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var gvDestination: ExpandableHeightGridView
    lateinit var destinationList: List<GridViewModal>

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
        destinationList = ArrayList<GridViewModal>()

        // on below line we are adding data to
        // our course list with image and course name.
        destinationList = listOf(
            GridViewModal("Gedung Sate", R.drawable.img_destination1),
            GridViewModal("Sahyang Heulut", R.drawable.img_destination2),
            GridViewModal("Gedung Sate Padang", R.drawable.img_destination1),
            GridViewModal("Sahyang Heulut", R.drawable.img_destination2),
            GridViewModal("Gedung Sate", R.drawable.img_destination1),
            GridViewModal("Sahyang Heulut", R.drawable.img_destination2),
            GridViewModal("Gedung Sate Padang", R.drawable.img_destination1),
            GridViewModal("Sahyang Heulut", R.drawable.img_destination2),
        )

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
            println("hllooo")
        }
    }
}
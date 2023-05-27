package com.example.batours.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.batours.*
import com.example.batours.GridRVAdapter
import com.example.batours.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    lateinit var gvDestination : ExpandableHeightGridView
    lateinit var destinationList : List<GridViewModal>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val bookmarkViewModel =
            ViewModelProvider(this).get(BookmarkViewModel::class.java)

        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
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

        val destinationAdapter = activity?.let { GridRVAdapter(destinationList = destinationList, it.applicationContext) }
        gvDestination.adapter = destinationAdapter

        gvDestination.setOnTouchListener { _, event ->

            // event action is set as ACTION_MOVE
            event.action == MotionEvent.ACTION_MOVE
        }

        gvDestination.onItemClickListener = AdapterView.OnItemClickListener{_, _, position, _ ->
            var intent = Intent(context, DetailActivity::class.java)
            startActivity(intent)
        }
    }
}
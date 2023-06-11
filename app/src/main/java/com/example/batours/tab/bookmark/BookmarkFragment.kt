package com.example.batours.tab.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.batours.*
import com.example.batours.GridRVAdapter
import com.example.batours.databinding.FragmentBookmarkBinding
import com.example.batours.models.DestinationItem

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    lateinit var gvDestination : ExpandableHeightGridView
    lateinit var destinationList : List<DestinationItem>

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
        destinationList = ArrayList<DestinationItem>()

        destinationList = listOf(
            DestinationItem(id = 0, name = "Gedung Sate", image_url = "https://vervalyayasan.data.kemdikbud.go.id/upload/file/9F/9FD0/80640-7789672821873048630.jpg", category = "", description = "", maps_url = "", price = "", rating = ""),
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
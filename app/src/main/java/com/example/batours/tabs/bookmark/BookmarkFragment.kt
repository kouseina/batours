package com.example.batours.tabs.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.batours.*
import com.example.batours.adapters.BookmarkGridRVAdapter
import com.example.batours.api.RetrofitClient
import com.example.batours.databinding.FragmentBookmarkBinding
import com.example.batours.models.AllBookmarkResponse
import com.example.batours.models.BookmarkItem
import com.example.batours.storage.SharedPrefManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    lateinit var gvBookmark : ExpandableHeightGridView
    lateinit var bookmarkList : List<BookmarkItem>
    lateinit var pbBookmark: ProgressBar

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

        gvBookmark = view.findViewById(R.id.gv_bookmark)
        gvBookmark.isExpanded = true
        pbBookmark = view.findViewById(R.id.pb_bookmark)

        getAllBookmark()
    }

    private fun getAllBookmark() {
        RetrofitClient.instance.getAllBookmark("Bearer ${context?.applicationContext?.let {
            SharedPrefManager.getInstance(
                it
            ).token
        }}",).enqueue(object : Callback<AllBookmarkResponse> {
            override fun onResponse(
                call: Call<AllBookmarkResponse>,
                response: Response<AllBookmarkResponse>
            ) {
                pbBookmark.visibility = View.GONE

                if(response.code() == 200) {
                    val data = response.body()?.data

                    if(data != null) {
                        bookmarkList = data.reversed()

                        val bookmarkAdapter = activity?.let { BookmarkGridRVAdapter(bookmarkList = bookmarkList, it.applicationContext) }
                        gvBookmark.adapter = bookmarkAdapter

                        gvBookmark.setOnTouchListener { _, event ->

                            // event action is set as ACTION_MOVE
                            event.action == MotionEvent.ACTION_MOVE
                        }

                        gvBookmark.onItemClickListener = AdapterView.OnItemClickListener{ _, _, position, _ ->
                            var intent = Intent(context, DetailActivity::class.java).putExtra("id", bookmarkList[position].destination.id)
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<AllBookmarkResponse>, t: Throwable) {
                pbBookmark.visibility = View.GONE
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}
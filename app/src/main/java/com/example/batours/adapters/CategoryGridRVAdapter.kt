package com.example.batours.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.batours.R
import com.example.batours.models.CategoryItem
import com.example.batours.models.DestinationItem

internal class CategoryGridRVAdapter (
    // on below line we are creating two
    // variables for course list and context
    private val categoryList: List<CategoryItem>,
    private val context: Context
    ) : BaseAdapter() {

    // in base adapter class we are creating variables
    // for layout inflater, course image view and course text view.
    private var layoutInflater: LayoutInflater? = null
    private lateinit var imgBackground: ImageView
    private lateinit var tvTitle: TextView

    override fun getCount(): Int {
        return categoryList.count()
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView

        // on blow line we are checking if layout inflater
        // is null, if it is null we are initializing it.
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        // on the below line we are checking if convert view is null.
        // If it is null we are initializing it.
        if (convertView == null) {
            // on below line we are passing the layout file
            // which we have to inflate for each item of grid view.
            convertView = layoutInflater!!.inflate(R.layout.gridview_category_item, null)
        }

        // on below line we are initializing our course image view
        // and course text view with their ids.
        imgBackground = convertView!!.findViewById(R.id.img_background)
        tvTitle = convertView!!.findViewById(R.id.tv_title)

        // on below line we are setting image for our course image view.
        Glide.with(context)
            .load(categoryList[position].image_url)
            .centerCrop()
            .into(imgBackground)
        // on below line we are setting text in our course text view.
        tvTitle.setText(categoryList[position].category)

        return convertView
    }

}
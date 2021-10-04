package com.mymovie.Components.viewpager_home

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import android.view.LayoutInflater
import com.mymovie.R
import com.mymovie.Components.ItemObject
import com.mymovie.Components.RecyclerViewAdapterHome
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

@SuppressLint("ViewConstructor")
class HomeLayout(context: Context, foto: Array<String?>, title: Array<String?>, rating: Array<String?>) : LinearLayout(context) {
    init {
        val mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val ll_main = mLayoutInflater.inflate(R.layout.home_layout, this, false) as LinearLayout
        val rowListItem: MutableList<ItemObject> = ArrayList()
        for (i in foto.indices) {
            arrayOf(foto[i], title[i], rating[i])?.let { ItemObject(it) }?.let { rowListItem.add(it) }
        }
        val recyclerviewAdapterHome = RecyclerViewAdapterHome(rowListItem)
        val lLayout = LinearLayoutManager(getContext())
        lLayout.orientation = RecyclerView.VERTICAL
        val recyclerView: RecyclerView = ll_main.findViewById(R.id.rv_main)
        recyclerView.layoutManager = lLayout
        recyclerView.adapter = recyclerviewAdapterHome
        addView(ll_main)
    }
}
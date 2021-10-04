package com.mymovie.Components

import androidx.recyclerview.widget.RecyclerView
import com.mymovie.Components.RecyclerViewHolderPro
import android.widget.LinearLayout
import com.mymovie.Components.ItemObject
import com.mymovie.Components.RecyclerViewAdapterHome
import android.view.ViewGroup
import android.view.LayoutInflater
import com.mymovie.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.mymovie.DetailMovieActivity
import android.widget.TextView
import java.lang.Exception
import java.lang.NullPointerException
import java.net.URL

class RecyclerViewAdapterHome : RecyclerView.Adapter<RecyclerViewHolderPro> {
    private var layoutView: LinearLayout? = null
    var typeLayout: Byte = 0

    constructor() {}
    constructor(itemLists: List<ItemObject>?) {
        Companion.itemList = itemLists
        itemListBackup = itemLists
    }

    constructor(itemLists: List<ItemObject>?, TypeLayout: Byte) {
        Companion.itemList = itemLists
        typeLayout = TypeLayout
    }

    val itemList: List<ItemObject>?
        get() = Companion.itemList

    override fun getItemViewType(position: Int): Int {
        return 1 /*viewType = itemList.get(position).getLayoutType();*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolderPro {
        layoutView = LayoutInflater.from(parent.context).inflate(R.layout.cell_list_rv_home, null) as LinearLayout
        val lp = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.setMargins(0, 10, 0, 10)
        layoutView!!.layoutParams = lp
        return RecyclerViewHolderPro(layoutView)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolderPro, position: Int) { /*final int position*/
        try {
            val url_image = URL(Companion.itemList!![holder.adapterPosition].getItem(0))
            val bitmap = BitmapFactory.decodeStream(url_image.openConnection().getInputStream())
            val res = holder.root.resources
            val roundBMP = RoundedBitmapDrawableFactory.create(res, bitmap)
            holder.foto.background = roundBMP
            holder.title.text = Companion.itemList!![holder.adapterPosition].getItem(1)
            holder.root.setOnClickListener { v -> v.context.startActivity(Intent(v.context, DetailMovieActivity::class.java)) }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        try {
            return Companion.itemList!!.size
        } catch (ignored: NullPointerException) {
        }
        return 0
    }

    companion object {
        var itemList: List<ItemObject>? = null
        var itemListBackup: List<ItemObject>? = null
    }
}

class RecyclerViewHolderPro(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var root: LinearLayout
    var foto: ImageView
    var title: TextView

    init {
        root = itemView!!.findViewById(R.id.root)
        foto = itemView.findViewById(R.id.list_item_foto)
        title = itemView.findViewById(R.id.list_item_title)
    }
}
package com.mymovie.Components.viewpager_home

import android.content.Context
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mymovie.R
import java.lang.NullPointerException
import java.util.*

class ViewPagerAdapter(var context: Context, var linearLayout: Array<HomeLayout?>) : PagerAdapter() {
    var inflater: LayoutInflater? = null
    var ll_main: LinearLayout? = null
    override fun getCount(): Int {
        return if (linearLayout.size != -1) linearLayout.size else 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        ll_main = inflater!!.inflate(R.layout.viewpager_adapter_layout, container, false) as LinearLayout
        Objects.requireNonNull(container).addView(ll_main)
        try {
            linearLayout[position].layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            if (linearLayout[position].parent != null) {
                (linearLayout[position].parent as ViewGroup).removeView(linearLayout[position])
                ll_main!!.addView(linearLayout[position])
            } else ll_main!!.addView(linearLayout[position])
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return ll_main!!
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}
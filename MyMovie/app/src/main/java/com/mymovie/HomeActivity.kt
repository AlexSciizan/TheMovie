package com.mymovie

import com.mymovie.Components.Utility.Companion.cekKoneksi
import com.mymovie.Components.API.getGenre
import com.mymovie.Components.Utility.Companion.setupButtonGenre
import com.mymovie.Components.Utility.Companion.setupEventViewPager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import com.mymovie.Components.viewpager_home.HomeLayout
import android.annotation.SuppressLint
import androidx.viewpager.widget.ViewPager
import androidx.core.content.res.ResourcesCompat
import com.mymovie.Components.viewpager_home.ViewPagerAdapter
import java.lang.RuntimeException
import java.util.concurrent.ExecutionException

class HomeActivity : AppCompatActivity() {
    // https://www.themoviedb.org/settings/api
    // 5edbd3e1a85cfef0cdd0fa429f3809cd
    // https://www.themoviedb.org/documentation/api/discover
    var ll_bottom: LinearLayout? = null
    lateinit var id_genre: IntArray
    lateinit var tv_genre: Array<TextView?>
    var jo_genre: JsonObject? = null
    var ja_genre: JsonArray? = null
    lateinit var arr_foto: Array<String?>
    lateinit var arr_title: Array<String?>
    lateinit var arr_rating: Array<String?>
    lateinit var homeLayouts: Array<HomeLayout?>
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        ll_bottom = findViewById(R.id.ll_bottom)
        arr_title = arrayOfNulls(10)
        arr_foto = arrayOfNulls(10)
        arr_rating = arrayOfNulls(10)
        try {
            if (cekKoneksi(baseContext, false, true)) {
                jo_genre = getGenre(baseContext)
                ja_genre = jo_genre!!["genres"].asJsonArray
                id_genre = IntArray(ja_genre.size())
                tv_genre = arrayOfNulls(ja_genre.size())
                homeLayouts = arrayOfNulls(ja_genre.size())
                val ll_param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                ll_param.bottomMargin = 5
                ll_param.topMargin = 5
                ll_param.leftMargin = 10
                ll_param.rightMargin = 10
                val typeface = ResourcesCompat.getFont(baseContext, R.font.metal)
                for (i in 0 until ja_genre.size()) {
                    tv_genre[i] = TextView(baseContext)
                    id_genre[i] = ja_genre.get(i).asJsonObject["id"].asInt
                    tv_genre[i]!!.layoutParams = ll_param
                    tv_genre[i]!!.background = baseContext.resources.getDrawable(R.drawable.round_rec_background_button_white2)
                    tv_genre[i]!!.text = ja_genre.get(i).asJsonObject["name"].asString
                    tv_genre[i]!!.setTypeface(typeface)
                    setupButtonGenre(viewPager, tv_genre[i]!!, i)
                    ll_bottom.addView(tv_genre[i])
                }
                for (j in 0 until ja_genre.size()) {
                    homeLayouts[j] = HomeLayout(baseContext, arr_title, arr_foto, arr_rating)
                }
                tv_genre[0]!!.background = baseContext.resources.getDrawable(R.drawable.round_rec_background_button_white1)
            }
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        try {
            /* new HomeLayout[]{
                    new HomeLayout(getBaseContext(), arr1, arr2, arr3, (byte) 1),
                    new HomeLayout(getBaseContext(), arr1, arr2, arr3, (byte) 2) }
            */
            val vpa4 = ViewPagerAdapter(baseContext, homeLayouts)
            setupEventViewPager(viewPager, tv_genre)
            viewPager.adapter = vpa4
            viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        } catch (r: RuntimeException) {
            r.printStackTrace()
        }
    }
}
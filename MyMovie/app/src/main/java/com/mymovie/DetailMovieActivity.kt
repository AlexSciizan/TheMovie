package com.mymovie

import com.mymovie.Components.Utility.Companion.cekKoneksi
import com.mymovie.Components.API.getGenre
import com.mymovie.Components.Utility.Companion.setupButtonGenre
import com.mymovie.Components.Utility.Companion.setupEventViewPager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mymovie.R
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import com.mymovie.Components.viewpager_home.HomeLayout
import android.annotation.SuppressLint
import androidx.viewpager.widget.ViewPager
import com.mymovie.Components.API
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.mymovie.Components.viewpager_home.ViewPagerAdapter
import androidx.multidex.MultiDexApplication
import androidx.multidex.MultiDex

class DetailMovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)
    }
}
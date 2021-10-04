package com.mymovie.Components

import android.content.Context
import kotlin.Throws
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import java.util.concurrent.ExecutionException

object API {
    @JvmStatic
    @Throws(ExecutionException::class, InterruptedException::class)
    fun getGenre(context: Context?): JsonObject {
        return Ion.with(context).load("https://api.themoviedb.org/3/genre/movie/list?api_key=5edbd3e1a85cfef0cdd0fa429f3809cd&language=en-US").asJsonObject().get()
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun getMovieByGenre(context: Context?, id_genre: Int): JsonObject {
        return Ion.with(context).load("https://api.themoviedb.org/3/discover/movie?api_key=5edbd3e1a85cfef0cdd0fa429f3809cd&with_genres=$id_genre").asJsonObject().get()
    }
}
package com.engie.eea_tech_interview.helpers

import com.engie.eea_tech_interview.MainActivity

class Utils {

    companion object {
        @JvmStatic
        fun getPosterUrl(poster: String?): String {
            val imageUrl = "https://image.tmdb.org/t/p/w500"
            return "$imageUrl$poster?api_key=${MainActivity.MOVIE_API_KEY}"
        }
    }
}
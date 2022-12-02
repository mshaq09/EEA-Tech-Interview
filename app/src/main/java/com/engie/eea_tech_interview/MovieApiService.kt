package com.engie.eea_tech_interview

import com.engie.eea_tech_interview.model.GenreResult
import com.engie.eea_tech_interview.model.Movie
import com.engie.eea_tech_interview.model.SearchResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("search/movie")
    suspend fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
    ): SearchResult

    @GET("genre/movie/list")
    suspend fun getGenre(
        @Query("api_key") apiKey: String
    ): Response<GenreResult>

    @GET("trending/all/day")
    suspend fun getTrending(
        @Query("api_key") apiKey: String
    ): SearchResult

    @GET(" movie/{movie_id}")
    suspend fun getMovie(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Movie


}
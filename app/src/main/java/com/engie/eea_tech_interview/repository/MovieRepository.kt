package com.engie.eea_tech_interview.repository

import com.engie.eea_tech_interview.MovieApiService


class MovieRepository (private val movieApiService: MovieApiService) {

    suspend fun getSearchMovies(apiKey: String, query: String) = movieApiService.getMovies(apiKey, query)

    suspend fun getMovies(apiKey: String) = movieApiService.getTrending(apiKey)

    suspend fun getMovie(apiKey: String, movieId: Int) = movieApiService.getMovie(movieId, apiKey)

}
package com.engie.eea_tech_interview.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.engie.eea_tech_interview.helpers.LoadingState
import com.engie.eea_tech_interview.model.Movie
import com.engie.eea_tech_interview.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private var _data = MutableLiveData<List<Movie>>()

    val data: LiveData<List<Movie>>
        get() = _data

    private var _movie = MutableLiveData<Movie>()

    val movie: LiveData<Movie>
        get() = _movie

    fun fetchSearchData(apiKey: String, query: String) {
        viewModelScope.launch {
            try {
                _data.value = movieRepository.getSearchMovies(apiKey, query).results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchMovies(apiKey: String) {
        viewModelScope.launch {
            try {
                _data.value = movieRepository.getMovies(apiKey).results
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchMovie(apiKey: String, movieId: Int) {
        viewModelScope.launch {
            try {
                _movie.value = movieRepository.getMovie(apiKey, movieId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
package com.engie.eea_tech_interview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.engie.eea_tech_interview.adapters.MovieAdapter
import com.engie.eea_tech_interview.databinding.ActivityMainBinding
import com.engie.eea_tech_interview.viewModel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var movieAdapter: MovieAdapter? = null
    private val movieViewModel: MovieViewModel by viewModel()

    companion object {
        const val MOVIE_API_KEY = "47304f18bd4a3b4e733196b18e68bfbc"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val numberOfColumns = 2
        binding.recyclerView.layoutManager = GridLayoutManager(this, numberOfColumns)

        binding.search.doAfterTextChanged {
            movieViewModel.fetchSearchData(MOVIE_API_KEY, it.toString())
        }

        movieViewModel.data.observe(this
        ) {
            movieAdapter = MovieAdapter(it)
            binding.recyclerView.adapter = movieAdapter
            movieAdapter?.onItemClick = { movie ->
                // do something with your item
                val intent = Intent(this, MovieDetailsActivity::class.java)
                intent.putExtra("movie", movie.id)
                startActivity(intent)
            }
        }

        movieViewModel.fetchMovies(MOVIE_API_KEY)
    }
}

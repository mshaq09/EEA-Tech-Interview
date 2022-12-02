package com.engie.eea_tech_interview

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.engie.eea_tech_interview.databinding.ActivityMovieDetailsBinding
import com.engie.eea_tech_interview.helpers.Utils
import com.engie.eea_tech_interview.viewModel.MovieViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieDetailsBinding
    private val movieViewModel: MovieViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val movieId: Int = intent.extras?.getInt("movie", 0) ?: 0
        init(movieId)
    }

    private fun init(movieId: Int){

        movieViewModel.movie.observe(this
        ) { movie ->
            binding.title.text = movie.title
            binding.description.text = movie.overview
            binding.date.text = "Release Date: ${movie.releaseDate}"
            binding.rating.text = "Rating: ${movie.voteCount}"
            binding.genre.text = "Genre: ${movie.genres.toString()}"

            if (movie.posterPath != null) {
                Glide.with(this)
                    .load(Utils.getPosterUrl(movie.posterPath))
                    .centerCrop()
                    .placeholder(R.drawable.eea_mobile_logo)
                    .into(binding.poster)
            } else {
                binding.poster.setImageResource(R.drawable.eea_mobile_logo)
            }

            binding.progressBar.visibility = View.GONE

        }

        movieViewModel.fetchMovie(MainActivity.MOVIE_API_KEY, movieId)
    }
}

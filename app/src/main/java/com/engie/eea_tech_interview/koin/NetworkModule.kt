package com.engie.eea_tech_interview.koin

import com.engie.eea_tech_interview.MovieApiService
import com.engie.eea_tech_interview.network.createOkHttpClient
import com.engie.eea_tech_interview.network.createMoshiConverter
import com.engie.eea_tech_interview.network.createRetrofit
import com.engie.eea_tech_interview.repository.MovieRepository
import com.engie.eea_tech_interview.viewModel.MovieViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module(createdAtStart = true) {
    single {
        val baseUrl = "https://api.themoviedb.org/3/"
        createRetrofit(baseUrl, get(), get())
    }

    single { createOkHttpClient(androidContext()) }

    single { createMoshiConverter() }
}

val apiModule = module {
    fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }

    single { provideMovieApiService(get()) }
}

val viewModelModule = module {

    // ViewModel for Result View
    viewModel { MovieViewModel(get()) }
}

val repositoryModule = module {
    fun provideMovieRepository(api: MovieApiService): MovieRepository {
        return MovieRepository(api,)
    }

    single { provideMovieRepository(get()) }
}
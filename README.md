# EEA-Tech-Interview

Welcome to the Engie Energy Access Android code challenge!

The project you're about to clone aims at displaying a list of movies fetched from [The Movie Database API](https://developers.themoviedb.org/3/getting-started/introduction) and creating
a detail page for these movies accessible through each item in the list.

Your task is to implement such flow through different steps:
1. a `RecyclerView` in the main screen of the app showing a preview of the movie poster, the movie title and, if possible, a release date using the data fetched through the API calls.
2. an `Intent` fired from each item in the `ReyclerView` that opens a new activity/fragment (it depends on how you want to structure your project)
3. a Details page that shows in a more detailed way the characteristics of each movie (synopsis, a larger poster, genre, rating, etc)

What we ask is for you to build this project without having any particular constraints. 

You can pick your preferred type of architecture (MVP, MVVM, Clean Architecture, etc) and you can use all the third-party libraries you want.

Only two requests coming from our side:
1. You don't spend more than 3-4 hours writing the code for this project
2. Be as creative as you desire, that's for sure appreciated!


# Tech Stack
The project, as you might have noticed, is not completely empty. In order to make you life easier, we've implemented already the networking layer 
and the dependency injection structure. Here the list of libraries used:
- Koin
- Retrofit
- OkHttp
- Moshi
- AndroidX

The project also uses the Movie Database API for fetching some movies. <br >
We've therefore already added some `data class`es that can be use to create objects for better mapping the JSON coming out from the API calls.

Here's an example:<br />
```kotlin
@JsonClass(generateAdapter = true)
data class Movie(
    @Json(name = "id") val id: Int,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "overview") val overview: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "original_title") val originalTitle: String?,
    @Json(name = "genre_ids") val genreIds: List<Int>?,
    @Json(name = "media_type") val mediaType: String?,
    @Json(name = "original_language") val originalLanguage: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "vote_count") val voteCount: Int?,
    @Json(name = "video") val hasVideo: Boolean?,
)
```

In the `model` package in the project, you will find other classes already created that might help with dealing with data in a simpler way. 

A portion of the networking layer has been already added as well: Retrofit, OkHttp, and the related builder methods can be used already as 
object you can inject in your activities/fragments.

And an interface acting as a service for Retrofit is available for adding new API calls or using what's already in there.<br />
```kotlin
interface MovieApiService {
    @GET("search/movie")
    fun getMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
    ): Call<SearchResult>

    @GET("genre/movie/list")
    fun getGenre(
        @Query("api_key") apiKey: String
    ): Call<GenreResult>
}
```

Don't worry about the API KEY, you will find it in the project as a `const val` in the `MainActivity` class.

# Unit tests
What we would like to see is that the code created is being tested throuroughly, whether it is part of `ViewModel` classes, part of use case implementations or part of something to the effect of a repository pattern.<br />
We consider this a very important component of the skillset of any Android developers and that is why we ask you to take some time for writing tests for whatever logic you've created.

# The world is your oyster
Bear in mind that the code provided is just a starting point and it's not necessary to follow what we've built.<br />
Feel free to create something unique that's going to allow us to better understand how creative you are.

You can play around with the Movie API calls or you can create a custom UI that reflects your experience and mind. You can also add nice transition animations if you want! <br />
In a nutshell, bring anything you think could improve the quality of the project and show to us what you can do.<br />
But remember not to push too hard and only do things that you can finish within the expected timeframe. <br />
We will take the time constraint into account while looking at the results. 

Last but not least, since we are taking advantage of the Movie API, what best way to wish you good luck if not with a movie quote:<br />
> “Sometimes it is the people who no one imagines anything of who do the things that no one can imagine.”<br />
–Alan Turing (The Imitation Game)

You can submit your project either using your own Github or through a zip file.<br />
Once you think you've completed the task, please send an email to niccolo.passolunghi@engie.com with a link to your project!<br />
Use the same email account in case you have questions.

Enjoy the ride!

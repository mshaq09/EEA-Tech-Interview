package com.engie.eea_tech_interview.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.engie.eea_tech_interview.MainActivity
import com.engie.eea_tech_interview.model.Movie
import com.engie.eea_tech_interview.model.Resource
import com.engie.eea_tech_interview.model.SearchResult
import com.engie.eea_tech_interview.repository.MovieRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.lang.reflect.Type


@RunWith(JUnit4::class)
internal class MovieViewModelTest {

    private lateinit var viewModel: MovieViewModel
    private lateinit var movieRepository: MovieRepository
    private lateinit var moviesObserver: Observer<List<Movie>>
    private lateinit var movieObserver: Observer<Movie>
    private val apiKey = MainActivity.MOVIE_API_KEY
    private val stringMovieResource = "{\"adult\":false,\"backdrop_path\":null,\"belongs_to_collection\":null,\"budget\":0,\"genres\":[{\"id\":18,\"name\":\"Drama\"}],\"homepage\":\"\",\"id\":111837,\"imdb_id\":\"tt0756237\",\"original_language\":\"hu\",\"original_title\":\"Fövenyóra\",\"overview\":\"A story of a Jewish-Hungarian-Serbian family. The plot is centered about trying of an adult son to reconstruct his childhood dream of and memories. Looms, but a sunken world, the central European town with its figures, the way to experiencing deep and see a sensitive boy, his father, who by their fears that penetrate to the bone and the final humiliation of acting madness escapes and becomes a lonely, pathetic buffoon in the drama of life.\",\"popularity\":1.229,\"poster_path\":null,\"production_companies\":[{\"id\":28394,\"logo_path\":\"/uayh0bscJCf5StAz27R0XRyZKuj.png\",\"name\":\"Art & Popcorn\",\"origin_country\":\"RS\"},{\"id\":41356,\"logo_path\":null,\"name\":\"Artikulacija\",\"origin_country\":\"ME\"},{\"id\":46499,\"logo_path\":null,\"name\":\"Atalanta\",\"origin_country\":\"\"},{\"id\":60719,\"logo_path\":null,\"name\":\"Duna Mühely\",\"origin_country\":\"HU\"},{\"id\":120779,\"logo_path\":null,\"name\":\"Mediawave 2000\",\"origin_country\":\"HU\"}],\"production_countries\":[{\"iso_3166_1\":\"HU\",\"name\":\"Hungary\"},{\"iso_3166_1\":\"RS\",\"name\":\"Serbia\"}],\"release_date\":\"2007-10-02\",\"revenue\":0,\"runtime\":110,\"spoken_languages\":[{\"english_name\":\"Hungarian\",\"iso_639_1\":\"hu\",\"name\":\"Magyar\"},{\"english_name\":\"Serbian\",\"iso_639_1\":\"sr\",\"name\":\"Srpski\"}],\"status\":\"Released\",\"tagline\":\"\",\"title\":\"Hourglass\",\"video\":false,\"vote_average\":6.2,\"vote_count\":4}"
    private val stringResource = "{\"page\":1,\"results\":[{\"adult\":false,\"backdrop_path\":\"/53BC9F2tpZnsGno2cLhzvGprDYS.jpg\",\"id\":736526,\"title\":\"Troll\",\"original_language\":\"no\",\"original_title\":\"Troll\",\"overview\":\"Deep inside the mountain of Dovre, something gigantic awakens after being trapped for a thousand years. Destroying everything in its path, the creature is fast approaching the capital of Norway. But how do you stop something you thought only existed in Norwegian folklore?\",\"poster_path\":\"/ulgKdif3ubMACDltr8VZy6fyVjW.jpg\",\"media_type\":\"movie\",\"genre_ids\":[14,28,12],\"popularity\":204.845,\"release_date\":\"2022-12-01\",\"video\":false,\"vote_average\":7.016,\"vote_count\":31},{\"adult\":false,\"backdrop_path\":\"/93SxdkiR3gBcbG5FxIt0DCBttul.jpg\",\"id\":111837,\"name\":\"Willow\",\"original_language\":\"en\",\"original_name\":\"Willow\",\"overview\":\"Many years after the events of the original film, legendary sorcerer Willow leads a group of misfit heroes on a dangerous rescue mission through a world beyond their wildest imaginations.\",\"poster_path\":\"/jhdSPDlhswjN1r6O0pGP3ZvQgU8.jpg\",\"media_type\":\"tv\",\"genre_ids\":[10765,10759,18],\"popularity\":142.493,\"first_air_date\":\"2022-11-30\",\"vote_average\":5.952,\"vote_count\":21,\"origin_country\":[\"US\"]},{\"adult\":false,\"backdrop_path\":\"/iHSwvRVsRyxpX7FE7GbviaDvgGZ.jpg\",\"id\":119051,\"name\":\"Wednesday\",\"original_language\":\"en\",\"original_name\":\"Wednesday\",\"overview\":\"Wednesday Addams is sent to Nevermore Academy, a bizarre boarding school where she attempts to master her psychic powers, stop a monstrous killing spree of the town citizens, and solve the supernatural mystery that affected her family 25 years ago — all while navigating her new relationships.\",\"poster_path\":\"/jeGtaMwGxPmQN5xM4ClnwPQcNQz.jpg\",\"media_type\":\"tv\",\"genre_ids\":[10765,9648,35],\"popularity\":3834.212,\"first_air_date\":\"2022-11-23\",\"vote_average\":8.821,\"vote_count\":1246,\"origin_country\":[\"US\"]},{\"adult\":false,\"backdrop_path\":\"/83IYtUhc7Vs8XYi5UYc2lUKuyMo.jpg\",\"id\":873126,\"title\":\"My Name Is Vendetta\",\"original_language\":\"it\",\"original_title\":\"Il mio nome è vendetta\",\"overview\":\"After old enemies kill his family, a former mafia enforcer and his feisty daughter flee to Milan, where they hide out while plotting their revenge.\",\"poster_path\":\"/5bwGuv3YyqXy6QEkaq8YBTVUoxO.jpg\",\"media_type\":\"movie\",\"genre_ids\":[28,12,80,9648,53],\"popularity\":51.965,\"release_date\":\"2022-11-30\",\"video\":false,\"vote_average\":6.031,\"vote_count\":16},{\"adult\":false,\"backdrop_path\":\"/rfnmMYuZ6EKOBvQLp2wqP21v7sI.jpg\",\"id\":774752,\"title\":\"The Guardians of the Galaxy Holiday Special\",\"original_language\":\"en\",\"original_title\":\"The Guardians of the Galaxy Holiday Special\",\"overview\":\"On a mission to make Christmas unforgettable for Quill, the Guardians head to Earth in search of the perfect present.\",\"poster_path\":\"/8dqXyslZ2hv49Oiob9UjlGSHSTR.jpg\",\"media_type\":\"movie\",\"genre_ids\":[35,878,12],\"popularity\":1057.592,\"release_date\":\"2022-11-25\",\"video\":false,\"vote_average\":7.5,\"vote_count\":450},{\"adult\":false,\"backdrop_path\":\"/bQXAqRx2Fgc46uCVWgoPz5L5Dtr.jpg\",\"id\":436270,\"title\":\"Black Adam\",\"original_language\":\"en\",\"original_title\":\"Black Adam\",\"overview\":\"Nearly 5,000 years after he was bestowed with the almighty powers of the Egyptian gods—and imprisoned just as quickly—Black Adam is freed from his earthly tomb, ready to unleash his unique form of justice on the modern world.\",\"poster_path\":\"/pFlaoHTZeyNkG83vxsAJiGzfSsa.jpg\",\"media_type\":\"movie\",\"genre_ids\":[28,14,878],\"popularity\":10405.742,\"release_date\":\"2022-10-19\",\"video\":false,\"vote_average\":7.303,\"vote_count\":2267},{\"adult\":false,\"backdrop_path\":\"/4tsZ4rWTUyFROVaFXMMBPQIJ940.jpg\",\"id\":945897,\"title\":\"A Man of Action\",\"original_language\":\"es\",\"original_title\":\"Un hombre de acción\",\"overview\":\"Loosely inspired by the life of Lucio Urtubia, explore the figure of the so-called anarchist 'Robin Hood,' who ran a legendary counterfeiting operation in Paris that put him in the crosshairs of America's largest bank, when he managed to obtain a huge amount of money by forging traveler's checks to invest in causes he believed in.\",\"poster_path\":\"/gL6lwQE5WrdQrMiisjma0uIonpT.jpg\",\"media_type\":\"movie\",\"genre_ids\":[18,36],\"popularity\":51.778,\"release_date\":\"2022-11-30\",\"video\":false,\"vote_average\":8.0,\"vote_count\":5},{\"adult\":false,\"backdrop_path\":\"/wvIV32bGg6HC4dOCyXIHk9q4Lgt.jpg\",\"id\":1042984,\"title\":\"Christmas Full of Grace\",\"original_language\":\"pt\",\"original_title\":\"Um Natal Cheio de Graça\",\"overview\":\"After discovering a betrayal, Carlinhos takes a fun stranger to accompany him on Christmas. But Graça proves to be a madwoman capable of bringing the traditional family home down.\",\"poster_path\":\"/iHRphB4zfPTuUHVrbO2vTmCLuHO.jpg\",\"media_type\":\"movie\",\"genre_ids\":[10749,35],\"popularity\":64.753,\"release_date\":\"2022-11-30\",\"video\":false,\"vote_average\":6.0,\"vote_count\":8},{\"adult\":false,\"backdrop_path\":\"/198vrF8k7mfQ4FjDJsBmdQcaiyq.jpg\",\"id\":76600,\"title\":\"Avatar: The Way of Water\",\"original_language\":\"en\",\"original_title\":\"Avatar: The Way of Water\",\"overview\":\"Set more than a decade after the events of the first film, learn the story of the Sully family (Jake, Neytiri, and their kids), the trouble that follows them, the lengths they go to keep each other safe, the battles they fight to stay alive, and the tragedies they endure.\",\"poster_path\":\"/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg\",\"media_type\":\"movie\",\"genre_ids\":[878,28,12],\"popularity\":646.277,\"release_date\":\"2022-12-14\",\"video\":false,\"vote_average\":0.0,\"vote_count\":0},{\"adult\":false,\"backdrop_path\":\"/xDMIl84Qo5Tsu62c9DGWhmPI67A.jpg\",\"id\":505642,\"title\":\"Black Panther: Wakanda Forever\",\"original_language\":\"en\",\"original_title\":\"Black Panther: Wakanda Forever\",\"overview\":\"Queen Ramonda, Shuri, M’Baku, Okoye and the Dora Milaje fight to protect their nation from intervening world powers in the wake of King T’Challa’s death. As the Wakandans strive to embrace their next chapter, the heroes must band together with the help of War Dog Nakia and Everett Ross and forge a new path for the kingdom of Wakanda.\",\"poster_path\":\"/ps2oKfhY6DL3alynlSqY97gHSsg.jpg\",\"media_type\":\"movie\",\"genre_ids\":[28,12,878],\"popularity\":1716.673,\"release_date\":\"2022-11-09\",\"video\":false,\"vote_average\":7.547,\"vote_count\":1116},{\"adult\":false,\"backdrop_path\":\"/eFkaDLCP2GJiAp54kwPqKnkELU5.jpg\",\"id\":214258,\"name\":\"Dead End\",\"original_language\":\"pl\",\"original_name\":\"Pewnego razu na krajowej jedynce\",\"overview\":\"A group of people sharing a ride accidentally switches cars with a bank robber, who then pursues them to retrieve the stolen money he left in the trunk.\",\"poster_path\":\"/j4yasPJg4bZLKv18lYZNc30xZM3.jpg\",\"media_type\":\"tv\",\"genre_ids\":[18,35],\"popularity\":34.635,\"first_air_date\":\"2022-12-01\",\"vote_average\":8.0,\"vote_count\":2,\"origin_country\":[\"PL\"]},{\"adult\":false,\"backdrop_path\":\"/79PcXPpbDWql74h8Y00mNwbYMbS.jpg\",\"id\":664469,\"title\":\"Amsterdam\",\"original_language\":\"en\",\"original_title\":\"Amsterdam\",\"overview\":\"In the 1930s, three friends—a doctor, a nurse, and an attorney—witness a murder, become suspects themselves and uncover one of the most outrageous plots in North American history.\",\"poster_path\":\"/6sJcVzGCwrDCBMV0DU6eRzA2UxM.jpg\",\"media_type\":\"movie\",\"genre_ids\":[80,35,36,9648,53],\"popularity\":157.124,\"release_date\":\"2022-09-27\",\"video\":false,\"vote_average\":6.146,\"vote_count\":394},{\"adult\":false,\"backdrop_path\":\"/ujrrlw1ZwOMPe4kM09KydH2mGZe.jpg\",\"id\":447365,\"title\":\"Guardians of the Galaxy Volume 3\",\"original_language\":\"en\",\"original_title\":\"Guardians of the Galaxy Volume 3\",\"overview\":\"Peter Quill, still reeling from the loss of Gamora, must rally his team around him to defend the universe along with protecting one of their own. A mission that, if not completed successfully, could quite possibly lead to the end of the Guardians as we know them.\",\"poster_path\":\"/nhW1LYXG2lLlRC9ZlgC1xZ12rO3.jpg\",\"media_type\":\"movie\",\"genre_ids\":[28,12,878],\"popularity\":51.404,\"release_date\":\"2023-05-03\",\"video\":false,\"vote_average\":0.0,\"vote_count\":0},{\"adult\":false,\"backdrop_path\":\"/jQo42k2k6tNTP8XBjUQIMlxbqvc.jpg\",\"id\":1028938,\"title\":\"A Hollywood Christmas\",\"original_language\":\"en\",\"original_title\":\"A Hollywood Christmas\",\"overview\":\"Jessica, a young, up-and-coming filmmaker in Hollywood has made a name for herself directing Christmas movies. But when handsome network executive Christopher shows up threatening to halt production on her latest movie, Jessica’s assistant, Reena, points out the irony: Jessica isn’t just trying to save her Christmas movie, she’s actually living in one. Jessica must now juggle all the classic tropes—her actors falling in and out of love, a wayward elf dog, and her own stirring romantic feelings for her perceived nemesis—in order to get her movie and her life to their happy endings.\",\"poster_path\":\"/f8D6dobydMiuabqRDQi0598ghc4.jpg\",\"media_type\":\"movie\",\"genre_ids\":[35,10751],\"popularity\":49.661,\"release_date\":\"2022-12-01\",\"video\":false,\"vote_average\":6.2,\"vote_count\":4},{\"adult\":false,\"backdrop_path\":\"/hIZFG7MK4leU4axRFKJWqrjhmxZ.jpg\",\"id\":95403,\"name\":\"The Peripheral\",\"original_language\":\"en\",\"original_name\":\"The Peripheral\",\"overview\":\"Stuck in a small Appalachian town, a young woman’s only escape from the daily grind is playing advanced video games. She is such a good player that a company sends her a new video game system to test…but it has a surprise in store. It unlocks all of her dreams of finding a purpose, romance, and glamour in what seems like a game…but it also puts her and her family in real danger.\",\"poster_path\":\"/ccBe5BVeibdBEQU7l6P6BubajWV.jpg\",\"media_type\":\"tv\",\"genre_ids\":[10765,18],\"popularity\":1117.479,\"first_air_date\":\"2022-10-20\",\"vote_average\":8.188,\"vote_count\":313,\"origin_country\":[\"US\"]},{\"adult\":false,\"backdrop_path\":\"/jTiPtn42YIplu51zOs67BjpkE96.jpg\",\"id\":934419,\"title\":\"Qala\",\"original_language\":\"hi\",\"original_title\":\"कला\",\"overview\":\"Haunted by her past, a talented singer with a rising career copes with the pressure of success, a mother's disdain and the voices of doubt within her.\",\"poster_path\":\"/k6iP3MWpbLutrXc58pznVtXRzPG.jpg\",\"media_type\":\"movie\",\"genre_ids\":[18],\"popularity\":12.667,\"release_date\":\"2022-11-24\",\"video\":false,\"vote_average\":4.0,\"vote_count\":1},{\"adult\":false,\"backdrop_path\":\"/c1bz69r0v065TGFA5nqBiKzPDys.jpg\",\"id\":830784,\"title\":\"Lyle, Lyle, Crocodile\",\"original_language\":\"en\",\"original_title\":\"Lyle, Lyle, Crocodile\",\"overview\":\"When the Primm family moves to New York City, their young son Josh struggles to adapt to his new school and new friends. All of that changes when he discovers Lyle — a singing crocodile who loves baths, caviar and great music — living in the attic of his new home. But when Lyle’s existence is threatened by evil neighbor Mr. Grumps, the Primms must band together to show the world that family can come from the most unexpected places.\",\"poster_path\":\"/irIS5Tn3TXjNi1R9BpWvGAN4CZ1.jpg\",\"media_type\":\"movie\",\"genre_ids\":[35,10751,10402],\"popularity\":1719.318,\"release_date\":\"2022-10-07\",\"video\":false,\"vote_average\":7.711,\"vote_count\":102},{\"adult\":false,\"backdrop_path\":\"/degHe3BrOS74SSlEa51Sicgwqqc.jpg\",\"id\":95249,\"name\":\"Gossip Girl\",\"original_language\":\"en\",\"original_name\":\"Gossip Girl\",\"overview\":\"Eight years after the original website went dark, a new generation of New York private school teens are introduced to the social surveillance of Gossip Girl.\",\"poster_path\":\"/9oY4OR0jQUbnKC5BhoUOQRrgIp9.jpg\",\"media_type\":\"tv\",\"genre_ids\":[18,9648],\"popularity\":96.524,\"first_air_date\":\"2021-07-08\",\"vote_average\":7.7,\"vote_count\":217,\"origin_country\":[\"US\"]},{\"adult\":false,\"backdrop_path\":\"/s1xnjbOIQtwGObPnydTebp74G2c.jpg\",\"id\":90669,\"name\":\"1899\",\"original_language\":\"zh\",\"original_name\":\"1899\",\"overview\":\"Passengers on an immigrant ship traveling to the new continent get caught in a mysterious riddle when they find a second vessel adrift on the open sea.\",\"poster_path\":\"/8KGvYHQNOamON6ufQGjyhkiVn1V.jpg\",\"media_type\":\"tv\",\"genre_ids\":[9648,18],\"popularity\":1552.014,\"first_air_date\":\"2022-11-17\",\"vote_average\":7.908,\"vote_count\":373,\"origin_country\":[\"DE\"]},{\"adult\":false,\"backdrop_path\":\"/AaV1YIdWKnjAIAOe8UUKBFm327v.jpg\",\"id\":361743,\"title\":\"Top Gun: Maverick\",\"original_language\":\"en\",\"original_title\":\"Top Gun: Maverick\",\"overview\":\"After more than thirty years of service as one of the Navy’s top aviators, and dodging the advancement in rank that would ground him, Pete “Maverick” Mitchell finds himself training a detachment of TOP GUN graduates for a specialized mission the likes of which no living pilot has ever seen.\",\"poster_path\":\"/62HCnUTziyWcpDaBO2i1DX17ljH.jpg\",\"media_type\":\"movie\",\"genre_ids\":[28,18],\"popularity\":450.775,\"release_date\":\"2022-05-24\",\"video\":false,\"vote_average\":8.3,\"vote_count\":4878}],\"total_pages\":1000,\"total_results\":20000}\n"

    var gson: Gson = Gson()
    var type: Type = object : TypeToken<SearchResult>() {}.type
    private val moviesSuccessResource: SearchResult  = gson.fromJson(stringResource, type)

    var type_movie: Type = object : TypeToken<Movie>() {}.type
    private val movieSuccessResource: Movie  = gson.fromJson(stringMovieResource, type_movie)
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        movieRepository = mock()
        runBlocking {
            whenever(movieRepository.getMovie(apiKey, 123)).thenReturn(movieSuccessResource)
            whenever(movieRepository.getMovies(apiKey)).thenReturn(moviesSuccessResource)
        }
        viewModel = MovieViewModel(movieRepository)
        movieObserver = mock()
        moviesObserver = mock()
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `when getMovie is called with valid movie id, then observer is updated with success`() = runBlocking {
        viewModel.movie.observeForever(movieObserver)
        viewModel.fetchMovie(apiKey,1234)
        delay(500)
        verify(movieRepository).getMovie(apiKey,1234)
        verify(movieObserver, timeout(500)).onChanged(movieSuccessResource)
    }

    @Test
    fun `when getMovies is called with invalid location, then observer is updated with failure`() = runBlocking {
        viewModel.data.observeForever(moviesObserver)
        viewModel.fetchMovies(apiKey)
        delay(10)
        verify(movieRepository).getMovies(apiKey)
        verify(moviesObserver, timeout(500)).onChanged(moviesSuccessResource.results)
    }
}
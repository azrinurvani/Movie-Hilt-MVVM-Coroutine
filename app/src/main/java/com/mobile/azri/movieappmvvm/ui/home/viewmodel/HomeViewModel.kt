package com.mobile.azri.movieappmvvm.ui.home.viewmodel

import android.content.Context

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.azri.movieappmvvm.model.Movie
import com.mobile.azri.movieappmvvm.model.MoviesResponse
import com.mobile.azri.movieappmvvm.ui.home.repository.HomeRepository
import com.mobile.azri.movieappmvvm.util.Resource
import com.mobile.azri.movieappmvvm.util.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

//TODO 24 - Create ViewModel for retrieve repository
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    @ApplicationContext private val context:Context
) : ViewModel() {

    val moviePopular : MutableLiveData<Resource<MoviesResponse?>?> = MutableLiveData()
    var movieListResponse : MoviesResponse? = null
    var moviePage = 1

    val topMovies : MutableLiveData<Resource<MoviesResponse?>?> = MutableLiveData()
    var topMoviesListResponse : MoviesResponse? = null
    var topMoviesPage = 1

//    fun fetchPopular(apiKey:String){
//        moviePopular.postValue(Resource.Loading())
//        viewModelScope.launch {
//            try {
//                if (hasInternetConnection((context))){
//                    val response = homeRepository.fetchPopular(apiKey)
//                    Log.d(TAG, "fetchPopular: response : $response")
//                    if (response!=null){
//                        val responseBody = response.body()
//                        if (response.body()!=null){
//                            moviePopular.postValue(Resource.Success(responseBody))
//                        }else{
//                            moviePopular.postValue(Resource.Error("Response Body is NULL"))
//                        }
//                    }else{
//                        moviePopular.postValue(Resource.Error("Response is NULL"))
//                    }
//                }else{
//                    moviePopular.postValue(Resource.Error("No Internet Connection"))
//                }
//            }catch (ex:Exception){
//                when (ex){
//                    is IOException -> moviePopular.postValue(Resource.Error("Network Failure "+ex.localizedMessage))
//                    else -> moviePopular.postValue(Resource.Error("Conversion Error"))
//                }
//            }
//        }
//    }

    fun fetchPopular(apiKey:String) = viewModelScope.launch {
        safeMovieCall(apiKey,moviePage)
    }

    private suspend fun safeMovieCall(apiKey: String, moviePage: Int?) {
        moviePopular.postValue(Resource.Loading())
        try{
            if (hasInternetConnection(context)){
                val response = homeRepository.fetchPopular(apiKey,moviePage)
                moviePopular.postValue(handleOrderResponse(response))
            }else{
                moviePopular.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (ex:Exception){
            when (ex){
                is IOException -> {
                    moviePopular.postValue(Resource.Error("Network Failure"))
                }
                else -> {
                    moviePopular.postValue(Resource.Error("Conversion Error"))
                }
            }
        }
    }

    private fun handleOrderResponse(response: Response<MoviesResponse>): Resource<MoviesResponse?> {
        if (response.isSuccessful){
            response.body().let { resultResponse->
                moviePage++
                if (movieListResponse == null){
                    movieListResponse = resultResponse
                }else{
                    val oldMovies = movieListResponse?.movies as ArrayList<Movie>?
                    val newMovies = resultResponse?.movies as ArrayList<Movie>?
                    newMovies?.let { oldMovies?.addAll(it) }
                }
                return Resource.Success(movieListResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun fetchTopRateMovies(apiKey: String) = viewModelScope.launch {
        safeApiTopMoviesCall(apiKey,topMoviesPage)
    }

    private suspend fun safeApiTopMoviesCall(apiKey: String,page:Int?){
        topMovies.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(context)){
                val response = homeRepository.fetchTopRated(apiKey,page)
                return topMovies.postValue(handleTopResponse(response))
            }else{
                topMovies.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (ex:Exception){
            when(ex){
                is IOException ->{
                    topMovies.postValue(Resource.Error("Network Failure"))
                }
                else -> {
                    topMovies.postValue(Resource.Error("Conversion Error"))
                }
            }
        }
    }

    private fun handleTopResponse(response: Response<MoviesResponse>): Resource<MoviesResponse?>{
        if (response.isSuccessful){
            response.body().let { resultResponse ->
                topMoviesPage++
                if (topMoviesListResponse == null){
                    topMoviesListResponse = resultResponse
                }else{
                    val oldTopMovies = topMoviesListResponse?.movies as ArrayList<Movie>?
                    val newTopMovies = resultResponse?.movies as ArrayList<Movie>?
                    newTopMovies?.let { oldTopMovies?.addAll(it) }
                }
                return Resource.Success(topMoviesListResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /*Note : Bagian UI dan implemen navigation pada activity belum */
    companion object {
        const val TAG ="HomeViewModel"
    }
}
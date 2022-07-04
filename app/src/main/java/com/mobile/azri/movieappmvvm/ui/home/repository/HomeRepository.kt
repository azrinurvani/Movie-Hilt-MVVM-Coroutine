package com.mobile.azri.movieappmvvm.ui.home.repository

import com.mobile.azri.movieappmvvm.model.MoviesResponse
import com.mobile.azri.movieappmvvm.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

//TODO 23 - Create Home Repository
@Singleton
class HomeRepository @Inject constructor(
    private val movieAppService: ApiService
) {

    suspend fun fetchPopular(apiKey:String,page:Int? = null) : Response<MoviesResponse> = withContext(Dispatchers.IO){
        val popular = movieAppService.getPopularMovies(apiKey,page)
        popular
    }

    suspend fun fetchTopRated(apiKey:String, page: Int?) : Response<MoviesResponse> = withContext(Dispatchers.IO){
        val top = movieAppService.getTopRatedMovies(apiKey,page)
        top
    }
}
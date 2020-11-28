package com.example.tvshowsapp.network

import com.example.tvshowsapp.responses.TvShowDetailsResponse
import com.example.tvshowsapp.responses.TvShowsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("most-popular")
    fun getMostPopularTvShows(@Query("page") page: Int): Call<TvShowsResponse>

    @GET("show-details")
    fun getTvShowDetails(@Query("q") tvShowId: String): Call<TvShowDetailsResponse>

    @GET("search")
    fun searchTvShow(@Query("q") query: String, @Query("page") page: Int): Call<TvShowsResponse>

}
package com.example.tvshowsapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tvshowsapp.network.ApiClient
import com.example.tvshowsapp.network.ApiService
import com.example.tvshowsapp.responses.TvShowsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MostPopularTvShowsRepository {

    var apiService: ApiService? = null

    init {
        apiService = ApiClient.getRetrofitClient()?.create(ApiService::class.java)
    }

    fun getMostPopularTvShows(page: Int): LiveData<TvShowsResponse>{
        val data = MutableLiveData<TvShowsResponse>()

        apiService?.getMostPopularTvShows(page)?.enqueue(object : Callback<TvShowsResponse> {
            override fun onResponse(
                call: Call<TvShowsResponse>,
                response: Response<TvShowsResponse>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<TvShowsResponse>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }
}
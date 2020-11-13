package com.example.tvshowsapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tvshowsapp.network.ApiClient
import com.example.tvshowsapp.network.ApiService
import com.example.tvshowsapp.responses.TvShowDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TvShowDetailsRepository {

    private var apiService: ApiService? = null

    init {
        apiService = ApiClient.getRetrofitClient()?.create(ApiService::class.java)
    }

    fun getTvShowDetails(tvShowId: String): LiveData<TvShowDetailsResponse> {
        val data = MutableLiveData<TvShowDetailsResponse>()

        apiService?.getTvShowDetails(tvShowId)?.enqueue(object : Callback<TvShowDetailsResponse> {
            override fun onResponse(
                call: Call<TvShowDetailsResponse>,
                response: Response<TvShowDetailsResponse>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<TvShowDetailsResponse>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }


}
package com.example.tvshowsapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {
        private var retrofit: Retrofit? = null

        fun getRetrofitClient(): Retrofit? {
            if (retrofit == null){
                retrofit = Retrofit.Builder()
                    .baseUrl(" https://www.episodate.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }
    }
}
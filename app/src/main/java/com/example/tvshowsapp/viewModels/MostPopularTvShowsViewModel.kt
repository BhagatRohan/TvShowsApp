package com.example.tvshowsapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tvshowsapp.repositories.MostPopularTvShowsRepository
import com.example.tvshowsapp.responses.TvShowsResponse

class MostPopularTvShowsViewModel: ViewModel(){

    var mostPopularTvShowsRepository : MostPopularTvShowsRepository? = null

    init {
        mostPopularTvShowsRepository = MostPopularTvShowsRepository()
    }

    fun getMostPopularTvShows(page: Int): LiveData<TvShowsResponse>? {
        return mostPopularTvShowsRepository?.getMostPopularTvShows(page)
    }
}
package com.example.tvshowsapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tvshowsapp.repositories.SearchTvShowRepository
import com.example.tvshowsapp.responses.TvShowsResponse

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    var searchTvShowRepository: SearchTvShowRepository? = null

    init {
        searchTvShowRepository = SearchTvShowRepository()
    }

    fun searchTvShow(query: String, page: Int): LiveData<TvShowsResponse>? {
        return searchTvShowRepository?.searchtvShow(query, page)
    }
}
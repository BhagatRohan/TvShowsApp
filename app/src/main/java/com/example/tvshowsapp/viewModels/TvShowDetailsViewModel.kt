package com.example.tvshowsapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tvshowsapp.repositories.TvShowDetailsRepository
import com.example.tvshowsapp.responses.TvShowDetailsResponse

class TvShowDetailsViewModel : ViewModel() {

    private var tvShowDetailsRepository: TvShowDetailsRepository? = null

    init {
        tvShowDetailsRepository = TvShowDetailsRepository()
    }

    fun getTvShowDetails(tvShowId: String): LiveData<TvShowDetailsResponse>? {
        return tvShowDetailsRepository?.getTvShowDetails(tvShowId)
    }

}
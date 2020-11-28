package com.example.tvshowsapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.tvshowsapp.database.TvShowDatabase
import com.example.tvshowsapp.models.TvShow
import com.example.tvshowsapp.repositories.TvShowDetailsRepository
import com.example.tvshowsapp.responses.TvShowDetailsResponse
import io.reactivex.Completable
import io.reactivex.Flowable

class TvShowDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private var tvShowDetailsRepository: TvShowDetailsRepository? = null
    private var tvShowDatabase: TvShowDatabase?=null

    init {
        tvShowDetailsRepository = TvShowDetailsRepository()
        tvShowDatabase = TvShowDatabase.getTvShowsDatabase(application)
    }

    fun getTvShowDetails(tvShowId: String): LiveData<TvShowDetailsResponse>? {
        return tvShowDetailsRepository?.getTvShowDetails(tvShowId)
    }

    fun addToWatchList(tvShow: TvShow): Completable?{
        return tvShowDatabase?.tvShowDao()?.addToWatchList(tvShow)
    }

    fun getTvShowFromWatchlist(tvShowId: String): Flowable<TvShow>?{
        return tvShowDatabase?.tvShowDao()?.getTvShowFromWatchlist(tvShowId)
    }

    fun removeTvShowFromWatchlist(tvShow: TvShow): Completable?{
        return tvShowDatabase?.tvShowDao()?.removeFromWatchList(tvShow)
    }

}
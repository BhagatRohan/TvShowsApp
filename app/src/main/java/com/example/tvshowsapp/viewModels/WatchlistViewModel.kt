package com.example.tvshowsapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tvshowsapp.database.TvShowDatabase
import com.example.tvshowsapp.models.TvShow
import io.reactivex.Completable
import io.reactivex.Flowable

class WatchlistViewModel(application: Application) : AndroidViewModel(application) {

    private var tvShowDatabase: TvShowDatabase? = null

    init {
        tvShowDatabase = TvShowDatabase.getTvShowsDatabase(application)
    }

    fun loadWatchlist(): Flowable<List<TvShow>>?{
        return tvShowDatabase?.tvShowDao()?.getWatchList()
    }

    fun removeTvShowFromWatchlist(tvShow: TvShow): Completable?{
        return tvShowDatabase?.tvShowDao()?.removeFromWatchList(tvShow)
    }
}
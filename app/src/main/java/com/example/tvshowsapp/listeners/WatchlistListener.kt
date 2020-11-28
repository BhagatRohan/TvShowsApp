package com.example.tvshowsapp.listeners

import com.example.tvshowsapp.models.TvShow

interface WatchlistListener {

    fun onTvShowClicked(tvShow: TvShow)

    fun removeTvShowFromWatchlist(tvShow: TvShow, position: Int)
}
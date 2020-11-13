package com.example.tvshowsapp.listeners

import com.example.tvshowsapp.models.TvShow

interface TvShowsListener {
    fun onTvShowClicked(tvShow: TvShow)
}
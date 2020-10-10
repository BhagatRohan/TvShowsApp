package com.example.tvshowsapp.responses

import com.example.tvshowsapp.models.TvShow
import com.google.gson.annotations.SerializedName

data class TvShowsResponse(
    @SerializedName("page") var page: Int,
    @SerializedName("pages") var pages: Int,
    @SerializedName("tv_shows") var tvShows: List<TvShow>,

)
package com.example.tvshowsapp.models

import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("season") var season: String = "",
    @SerializedName("episode") var episode: String = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("air_date") var airDate: String = ""
)
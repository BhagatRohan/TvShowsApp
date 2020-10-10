package com.example.tvshowsapp.models

import com.google.gson.annotations.SerializedName

data class TvShow(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("start_date") var startDate: String,
    @SerializedName("country") var country: String,
    @SerializedName("network") var network: String,
    @SerializedName("status") var status: String,
    @SerializedName("image_thumbnail_path") var thumbnail: String
)
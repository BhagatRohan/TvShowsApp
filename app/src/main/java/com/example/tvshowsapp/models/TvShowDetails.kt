package com.example.tvshowsapp.models

import com.google.gson.annotations.SerializedName

data class TvShowDetails(
    @SerializedName("url") var url: String? = "",
    @SerializedName("description") var description: String? = "",
    @SerializedName("runtime") var runtime: String? = "",
    @SerializedName("image_path") var imagePath: String = "",
    @SerializedName("rating") var rating: String? = "",
    @SerializedName("genres") var genre: ArrayList<String>? = arrayListOf(),
    @SerializedName("pictures") var pictures: ArrayList<String>? = arrayListOf(),
    @SerializedName("episodes") var episodes: ArrayList<Episode>? = arrayListOf()
)
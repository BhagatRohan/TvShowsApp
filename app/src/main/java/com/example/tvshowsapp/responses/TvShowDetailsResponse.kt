package com.example.tvshowsapp.responses

import com.example.tvshowsapp.models.TvShowDetails
import com.google.gson.annotations.SerializedName

data class TvShowDetailsResponse(
    @SerializedName("tvShow") var tvShowDetails: TvShowDetails? = null
)
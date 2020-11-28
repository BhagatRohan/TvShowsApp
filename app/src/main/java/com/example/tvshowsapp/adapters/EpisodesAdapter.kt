package com.example.tvshowsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshowsapp.R
import com.example.tvshowsapp.databinding.ItemContainerEpisodeBinding
import com.example.tvshowsapp.models.Episode

class EpisodesAdapter(val episodes: List<Episode>) :
    RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {

    inner class EpisodeViewHolder(
        private val itemContainerEpisodeBinding: ItemContainerEpisodeBinding,
    ) : RecyclerView.ViewHolder(itemContainerEpisodeBinding.root) {

        fun bindEpisode(episode: Episode) {
            var title = "S"
            var season = episode.season
            if (season.length == 1)
                season = "0 $season"

            var episodeNumber = episode.episode
            if (episodeNumber.length == 1)
                episodeNumber = "0 $episodeNumber"

            episodeNumber = "E $episodeNumber"
            title = "$title $season $episodeNumber"
            itemContainerEpisodeBinding.title = title
            itemContainerEpisodeBinding.name = episode.name
            itemContainerEpisodeBinding.airDate = episode.airDate

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val itemContainerEpisodeBinding = DataBindingUtil.inflate<ItemContainerEpisodeBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_container_episode,
            parent,
            false
        )
        return EpisodeViewHolder(itemContainerEpisodeBinding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bindEpisode(episodes[position])
    }

    override fun getItemCount() = episodes.size
}
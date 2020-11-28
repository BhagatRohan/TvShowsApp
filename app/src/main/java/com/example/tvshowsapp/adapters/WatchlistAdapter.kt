package com.example.tvshowsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshowsapp.R
import com.example.tvshowsapp.databinding.ItemContainerTvShowBinding
import com.example.tvshowsapp.listeners.TvShowsListener
import com.example.tvshowsapp.listeners.WatchlistListener
import com.example.tvshowsapp.models.TvShow

class WatchlistAdapter(private val tvShows: List<TvShow>, private val watchlistListener: WatchlistListener) :
    RecyclerView.Adapter<WatchlistAdapter.TvShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DataBindingUtil.inflate<ItemContainerTvShowBinding>(
            inflater,
            R.layout.item_container_tv_show,
            parent,
            false
        )

        return TvShowViewHolder(binding)
    }

    override fun getItemCount(): Int = tvShows.size

    inner class TvShowViewHolder(private val binding: ItemContainerTvShowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTvShow(tvShow: TvShow) {
            binding.tvShow = tvShow
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                watchlistListener.onTvShowClicked(tvShow)
            }
            binding.imageDelete.setOnClickListener {
                watchlistListener.removeTvShowFromWatchlist(tvShow, adapterPosition)
            }
            binding.imageDelete.visibility = View.VISIBLE
        }
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.bindTvShow(tvShows[position])
    }
}
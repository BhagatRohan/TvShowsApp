package com.example.tvshowsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshowsapp.R
import com.example.tvshowsapp.databinding.ItemContainerTvShowBinding
import com.example.tvshowsapp.models.TvShow

class TvShowsAdapter(private val tvShows: List<TvShow>) :
    RecyclerView.Adapter<TvShowsAdapter.TvShowViewHolder>() {

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
        }
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        holder.bindTvShow(tvShows[position])
    }
}
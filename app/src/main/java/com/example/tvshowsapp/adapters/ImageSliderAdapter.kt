package com.example.tvshowsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshowsapp.R
import com.example.tvshowsapp.databinding.ItemContainerSliderImageBinding

class ImageSliderAdapter(val sliderImages: ArrayList<String>) :
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    inner class ImageSliderViewHolder(private val itemContainerSliderImageBinding: ItemContainerSliderImageBinding) :
        RecyclerView.ViewHolder(itemContainerSliderImageBinding.root) {
        fun bindSliderImage(imageURL: String) {
            itemContainerSliderImageBinding.imageURL = imageURL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {

        val sliderImageBinding = DataBindingUtil.inflate<ItemContainerSliderImageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_container_slider_image,
            parent,
            false
        )

       return ImageSliderViewHolder(sliderImageBinding)
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.bindSliderImage(sliderImages[position])
    }

    override fun getItemCount() = sliderImages.size
}
package com.example.tvshowsapp.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class BindingAdapters{

    companion object{

        @BindingAdapter("android:imageURL")
        @JvmStatic
        fun setImageURL(imageView: ImageView, URL: String){
            try {
                imageView.alpha = 0f
                Picasso.get().load(URL).noFade().into(imageView, object : Callback{
                    override fun onSuccess() {
                        imageView.animate().setDuration(300).alpha(1f).start()
                    }

                    override fun onError(e: Exception?) {
                        TODO("Not yet implemented")
                    }

                })
            }catch (exec: Exception){

            }
        }
    }



}
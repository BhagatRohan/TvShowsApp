package com.example.tvshowsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tvshowsapp.R
import com.example.tvshowsapp.responses.TvShowsResponse
import com.example.tvshowsapp.viewModels.MostPopularTvShowsViewModel

class MainActivity : AppCompatActivity() {

    private var viewModel: MostPopularTvShowsViewModel? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MostPopularTvShowsViewModel::class.java)
        getMostPopularTvShows()
    }

    private fun getMostPopularTvShows(){
        viewModel?.getMostPopularTvShows(0)?.observe(this,
            {
                Toast.makeText(applicationContext, "Total Pages: " + it.pages, Toast.LENGTH_SHORT).show()
            }
        )
    }
}
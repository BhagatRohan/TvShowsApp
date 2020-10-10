package com.example.tvshowsapp.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.tvshowsapp.R
import com.example.tvshowsapp.adapters.TvShowsAdapter
import com.example.tvshowsapp.databinding.ActivityMainBinding
import com.example.tvshowsapp.models.TvShow
import com.example.tvshowsapp.viewModels.MostPopularTvShowsViewModel

class MainActivity : AppCompatActivity() {

    private var viewModel: MostPopularTvShowsViewModel? = null
    private var activityMainBinding: ActivityMainBinding? = null
    private var tvShowsAdapter: TvShowsAdapter? = null
    private val tvShows = ArrayList<TvShow>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        donInitialization()
    }

    private fun getMostPopularTvShows() {
        activityMainBinding?.isLoading = true
        viewModel?.getMostPopularTvShows(0)?.observe(this,
            { response ->
                activityMainBinding?.isLoading = false
                if (response != null) {
                    if (!response.tvShows.isNullOrEmpty()) {
                        tvShows.addAll(response.tvShows)
                        tvShowsAdapter?.notifyDataSetChanged()
                    }
                }
            }
        )
    }

    private fun donInitialization() {
        activityMainBinding?.tvShowsRecyclerView?.setHasFixedSize(true)
        viewModel = ViewModelProvider(this).get(MostPopularTvShowsViewModel::class.java)
        tvShowsAdapter = TvShowsAdapter(tvShows)
        activityMainBinding?.tvShowsRecyclerView?.adapter = tvShowsAdapter
        getMostPopularTvShows()
    }
}
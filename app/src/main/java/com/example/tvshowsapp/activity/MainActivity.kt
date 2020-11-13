package com.example.tvshowsapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshowsapp.R
import com.example.tvshowsapp.adapters.TvShowsAdapter
import com.example.tvshowsapp.databinding.ActivityMainBinding
import com.example.tvshowsapp.listeners.TvShowsListener
import com.example.tvshowsapp.models.TvShow
import com.example.tvshowsapp.viewModels.MostPopularTvShowsViewModel

class MainActivity : AppCompatActivity(), TvShowsListener {

    private var viewModel: MostPopularTvShowsViewModel? = null
    private var activityMainBinding: ActivityMainBinding? = null
    private var tvShowsAdapter: TvShowsAdapter? = null
    private val tvShows = ArrayList<TvShow>()
    private var currentPage = 1
    private var totalAvailablePages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        donInitialization()
    }

    private fun getMostPopularTvShows() {
        toggleLoading()
        viewModel?.getMostPopularTvShows(currentPage)?.observe(this,
            { response ->
                toggleLoading()
                if (response != null) {
                    totalAvailablePages = response.pages
                    if (!response.tvShows.isNullOrEmpty()) {
                        val oldCount = tvShows.count()
                        tvShows.addAll(response.tvShows)
                        tvShowsAdapter?.notifyItemRangeInserted(oldCount, tvShows.size)
                    }
                }
            }
        )
    }

    private fun donInitialization() {
        activityMainBinding?.tvShowsRecyclerView?.setHasFixedSize(true)
        viewModel = ViewModelProvider(this).get(MostPopularTvShowsViewModel::class.java)
        tvShowsAdapter = TvShowsAdapter(tvShows, this)
        activityMainBinding?.tvShowsRecyclerView?.adapter = tvShowsAdapter
        activityMainBinding?.tvShowsRecyclerView?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (activityMainBinding?.tvShowsRecyclerView?.canScrollVertically(1) == true){
                    if (currentPage <= totalAvailablePages){
                        currentPage +=1
                        getMostPopularTvShows()
                    }
                }
            }
        })
        getMostPopularTvShows()
    }

    private fun toggleLoading(){
        if (currentPage ==1){
            activityMainBinding?.isLoading =
                !(activityMainBinding?.isLoading !=null && activityMainBinding?.isLoading == true)
        } else{
            activityMainBinding?.isLoadingMore =
                !(activityMainBinding?.isLoadingMore !=null && activityMainBinding?.isLoadingMore == true)
        }
    }

    override fun onTvShowClicked(tvShow: TvShow) {
        val intent = Intent(applicationContext, TvShowDetailsActivity::class.java).apply {
            putExtra("id", tvShow.id)
            putExtra("name", tvShow.name)
            putExtra("startDate", tvShow.startDate)
            putExtra("country", tvShow.country)
            putExtra("network", tvShow.network)
            putExtra("status", tvShow.status)
        }
        startActivity(intent)
    }
}
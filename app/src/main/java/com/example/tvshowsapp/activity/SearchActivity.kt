package com.example.tvshowsapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshowsapp.R
import com.example.tvshowsapp.adapters.TvShowsAdapter
import com.example.tvshowsapp.databinding.ActivitySearchBinding
import com.example.tvshowsapp.listeners.TvShowsListener
import com.example.tvshowsapp.models.TvShow
import com.example.tvshowsapp.viewModels.SearchViewModel
import java.util.*

class SearchActivity : AppCompatActivity(), TvShowsListener {

    private var activitySearchBinding: ActivitySearchBinding? = null
    private var viewModel: SearchViewModel? = null
    private var tvShows: MutableList<TvShow> = mutableListOf()
    private var tvShowAdapter: TvShowsAdapter? = null
    private var currentPage = -1
    private var totalAvailablePages = 1
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        doInitialization()
    }

    private fun doInitialization() {
        activitySearchBinding?.imageBack?.setOnClickListener { onBackPressed() }
        activitySearchBinding?.tvShowsRecyclerView?.setHasFixedSize(true)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        tvShowAdapter = TvShowsAdapter(tvShows, this)
        activitySearchBinding?.tvShowsRecyclerView?.adapter = tvShowAdapter
        activitySearchBinding?.inputSearch?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (timer != null)
                    timer?.cancel()
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().trim().isNotEmpty()) {
                    timer = Timer()
                    timer?.schedule(object : TimerTask() {
                        override fun run() {
                            Handler(Looper.getMainLooper()).post {
                                currentPage = 1
                                totalAvailablePages = 1
                                searchTvShow(s.toString())
                            }
                        }
                    }, 800)
                } else {
                    tvShows.clear()
                    tvShowAdapter?.notifyDataSetChanged()
                }
            }
        })

        activitySearchBinding?.tvShowsRecyclerView?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (activitySearchBinding?.tvShowsRecyclerView?.canScrollVertically(1) == false) {
                    if (activitySearchBinding?.inputSearch?.text.toString().isNotEmpty()) {
                        if (currentPage < totalAvailablePages) {
                            currentPage += 1
                            searchTvShow(activitySearchBinding?.inputSearch?.text.toString())
                        }
                    }
                }
            }
        })
        activitySearchBinding?.inputSearch?.requestFocus()
    }

    private fun searchTvShow(query: String) {
        toggleLoading()
        viewModel?.searchTvShow(query, currentPage)?.observe(this, { tvShowResponse ->
            toggleLoading()
            if (tvShowResponse != null) {
                totalAvailablePages = tvShowResponse.pages
                val oldCount = tvShows.size
                tvShows.addAll(tvShowResponse.tvShows)
                tvShowAdapter?.notifyItemRangeInserted(oldCount, tvShows.size)
            }

        })
    }

    override fun onTvShowClicked(tvShow: TvShow) {
        val intent = Intent(applicationContext, TvShowDetailsActivity::class.java).apply {
            putExtra("tvShow", tvShow)
        }
        startActivity(intent)
    }

    private fun toggleLoading() {
        if (currentPage == 1) {
            activitySearchBinding?.isLoading =
                !(activitySearchBinding?.isLoading != null && activitySearchBinding?.isLoading == true)
        } else {
            activitySearchBinding?.isLoadingMore =
                !(activitySearchBinding?.isLoadingMore != null && activitySearchBinding?.isLoadingMore == true)
        }
    }
}
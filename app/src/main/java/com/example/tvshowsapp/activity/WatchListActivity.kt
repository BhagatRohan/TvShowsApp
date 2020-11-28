package com.example.tvshowsapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.tvshowsapp.R
import com.example.tvshowsapp.adapters.WatchlistAdapter
import com.example.tvshowsapp.databinding.ActivityWatchListBinding
import com.example.tvshowsapp.listeners.WatchlistListener
import com.example.tvshowsapp.models.TvShow
import com.example.tvshowsapp.utils.TempDataHolder
import com.example.tvshowsapp.viewModels.WatchlistViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class WatchListActivity : AppCompatActivity(), WatchlistListener {

    private var activityWatchListBinding: ActivityWatchListBinding? = null
    private var watchlistViewModel: WatchlistViewModel? = null
    private var watchlistAdapter: WatchlistAdapter? = null
    private var watchList: MutableList<TvShow>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityWatchListBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_watch_list)

        doInitialization()
    }

    private fun doInitialization() {
        watchlistViewModel = ViewModelProvider(this).get(WatchlistViewModel::class.java)
        activityWatchListBinding?.imageBack?.setOnClickListener { onBackPressed() }
        watchList = mutableListOf()
        loadWatchlist()
    }

    private fun loadWatchlist() {
        activityWatchListBinding?.isLoading = true
        val compositeDisposable = CompositeDisposable()
        watchlistViewModel?.loadWatchlist()?.subscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {tvShow->
                activityWatchListBinding?.isLoading = false
                        if (watchList?.isNotEmpty() == true){
                            watchList?.clear()
                        }
                watchList?.addAll(tvShow)
                watchlistAdapter = watchList?.let { it1 -> WatchlistAdapter(it1, this) }
                activityWatchListBinding?.watchlistRecyclerView?.adapter = watchlistAdapter
                activityWatchListBinding?.watchlistRecyclerView?.visibility = View.VISIBLE
                compositeDisposable.dispose()
            }?.let {
                compositeDisposable.add(
                    it
                )
            }
    }

    override fun onResume() {
        super.onResume()
        if (TempDataHolder.IS_WATCHLIST_UPDATED){
            loadWatchlist()
            TempDataHolder.IS_WATCHLIST_UPDATED = false
        }

    }

    override fun onTvShowClicked(tvShow: TvShow) {
        val intent = Intent(applicationContext, TvShowDetailsActivity::class.java)
        intent.putExtra("tvShow", tvShow)
        startActivity(intent)
    }

    override fun removeTvShowFromWatchlist(tvShow: TvShow, position: Int) {
        val compositeDisposableForDelete = CompositeDisposable()
        watchlistViewModel?.removeTvShowFromWatchlist(tvShow)?.subscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe {
                watchList?.removeAt(position)
                watchlistAdapter?.notifyItemRemoved(position)
                watchlistAdapter?.itemCount?.let {
                    watchlistAdapter?.notifyItemRangeChanged(position,
                        it
                    )
                }
                compositeDisposableForDelete.dispose()
            }?.let {
                compositeDisposableForDelete.add(
                    it
                )
            }
    }
}
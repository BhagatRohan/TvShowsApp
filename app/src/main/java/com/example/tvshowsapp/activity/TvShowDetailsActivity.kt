package com.example.tvshowsapp.activity

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.tvshowsapp.R
import com.example.tvshowsapp.adapters.EpisodesAdapter
import com.example.tvshowsapp.adapters.ImageSliderAdapter
import com.example.tvshowsapp.databinding.ActivityTvShowDetailsBinding
import com.example.tvshowsapp.databinding.LayoutEpisodesBottomSheetBinding
import com.example.tvshowsapp.models.TvShow
import com.example.tvshowsapp.utils.TempDataHolder
import com.example.tvshowsapp.viewModels.TvShowDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class TvShowDetailsActivity : AppCompatActivity() {
    private var binding: ActivityTvShowDetailsBinding? = null
    private var tvShowsDetailViewModel: TvShowDetailsViewModel? = null
    private var episodeBottomSheetDialog: BottomSheetDialog? = null
    private var layoutEpisodesBottomSheetBinding: LayoutEpisodesBottomSheetBinding? = null
    private var tvShow: TvShow? = null
    private var isTvShowAvailableInWatchlist = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tv_show_details)
        doInitialization()
    }

    private fun doInitialization() {
        tvShowsDetailViewModel = ViewModelProvider(this).get(TvShowDetailsViewModel::class.java)
        binding?.imageback?.setOnClickListener { onBackPressed() }
        tvShow = intent.getSerializableExtra("tvShow") as TvShow
        checkTvShowInWatchlist()
        getTvShowDetails()
    }

    private fun checkTvShowInWatchlist() {
        val compositeDisposable = CompositeDisposable()
        tvShowsDetailViewModel?.getTvShowFromWatchlist(tvShow?.id.toString())
            ?.subscribeOn(Schedulers.computation())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { tvShow ->
                isTvShowAvailableInWatchlist = true
                binding?.imageWatchList?.setImageResource(R.drawable.ic_added)
                compositeDisposable.dispose()
            }?.let {
                compositeDisposable.add(
                    it
                )
            }
    }

    private fun getTvShowDetails() {
        binding?.isLoading = true
        val tvShowId = (tvShow?.id).toString()
        Log.v("Show Id", tvShowId)

        tvShowsDetailViewModel?.getTvShowDetails(tvShowId)?.observe(this, { tvShowDetailResponse ->
            binding?.run {
                isLoading = false
                tvShowDetailResponse?.tvShowDetails?.pictures?.let { loadImageSlider(it) }
                tvShowImageURL = tvShowDetailResponse?.tvShowDetails?.imagePath
                imageTvShow.visibility = View.VISIBLE
                description = tvShowDetailResponse?.tvShowDetails?.description?.let {
                    HtmlCompat.fromHtml(
                        it, HtmlCompat.FROM_HTML_MODE_LEGACY
                    )
                }.toString()
                textDescription.visibility = View.VISIBLE
                textReadMore.visibility = View.VISIBLE
                textReadMore.setOnClickListener {
                    if (textReadMore.text.toString() == "Read More") {
                        textDescription.maxLines = Int.MAX_VALUE
                        textDescription.ellipsize = null
                        textReadMore.text = getString(R.string.read_less)
                    } else {
                        textDescription.maxLines = 4
                        textDescription.ellipsize = TextUtils.TruncateAt.END
                        textReadMore.text = getString(R.string.read_more)
                    }
                }

                rating = String.format(
                    Locale.getDefault(),
                    "%.2f",
                    tvShowDetailResponse?.tvShowDetails?.rating?.toDouble()
                )
                genre = "N/A"
                tvShowDetailResponse?.tvShowDetails?.genre?.let { genre = it[0] }
                runtime = tvShowDetailResponse?.tvShowDetails?.runtime + " Min"
                viewDivider1.visibility = View.VISIBLE
                viewDivider2.visibility = View.VISIBLE
                layoutMisc.visibility = View.VISIBLE

                buttonWebsite.setOnClickListener {
                    val intent = Intent().apply {
                        data = Uri.parse(tvShowDetailResponse?.tvShowDetails?.url)
                    }
                    startActivity(intent)
                }
                buttonWebsite.visibility = View.VISIBLE
                buttonEpisodes.visibility = View.VISIBLE

                buttonEpisodes.setOnClickListener {
                    if (episodeBottomSheetDialog == null) {
                        episodeBottomSheetDialog = BottomSheetDialog(this@TvShowDetailsActivity)
                        layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                            LayoutInflater.from(this@TvShowDetailsActivity),
                            R.layout.layout_episodes_bottom_sheet,
                            findViewById(R.id.episodesContainer),
                            false
                        )

                        layoutEpisodesBottomSheetBinding?.root?.let {
                            episodeBottomSheetDialog?.setContentView(
                                it
                            )
                        }

                        layoutEpisodesBottomSheetBinding?.episodesRecyclerView?.adapter =
                            tvShowDetailResponse?.tvShowDetails?.episodes?.let {
                                EpisodesAdapter(
                                    it
                                )
                            }

                        layoutEpisodesBottomSheetBinding?.textTitle?.text =
                            String.format("Episodes | %s", tvShow?.name)
                        layoutEpisodesBottomSheetBinding?.imageClose?.setOnClickListener {
                            episodeBottomSheetDialog?.dismiss()
                        }
                    }

                    val frameLayout =
                        episodeBottomSheetDialog?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                    if (frameLayout != null) {
                        val bottomSheetBehavior = BottomSheetBehavior.from(frameLayout)
                        bottomSheetBehavior.peekHeight =
                            Resources.getSystem().displayMetrics.heightPixels
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }

                    episodeBottomSheetDialog?.show()
                }

                binding?.imageWatchList?.setOnClickListener {
                    val compositeDisposable = CompositeDisposable()

                    if (isTvShowAvailableInWatchlist){
                        tvShow?.let { tvShow ->
                            tvShowsDetailViewModel?.removeTvShowFromWatchlist(tvShow)
                                ?.subscribeOn(Schedulers.computation())
                                ?.observeOn(AndroidSchedulers.mainThread())
                                ?.subscribe {
                                    isTvShowAvailableInWatchlist = false
                                    TempDataHolder.IS_WATCHLIST_UPDATED = true
                                    binding?.imageWatchList?.setImageResource(R.drawable.ic_watchlist)
                                    Toast.makeText(
                                        applicationContext,
                                        "Removed from WatchList",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    compositeDisposable.dispose()
                                }
                        }?.let { compositeDisposable.add(it) }
                    }else{
                        tvShow?.let { tvShow ->
                            tvShowsDetailViewModel?.addToWatchList(tvShow)
                                ?.subscribeOn(Schedulers.io())
                                ?.observeOn(AndroidSchedulers.mainThread())
                                ?.subscribe {
                                    TempDataHolder.IS_WATCHLIST_UPDATED = true
                                    binding?.imageWatchList?.setImageResource(R.drawable.ic_added)
                                    Toast.makeText(
                                        applicationContext,
                                        "Added to WatchList",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    compositeDisposable.dispose()
                                }
                        }?.let { compositeDisposable.add(it) }
                    }

                }
                binding?.imageWatchList?.visibility = View.VISIBLE
                loadBasicTvDetails()
            }
        })
    }

    private fun loadImageSlider(sliderImages: ArrayList<String>) {
        binding?.sliderViewPager?.offscreenPageLimit = 1
        binding?.sliderViewPager?.adapter = ImageSliderAdapter(sliderImages)
        binding?.sliderViewPager?.visibility = View.VISIBLE
        binding?.viewFadingEdge?.visibility = View.VISIBLE
        setupSliderIndicators(sliderImages.size)
        binding?.sliderViewPager?.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentSliderIndicator(position)
            }
        })
    }

    private fun setupSliderIndicators(count: Int) {
        val indicators = Array(count) { ImageView(applicationContext) }
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i].setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.background_slider_indicator_inactive
                )
            )
            indicators[i].layoutParams = layoutParams
            binding?.layoutSliderIndicators?.addView(indicators[i])
        }
        binding?.layoutSliderIndicators?.visibility = View.VISIBLE
        setCurrentSliderIndicator(0)
    }

    private fun setCurrentSliderIndicator(position: Int) {
        val childCount = binding?.layoutSliderIndicators?.childCount
        for (i in 0 until childCount!!) {
            val imageView = binding?.layoutSliderIndicators?.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.background_slider_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.background_slider_indicator_inactive
                    )
                )
            }
        }
    }

    private fun loadBasicTvDetails() {
        binding?.run {
            tvShowName = tvShow?.name
            networkCountry =
                tvShow?.network + " (" + tvShow?.country + ")"
            status = tvShow?.status
            startedDate = tvShow?.startDate
            textName.visibility = View.VISIBLE
            textNetworkCountry.visibility = View.VISIBLE
            textStatus.visibility = View.VISIBLE
            textStarted.visibility = View.VISIBLE
        }
    }
}
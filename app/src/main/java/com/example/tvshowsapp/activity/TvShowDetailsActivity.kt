package com.example.tvshowsapp.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.tvshowsapp.R
import com.example.tvshowsapp.adapters.ImageSliderAdapter
import com.example.tvshowsapp.databinding.ActivityTvShowDetailsBinding
import com.example.tvshowsapp.viewModels.TvShowDetailsViewModel
import java.util.*

class TvShowDetailsActivity : AppCompatActivity() {
    private var binding: ActivityTvShowDetailsBinding? = null
    private var tvShowsDetailViewModel: TvShowDetailsViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tv_show_details)
        doInitialization()
    }

    private fun doInitialization() {
        tvShowsDetailViewModel = ViewModelProvider(this).get(TvShowDetailsViewModel::class.java)
        binding?.imageback?.setOnClickListener {
            onBackPressed()
        }
        getTvShowDetails()
    }

    private fun getTvShowDetails() {
        binding?.isLoading = true
        val tvShowId = (intent.getIntExtra("id", -1)).toString()
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
            tvShowName = intent.getStringExtra("name")
            networkCountry =
                intent.getStringExtra("network") + " (" + intent.getStringExtra("country") + ")"
            status = intent.getStringExtra("status")
            startedDate = intent.getStringExtra("startDate")
            textName.visibility = View.VISIBLE
            textNetworkCountry.visibility = View.VISIBLE
            textStatus.visibility = View.VISIBLE
            textStarted.visibility = View.VISIBLE
        }
    }
}
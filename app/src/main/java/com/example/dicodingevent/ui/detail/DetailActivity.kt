package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.ActivityDetailBinding
import com.example.dicodingevent.ui.ViewModelFactory
import com.example.dicodingevent.ui.adapter.EventAdapter
import com.example.dicodingevent.data.Result
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var eventLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.detail_title)
            setDisplayHomeAsUpEnabled(true)
        }

        val eventId = intent.getStringExtra(EventAdapter.DT_ID)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val detailViewModel: DetailViewModel by viewModels {
            factory
        }

        detailViewModel.getDetailEvent(eventId).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is Result.Success -> {
                    binding.progressBar.isVisible = false
                    Glide.with(this).load(result.data.mediaCover).into(binding.ivCover)
                    binding.apply {
                        tvTitle.text = result.data.name
                        tvSummary.text = result.data.summary
                        tvOwner.text = getString(R.string.penyelenggaraDet, result.data.ownerName)
                        tvPlace.text = getString(R.string.tempatDet, result.data.cityName)
                        tvTime.text =
                            getString(R.string.waktuDet, result.data.beginTime, result.data.endTime)
                        tvQuota.text =
                            getString(
                                R.string.sisa_kuotaDet,
                                (result.data.quota - result.data.registrants).toString()
                            )
                        tvDesc.text = HtmlCompat.fromHtml(
                            result.data.description,
                            HtmlCompat.FROM_HTML_MODE_LEGACY
                        )
                    }
                    eventLink = result.data.link
                }

                is Result.Error -> {
                    binding.progressBar.isVisible = false
                    result.message.getContentIfNotHandled()?.let { errorMessage ->
                        Snackbar.make(
                            window.decorView.rootView,
                            errorMessage,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.btnRegis.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(eventLink)
            }
            startActivity(intent)
        }
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
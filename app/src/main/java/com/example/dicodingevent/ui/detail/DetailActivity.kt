package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.databinding.ActivityDetailBinding
import com.example.dicodingevent.ui.adapter.EventAdapter
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var eventLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Event Details"

        val eventId = intent.getStringExtra(EventAdapter.DT_ID)
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        if (detailViewModel.event.value == null) {
            detailViewModel.getDetailEvent(eventId)
        }

        detailViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        detailViewModel.errorMsg.observe(this) { event ->
            event.getContentIfNotHandled()?.let { errorMsg ->
                Snackbar.make(
                    window.decorView.rootView,
                    errorMsg,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        detailViewModel.event.observe(this) { event ->
            Glide.with(this).load(event.mediaCover).into(binding.ivCover)
            binding.tvTitle.text = event.name
            binding.tvSummary.text = event.summary
            binding.tvOwner.text = getString(R.string.penyelenggaraDet, event.ownerName)
            binding.tvPlace.text = getString(R.string.tempatDet, event.cityName)
            binding.tvTime.text = getString(R.string.waktuDet, event.beginTime, event.endTime)
            binding.tvQuota.text =
                getString(R.string.sisa_kuotaDet, (event.quota - event.registrants).toString())
            binding.tvDesc.text = HtmlCompat.fromHtml(
                event.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            eventLink = event.link
        }

        binding.btnRegis.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(eventLink)
            }
            startActivity(intent)
        }
    }
}
package com.example.dicodingevent.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.ItemEventMiniBinding
import com.example.dicodingevent.ui.detail.DetailActivity

class EventMiniAdapter: ListAdapter<ListEventsItem, EventMiniAdapter.EventMiniViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventMiniViewHolder {
        val binding = ItemEventMiniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventMiniViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventMiniViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DT_ID, event.id.toString())
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class EventMiniViewHolder(private val binding: ItemEventMiniBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            Glide.with(binding.root.context).load(event.imageLogo).into(binding.imgItemPhotoMini)
            binding.apply {
                tvItemNameMini.text = event.name
                tvItemTimeMini.text = event.beginTime
                tvItemSummaryMini.text = event.summary
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }

        const val DT_ID = "dt_id"
    }
}
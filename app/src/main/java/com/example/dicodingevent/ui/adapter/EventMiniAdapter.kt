package com.example.dicodingevent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.response.ListEventsItem
import com.example.dicodingevent.databinding.ItemEventMiniBinding

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
    }

    class EventMiniViewHolder(private val binding: ItemEventMiniBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            Glide.with(binding.root.context).load(event.imageLogo).into(binding.imgItemPhotoMini)
            binding.tvItemNameMini.text = event.name
            binding.tvItemTimeMini.text = event.beginTime
            binding.tvItemSummaryMini.text = event.summary
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
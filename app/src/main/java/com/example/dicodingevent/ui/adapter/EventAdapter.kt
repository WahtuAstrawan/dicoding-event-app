package com.example.dicodingevent.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.databinding.ItemEventBinding
import com.example.dicodingevent.ui.detail.DetailActivity

class EventAdapter : ListAdapter<EventItem, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DT_ID, event.id.toString())
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class EventViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventItem) {
            Glide.with(binding.root.context).load(event.mediaCover).into(binding.ivItemPhoto)
            binding.apply {
                tvItemName.text = event.name
                tvItemTime.text = event.beginTime
                tvItemSummary.text = event.summary
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventItem>() {
            override fun areItemsTheSame(
                oldItem: EventItem,
                newItem: EventItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: EventItem,
                newItem: EventItem
            ): Boolean {
                return oldItem.id == newItem.id &&
                        oldItem.mediaCover == newItem.mediaCover &&
                        oldItem.name == newItem.name &&
                        oldItem.beginTime == newItem.beginTime &&
                        oldItem.summary == newItem.summary
            }
        }

        const val DT_ID = "dt_id"
    }
}
package com.example.gamingwakeup.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.databinding.ViewAlarmListItemBinding
import com.example.gamingwakeup.model.Alarm

class AlarmListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Alarm, AlarmListAdapter.ViewHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ViewAlarmListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(alarm)
        }
        holder.bind(alarm)
    }

    class ViewHolder(private val binding: ViewAlarmListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alarm: Alarm) {
            binding.alarm = alarm
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem.hour == newItem.hour
        }

    }

    class OnClickListener(private val clickListener: (alarm: Alarm) -> Unit) {
        fun onClick(alarm: Alarm) = clickListener(alarm)
    }
}

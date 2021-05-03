package com.example.gamingwakeup.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.ViewAlarmListItemBinding
import com.example.gamingwakeup.model.model.Alarm

class AlarmListAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Alarm, AlarmListAdapter.ViewHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewAlarmListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(alarm)
        }
        setupWeeklyRecurringTextColor(holder, alarm)
        holder.bind(alarm)
    }

    private fun setupWeeklyRecurringTextColor(holder: ViewHolder, alarm: Alarm) {
        val binding = holder.binding
        val resource = holder.itemView.resources
        val changeTextColor = { recurring: Boolean, text: TextView ->
            if (recurring) text.setTextColor(ResourcesCompat.getColor(resource, R.color.primary_light, null))
        }
        val weeklyRecurringSettingTexts = listOf(
            binding.monday,
            binding.tuesday,
            binding.wednesday,
            binding.thursday,
            binding.friday,
            binding.saturday,
            binding.sunday
        )
        weeklyRecurringSettingTexts.forEachIndexed { index, textView ->
            when(index) {
                0 -> changeTextColor(alarm.weeklyRecurring.monday, textView)
                1 -> changeTextColor(alarm.weeklyRecurring.tuesday, textView)
                2 -> changeTextColor(alarm.weeklyRecurring.wednesday, textView)
                3 -> changeTextColor(alarm.weeklyRecurring.thursday, textView)
                4 -> changeTextColor(alarm.weeklyRecurring.friday, textView)
                5 -> changeTextColor(alarm.weeklyRecurring.saturday, textView)
                else -> changeTextColor(alarm.weeklyRecurring.sunday, textView)
            }
        }
    }

    class ViewHolder(val binding: ViewAlarmListItemBinding) :
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

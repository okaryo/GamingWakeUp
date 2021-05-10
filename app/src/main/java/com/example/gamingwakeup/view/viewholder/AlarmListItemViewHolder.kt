package com.example.gamingwakeup.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.databinding.ViewAlarmListItemBinding
import com.example.gamingwakeup.model.alarm.Alarm

class AlarmListItemViewHolder(val binding: ViewAlarmListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(alarm: Alarm) {
        binding.alarm = alarm
        binding.executePendingBindings()
    }
}

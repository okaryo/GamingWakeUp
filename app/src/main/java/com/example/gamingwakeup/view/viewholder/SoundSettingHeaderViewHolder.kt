package com.example.gamingwakeup.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.databinding.ViewSoundSettingHeaderBinding

class SoundSettingHeaderViewHolder private constructor(private val binding: ViewSoundSettingHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(position: Int) {
        binding.headerTitle = if (position == 0) "Volume" else "Sound"
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): SoundSettingHeaderViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ViewSoundSettingHeaderBinding.inflate(inflater, parent, false)
            return SoundSettingHeaderViewHolder(binding)
        }
    }
}

package com.example.gamingwakeup.view.viewholder

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.ViewSoundSettingSoundTitleBinding
import com.example.gamingwakeup.view.adapter.SoundSettingAdapter

class SoundSettingContentTitleViewHolder private constructor(
    private val binding: ViewSoundSettingSoundTitleBinding,
    private val soundTitles: List<String>,
    private val clickListener: SoundSettingAdapter.OnClickSoundTitleListener
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        position: Int,
        holder: RecyclerView.ViewHolder,
        selectedSoundTitle: String,
        isSoundPlaying: Boolean
    ) {
        binding.title = soundTitles[position]
        setupClickListener(binding.title!!)
        setupEqualizerIcon(isSoundPlaying)
        setupCheckIcon(selectedSoundTitle, holder.itemView.resources)
        binding.executePendingBindings()
    }

    private fun setupClickListener(selectedSoundTitle: String) {
        binding.soundTitleItem.setOnClickListener {
            clickListener.onClick(selectedSoundTitle)
        }
    }

    private fun setupEqualizerIcon(isSoundPlaying: Boolean) {
        if (isSoundPlaying) {
            binding.equalizerIcon.visibility = View.VISIBLE
            binding.equalizerIcon.animateBars()
        } else {
            binding.equalizerIcon.visibility = View.GONE
            binding.equalizerIcon.stopBars()
        }
    }

    private fun setupCheckIcon(selectedSoundTitle: String, resources: Resources) {
        if (selectedSoundTitle == binding.title) {
            binding.soundTitleItem.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.primary_light,
                    null
                )
            )
            binding.selectedCheckIcon.visibility = View.VISIBLE
        } else {
            binding.soundTitleItem.setBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.background,
                    null
                )
            )
            binding.equalizerIcon.visibility = View.GONE
            binding.selectedCheckIcon.visibility = View.GONE
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            soundTitles: List<String>,
            clickListener: SoundSettingAdapter.OnClickSoundTitleListener
        ): SoundSettingContentTitleViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ViewSoundSettingSoundTitleBinding.inflate(inflater, parent, false)
            return SoundSettingContentTitleViewHolder(binding, soundTitles, clickListener)
        }
    }
}

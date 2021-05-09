package com.example.gamingwakeup.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.databinding.ViewSoundSettingSoundVolumeBinding
import com.example.gamingwakeup.view.adapter.SoundSettingAdapter

class SoundSettingContentVolumeViewHolder private constructor(
    private val binding: ViewSoundSettingSoundVolumeBinding,
    private val onChangeListener: SoundSettingAdapter.OnChangeSoundVolumeListener
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(soundVolume: Int) {
        binding.volume = soundVolume
        binding.soundVolumeSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                onChangeListener.onChange(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        binding.executePendingBindings()
    }

    companion object {
        fun from(
            parent: ViewGroup,
            changeListener: SoundSettingAdapter.OnChangeSoundVolumeListener
        ): SoundSettingContentVolumeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ViewSoundSettingSoundVolumeBinding.inflate(inflater, parent, false)
            return SoundSettingContentVolumeViewHolder(binding, changeListener)
        }
    }
}

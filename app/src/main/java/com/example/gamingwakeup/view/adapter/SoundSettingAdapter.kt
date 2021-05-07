package com.example.gamingwakeup.view.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.ViewSoundSettingHeaderBinding
import com.example.gamingwakeup.databinding.ViewSoundSettingSoundTitleBinding
import com.example.gamingwakeup.databinding.ViewSoundSettingSoundVolumeBinding

class SoundSettingAdapter(
    private val onClickSoundTitleListener: OnClickSoundTitleListener,
    private val onChangeSoundVolumeListener: OnChangeSoundVolumeListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var soundTitles: List<String>
    lateinit var selectedSoundTitle: String
    var soundVolume = 50
    var isSoundPlaying = false

    enum class ViewType {
        HEADER,
        SOUND_VOLUME,
        SOUND_TITLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.values().first { it.ordinal == viewType }) {
            ViewType.HEADER -> HeaderViewHolder.from(parent)
            ViewType.SOUND_VOLUME -> SoundVolumeViewHolder.from(parent, onChangeSoundVolumeListener)
            ViewType.SOUND_TITLE -> SoundTitleViewHolder.from(
                parent,
                soundTitles,
                onClickSoundTitleListener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(position)
            is SoundVolumeViewHolder -> holder.bind(soundVolume)
            is SoundTitleViewHolder -> {
                val offset = 3
                holder.bind(
                    position - offset,
                    holder,
                    selectedSoundTitle,
                    isSoundPlaying
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0, 2 -> ViewType.HEADER.ordinal
            1 -> ViewType.SOUND_VOLUME.ordinal
            else -> ViewType.SOUND_TITLE.ordinal
        }
    }

    override fun getItemCount(): Int {
        val headerCount = 2
        val seekBarCount = 1
        val soundTypesCount = soundTitles.size
        return headerCount + seekBarCount + soundTypesCount
    }

    class HeaderViewHolder private constructor(private val binding: ViewSoundSettingHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.headerTitle = if (position == 0) "Volume" else "Sound"
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ViewSoundSettingHeaderBinding.inflate(inflater, parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    class SoundVolumeViewHolder private constructor(
        private val binding: ViewSoundSettingSoundVolumeBinding,
        private val onChangeListener: OnChangeSoundVolumeListener
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
                changeListener: OnChangeSoundVolumeListener
            ): SoundVolumeViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ViewSoundSettingSoundVolumeBinding.inflate(inflater, parent, false)
                return SoundVolumeViewHolder(binding, changeListener)
            }
        }
    }

    class SoundTitleViewHolder private constructor(
        private val binding: ViewSoundSettingSoundTitleBinding,
        private val soundTitles: List<String>,
        private val clickListener: OnClickSoundTitleListener
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
                clickListener: OnClickSoundTitleListener
            ): SoundTitleViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ViewSoundSettingSoundTitleBinding.inflate(inflater, parent, false)
                return SoundTitleViewHolder(binding, soundTitles, clickListener)
            }
        }
    }

    class OnClickSoundTitleListener(val clickListener: (soundTitle: String) -> Unit) {
        fun onClick(soundTitle: String) = clickListener(soundTitle)
    }

    class OnChangeSoundVolumeListener(private val changeListener: (soundVolume: Int) -> Unit) {
        fun onChange(soundVolume: Int) = changeListener(soundVolume)
    }
}

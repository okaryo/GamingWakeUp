package com.example.gamingwakeup.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.ViewSoundSettingHeaderBinding
import com.example.gamingwakeup.databinding.ViewSoundSettingSoundTitleBinding
import com.example.gamingwakeup.databinding.ViewSoundSettingSoundVolumeBinding

class SoundSettingAdapter(
    private val soundTitles: List<String>,
    private val selectedSoundTitle: LiveData<String>,
    private val isSoundPlaying: () -> Boolean,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewType {
        HEADER,
        SOUND_VOLUME,
        SOUND_TITLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.values().first { it.ordinal == viewType }) {
            ViewType.HEADER -> HeaderViewHolder.from(parent)
            ViewType.SOUND_VOLUME -> SoundVolumeViewHolder.from(parent)
            ViewType.SOUND_TITLE -> SoundTitleViewHolder.from(
                parent,
                isSoundPlaying,
                soundTitles,
                onClickListener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(position)
            is SoundVolumeViewHolder -> holder.bind()
            is SoundTitleViewHolder -> {
                val offset = 3
                holder.bind(
                    position - offset,
                    holder,
                    selectedSoundTitle
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

    class SoundVolumeViewHolder private constructor(private val binding: ViewSoundSettingSoundVolumeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.volume = 50
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): SoundVolumeViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ViewSoundSettingSoundVolumeBinding.inflate(inflater, parent, false)
                return SoundVolumeViewHolder(binding)
            }
        }
    }

    class SoundTitleViewHolder private constructor(
        private val binding: ViewSoundSettingSoundTitleBinding,
        private val isSoundPlaying: () -> Boolean,
        private val soundTitles: List<String>,
        private val clickListener: OnClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            holder: RecyclerView.ViewHolder,
            selectedSoundTitle: LiveData<String>
        ) {
            binding.title = soundTitles[position]
            setupClickListener(holder, isSoundPlaying, selectedSoundTitle)
            binding.executePendingBindings()
        }

        private fun setupClickListener(
            holder: RecyclerView.ViewHolder,
            isSoundPlaying: () -> Boolean,
            selectedSoundTitleLiveData: LiveData<String>
        ) {
            val resources = holder.itemView.resources
            val selectedSoundTitle = binding.title!!
            binding.soundTitleItem.setOnClickListener {
                clickListener.onClick(selectedSoundTitle)
                if (isSoundPlaying()) {
                    binding.equalizerIcon.visibility = View.VISIBLE
                    binding.equalizerIcon.animateBars()
                } else {
                    binding.equalizerIcon.visibility = View.GONE
                    binding.equalizerIcon.stopBars()
                }
            }
            selectedSoundTitleLiveData.observe(this.itemView.context as LifecycleOwner, Observer {
                if (selectedSoundTitle == it) {
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
            })
        }

        companion object {
            fun from(
                parent: ViewGroup,
                isSoundPlaying: () -> Boolean,
                soundTitles: List<String>,
                clickListener: OnClickListener
            ): SoundTitleViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ViewSoundSettingSoundTitleBinding.inflate(inflater, parent, false)
                return SoundTitleViewHolder(binding, isSoundPlaying, soundTitles, clickListener)
            }
        }
    }

    class OnClickListener(private val clickListener: (soundTitle: String) -> Unit) {
        fun onClick(soundTitle: String) = clickListener(soundTitle)
    }
}

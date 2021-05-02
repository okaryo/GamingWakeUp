package com.example.gamingwakeup.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.databinding.ViewSoundSettingHeaderBinding
import com.example.gamingwakeup.databinding.ViewSoundSettingSoundTitleActiveBinding
import com.example.gamingwakeup.databinding.ViewSoundSettingSoundTitleSilentBinding
import com.example.gamingwakeup.databinding.ViewSoundSettingSoundVolumeBinding

class SoundSettingAdapter(
    private val soundTitles: List<String>,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewType {
        HEADER,
        SOUND_VOLUME,
        SOUND_SILENT,
        SOUND_TITLE
    }
    // header, volumeSeekBar, soundTypeItem みたいにしたい

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        println(viewType)
        return when (ViewType.values().first { it.ordinal == viewType }) {
            ViewType.HEADER -> HeaderViewHolder.from(parent)
            ViewType.SOUND_VOLUME -> SoundVolumeViewHolder.from(parent)
            ViewType.SOUND_SILENT -> SoundTitleSilentViewHolder.from(parent)
            ViewType.SOUND_TITLE -> SoundTitleActiveViewHolder.from(parent, soundTitles)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> holder.bind(position)
            is SoundVolumeViewHolder -> holder.bind()
            is SoundTitleSilentViewHolder -> holder.bind()
            is SoundTitleActiveViewHolder -> holder.bind(position - 4) // TODO: マジックナンバー！
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0, 2 -> ViewType.HEADER.ordinal
            1 -> ViewType.SOUND_VOLUME.ordinal
            3 -> ViewType.SOUND_SILENT.ordinal
            else -> ViewType.SOUND_TITLE.ordinal
        }
    }

    override fun getItemCount(): Int {
        val headerCount = 2
        val seekBarCount = 1
        val soundSilentCount = 1
        val soundTypesCount = soundTitles.size
        return headerCount + seekBarCount + soundSilentCount + soundTypesCount
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

    class SoundTitleActiveViewHolder private constructor(private val binding: ViewSoundSettingSoundTitleActiveBinding, private val soundTitles: List<String>) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.title = soundTitles[position]
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, soundTitles: List<String>): SoundTitleActiveViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ViewSoundSettingSoundTitleActiveBinding.inflate(inflater, parent, false)
                return SoundTitleActiveViewHolder(binding, soundTitles)
            }
        }
    }

    class SoundTitleSilentViewHolder private constructor(private val binding: ViewSoundSettingSoundTitleSilentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
        }

        companion object {
            fun from(parent: ViewGroup): SoundTitleSilentViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ViewSoundSettingSoundTitleSilentBinding.inflate(inflater, parent, false)
                return SoundTitleSilentViewHolder(binding)
            }
        }
    }

    class OnClickListener(private val clickListener: (soundName: String) -> Unit) {
        fun onClick(soundName: String) = clickListener(soundName)
    }
}

package com.example.gamingwakeup.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.view.viewholder.SoundSettingContentTitleViewHolder
import com.example.gamingwakeup.view.viewholder.SoundSettingContentVolumeViewHolder
import com.example.gamingwakeup.view.viewholder.SoundSettingHeaderViewHolder

class SoundSettingAdapter(
    private val onClickSoundTitleListener: OnClickSoundTitleListener,
    private val onChangeSoundVolumeListener: OnChangeSoundVolumeListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var soundTitles: List<String>
    lateinit var selectedSoundTitle: String
    var soundVolume = 50
    var isSoundPlaying = false

    enum class ViewType {
        HEADER,
        CONTENT_VOLUME,
        CONTENT_TITLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.values().first { it.ordinal == viewType }) {
            ViewType.HEADER -> SoundSettingHeaderViewHolder.from(parent)
            ViewType.CONTENT_VOLUME -> SoundSettingContentVolumeViewHolder.from(
                parent,
                onChangeSoundVolumeListener
            )
            ViewType.CONTENT_TITLE -> SoundSettingContentTitleViewHolder.from(
                parent,
                soundTitles,
                onClickSoundTitleListener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SoundSettingHeaderViewHolder -> holder.bind(position)
            is SoundSettingContentVolumeViewHolder -> holder.bind(soundVolume)
            is SoundSettingContentTitleViewHolder -> {
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
            1 -> ViewType.CONTENT_VOLUME.ordinal
            else -> ViewType.CONTENT_TITLE.ordinal
        }
    }

    override fun getItemCount(): Int {
        val headerCount = 2
        val seekBarCount = 1
        val soundTypesCount = soundTitles.size
        return headerCount + seekBarCount + soundTypesCount
    }

    class OnClickSoundTitleListener(val clickListener: (soundTitle: String) -> Unit) {
        fun onClick(soundTitle: String) = clickListener(soundTitle)
    }

    class OnChangeSoundVolumeListener(private val changeListener: (soundVolume: Int) -> Unit) {
        fun onChange(soundVolume: Int) = changeListener(soundVolume)
    }
}

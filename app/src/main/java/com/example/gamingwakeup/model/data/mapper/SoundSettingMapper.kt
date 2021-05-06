package com.example.gamingwakeup.model.data.mapper

import com.example.gamingwakeup.model.model.SoundSetting

class SoundSettingMapper {
    companion object {
        fun transform(id: Long, title: String, volume: Int): SoundSetting {
            return SoundSetting(
                id = id,
                title = title,
                volume = volume
            )
        }
    }
}

package com.example.gamingwakeup.model.data.mapper

import com.example.gamingwakeup.model.model.SoundSetting

class SoundSettingMapper {
    companion object {
        fun transform(title: String, volume: Int): SoundSetting {
            return SoundSetting(
                title = title,
                volume = volume
            )
        }
    }
}

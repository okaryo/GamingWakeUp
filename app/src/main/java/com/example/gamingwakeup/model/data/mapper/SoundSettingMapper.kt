package com.example.gamingwakeup.model.data.mapper

import com.example.gamingwakeup.model.model.SoundSetting

class SoundSettingMapper {
    companion object {
        fun transform(name: String, volume: Int): SoundSetting {
            return SoundSetting(
                name = name,
                volume = volume
            )
        }
    }
}

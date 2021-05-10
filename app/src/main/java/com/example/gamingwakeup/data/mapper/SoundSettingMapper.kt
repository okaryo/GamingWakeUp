package com.example.gamingwakeup.data.mapper

import com.example.gamingwakeup.model.alarm.SoundSetting

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

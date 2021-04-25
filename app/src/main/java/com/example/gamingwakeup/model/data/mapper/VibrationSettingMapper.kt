package com.example.gamingwakeup.model.data.mapper

import com.example.gamingwakeup.model.model.VibrationSetting

class VibrationSettingMapper {
    companion object {
        fun transform(active: Boolean): VibrationSetting {
            return VibrationSetting(
                active = active
            )
        }
    }
}

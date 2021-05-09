package com.example.gamingwakeup.data.mapper

import com.example.gamingwakeup.model.WeeklyRecurringSetting

class WeeklyRecurringSettingMapper {
    companion object {
        fun transform(
            monday: Boolean,
            tuesday: Boolean,
            wednesday: Boolean,
            thursday: Boolean,
            friday: Boolean,
            saturday: Boolean,
            sunday: Boolean
        ): WeeklyRecurringSetting {
            return WeeklyRecurringSetting(
                monday = monday,
                tuesday = tuesday,
                wednesday = wednesday,
                thursday = thursday,
                friday = friday,
                saturday = saturday,
                sunday = sunday
            )
        }
    }
}

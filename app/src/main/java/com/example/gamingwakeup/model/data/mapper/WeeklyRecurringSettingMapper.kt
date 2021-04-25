package com.example.gamingwakeup.model.data.mapper

import com.example.gamingwakeup.model.model.WeeklyRecurringSetting

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

package com.example.gamingwakeup.model.data.mapper

import com.example.gamingwakeup.model.data.entity.AlarmEntity
import com.example.gamingwakeup.model.model.Alarm

class AlarmMapper {
    companion object {
        fun transform(entity: AlarmEntity): Alarm {
            return Alarm(
                id = entity.id,
                hour = entity.hour,
                minute = entity.minute,
                sound = SoundSettingMapper.transform(entity.soundName, entity.soundVolume),
                vibration = VibrationSettingMapper.transform(entity.vibrationActive),
                weeklyRecurring = WeeklyRecurringSettingMapper.transform(
                    entity.weeklyRecurringMonday,
                    entity.weeklyRecurringTuesday,
                    entity.weeklyRecurringWednesday,
                    entity.weeklyRecurringThursday,
                    entity.weeklyRecurringFriday,
                    entity.weeklyRecurringSaturday,
                    entity.weeklyRecurringSunday
                ),
                active = entity.active
            )
        }

        fun transform(model: Alarm): AlarmEntity {
            return AlarmEntity(
                id = model.id,
                hour = model.hour,
                minute = model.minute,
                soundName = model.sound.name,
                soundVolume = model.sound.volume,
                vibrationActive = model.vibration.active,
                weeklyRecurringMonday = model.weeklyRecurring.monday,
                weeklyRecurringTuesday = model.weeklyRecurring.tuesday,
                weeklyRecurringWednesday = model.weeklyRecurring.wednesday,
                weeklyRecurringThursday = model.weeklyRecurring.thursday,
                weeklyRecurringFriday = model.weeklyRecurring.friday,
                weeklyRecurringSaturday = model.weeklyRecurring.saturday,
                weeklyRecurringSunday = model.weeklyRecurring.sunday,
                active = model.active
            )
        }
    }
}

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
                vibration = entity.vibration,
                recurring = entity.recurring,
                sound = SoundSettingMapper.transform(entity.soundTitle, entity.soundVolume),
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
                soundTitle = model.sound.title,
                soundVolume = model.sound.volume,
                vibration = model.vibration,
                recurring = model.recurring,
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

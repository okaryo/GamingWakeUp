package com.example.gamingwakeup.model.data.mapper

import com.example.gamingwakeup.model.data.entity.AlarmEntity
import com.example.gamingwakeup.model.model.Alarm

class AlarmMapper {
    companion object {
        fun transformToModel(alarmEntity: AlarmEntity): Alarm {
            return Alarm(
                id = alarmEntity.id,
                hour = alarmEntity.hour,
                minute = alarmEntity.minute,
                soundVolume = alarmEntity.soundVolume,
                hasVibration = alarmEntity.hasVibration,
                isTurnedOn = alarmEntity.isTurnedOn
            )
        }

        fun transformToEntity(alarm: Alarm): AlarmEntity {
            return AlarmEntity(
                id = alarm.id,
                hour = alarm.hour,
                minute = alarm.minute,
                soundVolume = alarm.soundVolume,
                hasVibration = alarm.hasVibration,
                isTurnedOn = alarm.isTurnedOn
            )
        }
    }
}

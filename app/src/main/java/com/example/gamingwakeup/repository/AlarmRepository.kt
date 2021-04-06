package com.example.gamingwakeup.repository

import com.example.gamingwakeup.model.Alarm
import com.example.gamingwakeup.room.database.AlarmDatabase

class AlarmRepository(private val database: AlarmDatabase) {
    suspend fun getAlarm(alarmId: Int) = database.alarmDao().getAlarm(alarmId)

    suspend fun createAlarm(alarm: Alarm) = database.alarmDao().insert(alarm)

    suspend fun updateAlarm(alarm: Alarm) = database.alarmDao().update(alarm)

    suspend fun deleteAlarm(alarm: Alarm) = database.alarmDao().delete(alarm)
}
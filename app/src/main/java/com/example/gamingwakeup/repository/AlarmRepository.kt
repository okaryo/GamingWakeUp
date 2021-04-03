package com.example.gamingwakeup.repository

import com.example.gamingwakeup.model.Alarm
import com.example.gamingwakeup.room.database.AlarmDatabase

class AlarmRepository(private val database: AlarmDatabase) {
    suspend fun createAlarm(alarm: Alarm) = database.alarmDao().insert(alarm)

    fun updateAlarm(alarm: Alarm) {
        // TODO: 別イシューで
    }

    fun deleteAlarm(alarm: Alarm) {
        // TODO: 別イシューで
    }
}
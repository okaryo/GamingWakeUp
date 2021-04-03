package com.example.gamingwakeup.repository

import com.example.gamingwakeup.model.AlarmList
import com.example.gamingwakeup.room.database.AlarmDatabase

class AlarmListRepository(private val database: AlarmDatabase) {
    suspend fun getAlarmList(): AlarmList {
        return AlarmList(database.alarmDao().getAll())
    }
}
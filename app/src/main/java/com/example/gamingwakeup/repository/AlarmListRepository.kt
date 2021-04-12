package com.example.gamingwakeup.repository

import com.example.gamingwakeup.model.AlarmList
import com.example.gamingwakeup.database.AlarmDatabase

class AlarmListRepository(private val database: AlarmDatabase) {
    suspend fun getAlarmList(): AlarmList = AlarmList(database.alarmDao().getAll())
}

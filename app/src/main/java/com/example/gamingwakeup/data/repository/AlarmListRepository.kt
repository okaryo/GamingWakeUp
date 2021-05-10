package com.example.gamingwakeup.data.repository

import com.example.gamingwakeup.data.database.AlarmDatabase
import com.example.gamingwakeup.data.mapper.AlarmMapper
import com.example.gamingwakeup.model.alarm.AlarmList

class AlarmListRepository(private val database: AlarmDatabase) {
    suspend fun getAlarmList(): AlarmList {
        val values = database.alarmDao().getAll().map { AlarmMapper.transform(it) }
        return AlarmList(values)
    }
}

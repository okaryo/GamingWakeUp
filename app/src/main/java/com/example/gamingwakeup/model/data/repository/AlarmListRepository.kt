package com.example.gamingwakeup.model.data.repository

import com.example.gamingwakeup.model.data.database.AlarmDatabase
import com.example.gamingwakeup.model.data.mapper.AlarmMapper
import com.example.gamingwakeup.model.model.AlarmList

class AlarmListRepository(private val database: AlarmDatabase) {
    suspend fun getAlarmList(): AlarmList {
        val values = database.alarmDao().getAll().map { AlarmMapper.transform(it) }
        return AlarmList(values)
    }
}

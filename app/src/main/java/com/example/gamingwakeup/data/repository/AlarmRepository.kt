package com.example.gamingwakeup.data.repository

import com.example.gamingwakeup.data.database.AlarmDatabase
import com.example.gamingwakeup.data.entity.AlarmEntity
import com.example.gamingwakeup.data.mapper.AlarmMapper
import com.example.gamingwakeup.model.alarm.Alarm

class AlarmRepository(private val database: AlarmDatabase) {
    suspend fun getAlarm(alarmId: Int): Alarm {
        val alarmEntity = database.alarmDao().getAlarm(alarmId)
        return AlarmMapper.transform(alarmEntity)
    }

    suspend fun createAlarm(alarm: Alarm) {
        val alarmEntity = AlarmMapper.transform(alarm)
        database.alarmDao().insert(AlarmEntity.createFotInsert(alarmEntity))
    }

    suspend fun updateAlarm(alarm: Alarm) {
        val alarmEntity = AlarmMapper.transform(alarm)
        database.alarmDao().update(alarmEntity)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        val alarmEntity = AlarmMapper.transform(alarm)
        database.alarmDao().delete(alarmEntity)
    }
}

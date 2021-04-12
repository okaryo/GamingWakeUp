package com.example.gamingwakeup.model.data.repository

import com.example.gamingwakeup.model.model.Alarm
import com.example.gamingwakeup.model.data.database.AlarmDatabase
import com.example.gamingwakeup.model.data.entity.AlarmEntity
import com.example.gamingwakeup.model.data.mapper.AlarmMapper

class AlarmRepository(private val database: AlarmDatabase) {
    suspend fun getAlarm(alarmId: Int): Alarm {
        val alarmEntity = database.alarmDao().getAlarm(alarmId)
        return AlarmMapper.transformToModel(alarmEntity)
    }

    suspend fun createAlarm(alarm: Alarm) {
        val alarmEntity = AlarmMapper.transformToEntity(alarm)
        database.alarmDao().insert(AlarmEntity.createFotInsert(alarmEntity))
    }

    suspend fun updateAlarm(alarm: Alarm) {
        val alarmEntity = AlarmMapper.transformToEntity(alarm)
        database.alarmDao().update(alarmEntity)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        val alarmEntity = AlarmMapper.transformToEntity(alarm)
        database.alarmDao().delete(alarmEntity)
    }
}

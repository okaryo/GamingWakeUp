package com.example.gamingwakeup.room.dao

import androidx.room.*
import com.example.gamingwakeup.model.Alarm

@Dao
interface AlarmDao {
    @Insert
    suspend fun insert(alarm: Alarm)

    @Update
    suspend fun update(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("select * from alarms")
    suspend fun getAll(): List<Alarm>

    @Query("select * from alarms where id = :id")
    suspend fun getAlarm(id: Int): Alarm
}

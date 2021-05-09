package com.example.gamingwakeup.data.database

import androidx.room.*
import com.example.gamingwakeup.data.entity.AlarmEntity

@Dao
interface AlarmDao {
    @Insert
    suspend fun insert(alarm: AlarmEntity)

    @Update
    suspend fun update(alarm: AlarmEntity)

    @Delete
    suspend fun delete(alarm: AlarmEntity)

    @Query("select * from alarms")
    suspend fun getAll(): List<AlarmEntity>

    @Query("select * from alarms where id = :id")
    suspend fun getAlarm(id: Int): AlarmEntity
}

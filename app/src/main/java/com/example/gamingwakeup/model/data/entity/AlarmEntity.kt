package com.example.gamingwakeup.model.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val hour: Int,
    val minute: Int,
    @ColumnInfo(name = "sound_volume") val soundVolume: Int,
    @ColumnInfo(name = "has_vibration") val hasVibration: Boolean = true,
    @ColumnInfo(name = "is_turned_on") val isTurnedOn: Boolean = false
) {
    companion object {
        fun createFotInsert(alarmEntity: AlarmEntity) = alarmEntity.copy(id = 0)
    }
}


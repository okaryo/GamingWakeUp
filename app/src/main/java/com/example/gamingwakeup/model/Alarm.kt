package com.example.gamingwakeup.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val hour: Int,
    val minute: Int,
    @ColumnInfo(name = "sound_volume") val soundVolume: Int,
    @ColumnInfo(name = "has_vibration") val hasVibration: Boolean = true,
    @ColumnInfo(name = "is_turned_on") val isTurnedOn: Boolean = false
) {
    companion object {
        fun createForInsert(
            hour: Int,
            minute: Int,
            soundVolume: Int,
            hasVibration: Boolean,
            isTurnedOn: Boolean
        ): Alarm {
            return Alarm(
                id = 0,
                hour = hour,
                minute = minute,
                soundVolume = soundVolume,
                hasVibration = hasVibration,
                isTurnedOn = isTurnedOn
            )
        }
    }
}

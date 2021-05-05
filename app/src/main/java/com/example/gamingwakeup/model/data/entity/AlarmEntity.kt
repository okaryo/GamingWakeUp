package com.example.gamingwakeup.model.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val hour: Int,
    val minute: Int,
    val vibration: Boolean,
    val recurring: Boolean,
    @ColumnInfo(name = "sound_title") val soundTitle: String,
    @ColumnInfo(name = "sound_volume") val soundVolume: Int,
    @ColumnInfo(name = "weekly_recurring_monday") val weeklyRecurringMonday: Boolean,
    @ColumnInfo(name = "weekly_recurring_tuesday") val weeklyRecurringTuesday: Boolean,
    @ColumnInfo(name = "weekly_recurring_wednesday") val weeklyRecurringWednesday: Boolean,
    @ColumnInfo(name = "weekly_recurring_thursday") val weeklyRecurringThursday: Boolean,
    @ColumnInfo(name = "weekly_recurring_friday") val weeklyRecurringFriday: Boolean,
    @ColumnInfo(name = "weekly_recurring_saturday") val weeklyRecurringSaturday: Boolean,
    @ColumnInfo(name = "weekly_recurring_sunday") val weeklyRecurringSunday: Boolean,
    val active: Boolean
) {
    companion object {
        fun createFotInsert(alarmEntity: AlarmEntity) = alarmEntity.copy(id = 0)
    }
}


package com.example.gamingwakeup.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.example.gamingwakeup.data.repository.AlarmRepository
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Alarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val vibration: Boolean,
    val recurring: Boolean,
    val sound: SoundSetting,
    val weeklyRecurring: WeeklyRecurringSetting,
    val active: Boolean
): Parcelable {
    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context, this.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.let {
            it.set(Calendar.HOUR_OF_DAY, this.hour)
            it.set(Calendar.MINUTE, this.minute)
            it.set(Calendar.SECOND, 0)
            it.set(Calendar.MILLISECOND, 0)
        }
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmPendingIntent
        )
    }

    fun clockTimeStringFormat(): String {
        val hour = this.hour.toString().padStart(2, '0')
        val minute = this.minute.toString().padStart(2, '0')
        return "$hour:$minute"
    }

    suspend fun create(repository: AlarmRepository) = repository.createAlarm(this)

    suspend fun update(repository: AlarmRepository) = repository.updateAlarm(this)

    suspend fun delete(repository: AlarmRepository) = repository.deleteAlarm(this)

    companion object {
        suspend fun find(id: Int, repository: AlarmRepository): Alarm {
            return repository.getAlarm(id)
        }
    }
}

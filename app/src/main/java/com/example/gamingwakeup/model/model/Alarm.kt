package com.example.gamingwakeup.model.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.gamingwakeup.model.data.repository.AlarmRepository
import com.example.gamingwakeup.view.activity.GamingAlarmActivity
import java.util.*

data class Alarm(
    val id: Int,
    val hour: Int,
    val minute: Int,
    val soundVolume: Int,
    val hasVibration: Boolean = true,
    val isTurnedOn: Boolean = false
) {
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

    suspend fun create(repository: AlarmRepository) {
        repository.createAlarm(this)
    }

    suspend fun update(repository: AlarmRepository) {
        repository.updateAlarm(this)
    }

    suspend fun delete(repository: AlarmRepository) {
        repository.deleteAlarm(this)
    }

    companion object {
        suspend fun find(id: Int, repository: AlarmRepository): Alarm {
            return repository.getAlarm(id)
        }
    }

    // Receiver -----------------------------------------------------------

    class AlarmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("receiver", "onReceive!")
            if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                // 端末起動時にアラームをセットし直す
                rescheduleAlarmService(context)
            } else {
                activateAlarmService(context, intent)
            }
        }

        private fun activateAlarmService(context: Context, intent: Intent) {
            val alarmServiceIntent = Intent(context, AlarmService::class.java)
            alarmServiceIntent.putExtra("alarm", intent.getStringExtra("alarm"))
            context.startService(alarmServiceIntent)
        }

        private fun rescheduleAlarmService(context: Context) {}
    }

    // Service -----------------------------------------------------------

    class AlarmService : Service() {
        override fun onCreate() {
            super.onCreate()
            Log.d("Service", "onCreate!")
        }

        override fun onStart(intent: Intent, startId: Int) {
            Log.d("Service", "onStart!")
            val alarmIntent =
                Intent(this, GamingAlarmActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
            startActivity(alarmIntent)
            val pendingIntent = PendingIntent.getActivity(this, 0, alarmIntent, 0)

            val alarmTitle = String.format("%s Alarm", intent.getStringExtra("alarm"))
        }

        override fun onDestroy() {
            super.onDestroy()
        }

        override fun onBind(intent: Intent?): IBinder? {
            return null
        }
    }
}

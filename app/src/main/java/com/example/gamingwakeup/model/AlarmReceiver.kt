package com.example.gamingwakeup.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
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

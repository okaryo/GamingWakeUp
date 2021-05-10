package com.example.gamingwakeup.model

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.gamingwakeup.view.activity.GamingAlarmActivity

class AlarmService : Service() {
    override fun onStart(intent: Intent, startId: Int) {
        val alarmIntent =
            Intent(this, GamingAlarmActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
        startActivity(alarmIntent)
        val pendingIntent = PendingIntent.getActivity(this, 0, alarmIntent, 0)
        val alarmTitle = String.format("%s Alarm", intent.getStringExtra("alarm"))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

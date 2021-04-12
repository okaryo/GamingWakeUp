package com.example.gamingwakeup.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.example.gamingwakeup.R
import com.example.gamingwakeup.model.Alarm
import com.example.gamingwakeup.repository.AlarmRepository
import com.example.gamingwakeup.room.database.AlarmDatabase
import com.example.gamingwakeup.view.AlarmActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class AddEditAlarmViewModel(
    private val repository: AlarmRepository,
    private val alarmManager: MainAlarmManager,
    calender: Calendar
) : BaseObservable() {
    @Bindable
    var soundVolume: Int = 50

    @Bindable
    var hour = calender.get(Calendar.HOUR_OF_DAY)

    @Bindable
    var minute = calender.get(Calendar.MINUTE)
    private var _alarmId = 0
    private val _hasVibration = MutableLiveData(true)
    private var isNewAlarm = false

    val hasVibration: LiveData<Boolean>
        get() = _hasVibration

    fun initialize(alarmId: Int) {
        _alarmId = alarmId
        if (_alarmId == 0) {
            isNewAlarm = true
        } else {
            val alarm = runBlocking {
                repository.getAlarm(_alarmId)
            }
            hour = alarm.hour
            minute = alarm.minute
            soundVolume = alarm.soundVolume
            _hasVibration.value = alarm.hasVibration
        }
    }

    fun onVibrationValueChanged(isChecked: Boolean) {
        _hasVibration.value = isChecked
    }

    fun saveAlarm() {
        val currentHasVibration = _hasVibration.value ?: return
        var alarm: Alarm
        if (isNewAlarm) {
            alarm = Alarm.createForInsert(
                hour = hour,
                minute = minute,
                soundVolume = soundVolume,
                hasVibration = currentHasVibration,
                isTurnedOn = true
            )
            createAlarm(alarm)
        } else {
            alarm = Alarm(
                id = _alarmId,
                hour = hour,
                minute = minute,
                soundVolume = soundVolume,
                hasVibration = currentHasVibration,
                isTurnedOn = true
            )
            updateAlarm(alarm)
        }
        scheduleAlarm(alarm)
    }

    suspend fun deleteAlarm() {
        if (!isNewAlarm) {
            val alarm = repository.getAlarm(_alarmId)
            repository.deleteAlarm(alarm)
        }
    }

    private fun createAlarm(alarm: Alarm) = GlobalScope.launch { repository.createAlarm(alarm) }

    private fun updateAlarm(alarm: Alarm) = GlobalScope.launch { repository.updateAlarm(alarm) }

    private fun scheduleAlarm(alarm: Alarm) {
        alarmManager.registerAlarm(alarm)
    }

    companion object {
        fun create(application: Application): AddEditAlarmViewModel {
            val context = application.applicationContext
            val alarmManager = MainAlarmManager(context)
            val calender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
            val database = AlarmDatabase.getInstance(context)
            val repository = AlarmRepository(database)
            return AddEditAlarmViewModel(repository, alarmManager, calender)
        }
    }
}

// アラームモデルに処理をおこう
class MainAlarmManager(private val context: Context) {
    fun registerAlarm(alarm: Alarm) {
        Log.d("AlarmManager", "register!")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context, alarm.id, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmPendingIntent
        )


    }
}

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

class AlarmService : Service() {
    override fun onCreate() {
        super.onCreate()
        Log.d("Service", "onCreate!")
    }

    override fun onStart(intent: Intent, startId: Int) {
        Log.d("Service", "onStart!")
        val alarmIntent =
            Intent(this, AlarmActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
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

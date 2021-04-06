package com.example.gamingwakeup.viewmodel

import android.app.Application
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.example.gamingwakeup.model.Alarm
import com.example.gamingwakeup.repository.AlarmRepository
import com.example.gamingwakeup.room.database.AlarmDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class AddEditAlarmViewModel(private val repository: AlarmRepository, calender: Calendar) : BaseObservable() {
    @Bindable var soundVolume: Int = 50
    @Bindable var hour = calender.get(Calendar.HOUR_OF_DAY)
    @Bindable var minute = calender.get(Calendar.MINUTE)
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
        if (isNewAlarm) {
            val alarm = Alarm.createForInsert(
                hour = hour,
                minute = minute,
                soundVolume = soundVolume,
                hasVibration = currentHasVibration,
                isTurnedOn = true
            )
            createAlarm(alarm)
        } else {
            val alarm = Alarm(
                id = _alarmId,
                hour = hour,
                minute = minute,
                soundVolume = soundVolume,
                hasVibration = currentHasVibration,
                isTurnedOn = true
            )
            updateAlarm(alarm)
        }
    }

    suspend fun deleteAlarm() {
        if (!isNewAlarm) {
            val alarm = repository.getAlarm(_alarmId)
            repository.deleteAlarm(alarm)
        }
    }

    private fun createAlarm(alarm: Alarm) = GlobalScope.launch { repository.createAlarm(alarm) }

    private fun updateAlarm(alarm: Alarm) = GlobalScope.launch { repository.updateAlarm(alarm) }

    companion object {
        fun create(application: Application): AddEditAlarmViewModel {
            val calender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
            val database = AlarmDatabase.getInstance(application.applicationContext)
            val repository = AlarmRepository(database)
            return AddEditAlarmViewModel(repository, calender)
        }
    }
}

package com.example.gamingwakeup.viewmodel

import android.app.Application
import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.example.gamingwakeup.model.model.Alarm
import com.example.gamingwakeup.model.data.repository.AlarmRepository
import com.example.gamingwakeup.model.data.database.AlarmDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.*

class AddEditAlarmViewModel(
    private val repository: AlarmRepository,
    private val applicationContext: Context,
    calender: Calendar
) : BaseObservable() {
    @Bindable
    var soundVolume: Int = 50

    @Bindable
    var hour = calender.get(Calendar.HOUR_OF_DAY)

    @Bindable
    var minute = calender.get(Calendar.MINUTE)
    val hasVibration: LiveData<Boolean>
        get() = _hasVibration
    val navigateToAlarmListFragment: LiveData<Boolean>
        get() = _navigateToAlarmListFragment
    val toastMessageForAlarmListFragment: String
        get() = _toastMessageForAlarmListFragment
    private var _alarmId = 0
    private val _hasVibration = MutableLiveData(true)
    private var isNewAlarm = false
    private val _navigateToAlarmListFragment = MutableLiveData(false)
    private var _toastMessageForAlarmListFragment = ""

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

    fun saveAndScheduleAlarm() {
        try {
            val currentHasVibration = _hasVibration.value ?: return
            val alarm = Alarm(
                id = _alarmId,
                hour = hour,
                minute = minute,
                soundVolume = soundVolume,
                hasVibration = currentHasVibration,
                isTurnedOn = true
            )
            runBlocking {
                if (isNewAlarm) {
                    createAlarm(alarm)
                } else {
                    updateAlarm(alarm)
                }
                scheduleAlarm(alarm)
            }
            navigateBackToAlarmListFragment("Alarm set to ${alarm.clockTimeStringFormat()}")
        } catch (e: Exception) {
            navigateBackToAlarmListFragment("Failed to set alarm.")
        }
    }

    fun deleteAlarm() {
        try {
            runBlocking {
                if (!isNewAlarm) {
                    val alarm = Alarm.find(_alarmId, repository)
                    alarm.delete(repository)
                }
            }
            navigateBackToAlarmListFragment("Alarm removed.")
        } catch (e: Exception) {
            navigateBackToAlarmListFragment("Failed to delete alarm.")
        }
    }

    private fun createAlarm(alarm: Alarm) = GlobalScope.launch { alarm.create(repository) }

    private fun updateAlarm(alarm: Alarm) = GlobalScope.launch { alarm.update(repository) }

    private fun scheduleAlarm(alarm: Alarm) {
        alarm.schedule(applicationContext)
    }

    private fun navigateBackToAlarmListFragment(toastMessage: String) {
        _toastMessageForAlarmListFragment = toastMessage
        _navigateToAlarmListFragment.value = true
    }

    companion object {
        fun create(application: Application): AddEditAlarmViewModel {
            val applicationContext = application.applicationContext
            val calender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
            val database = AlarmDatabase.getInstance(applicationContext)
            val repository = AlarmRepository(database)
            return AddEditAlarmViewModel(repository, applicationContext, calender)
        }
    }
}

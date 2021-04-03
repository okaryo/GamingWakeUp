package com.example.gamingwakeup.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gamingwakeup.model.Alarm
import com.example.gamingwakeup.repository.AlarmRepository
import com.example.gamingwakeup.room.database.AlarmDatabase
import com.example.gamingwakeup.view.addeditalarm.AddEditAlarmFragmentArgs
import com.example.gamingwakeup.viewmodel.alarmlist.AlarmListViewModel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AddEditAlarmViewModel(private val repository: AlarmRepository) : ViewModel() {
    private val _alarmId = MutableLiveData<String?>()
    private val _hour = MutableLiveData<Int>()
    private val _minute = MutableLiveData<Int>()
    private val _soundVolume = MutableLiveData<Int>()
    private val _hasVibration = MutableLiveData(true)
    private var isNewAlarm = false

    val alarmId: LiveData<String?>
        get() = _alarmId
    val hour: LiveData<Int>
        get() = _hour
    val minute: LiveData<Int>
        get() = _minute
    val soundVolume: LiveData<Int>
        get() = _soundVolume
    val hasVibration: LiveData<Boolean>
        get() = _hasVibration

    fun initialize(alarmId: String?) {
        if (alarmId == null) {
            isNewAlarm = true
        }
        _hour.value = 10
        _minute.value = 10
        _soundVolume.value = 10
        _hasVibration.value = true

    }

    fun saveAlarm() {
        val currentHour = _hour.value
        val currentMinute = _minute.value
        val currentSoundVolume = _soundVolume.value
        val currentHasVibration = _hasVibration.value
        if (currentHour == null || currentMinute == null || currentSoundVolume == null || currentHasVibration == null) {
            return
        }
        if (isNewAlarm) {
            val alarm = Alarm.createForInsert(
                hour = currentHour,
                minute = currentMinute,
                soundVolume = currentSoundVolume,
                hasVibration = currentHasVibration,
                isTurnedOn = true
            )
            createAlarm(alarm)
        }
    }

    private fun createAlarm(alarm: Alarm) = viewModelScope.launch { repository.createAlarm(alarm) }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddEditAlarmViewModel::class.java)) {
                val database = AlarmDatabase.getInstance(application.applicationContext)
                val repository = AlarmRepository(database)
                return AddEditAlarmViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class!")
        }
    }
}
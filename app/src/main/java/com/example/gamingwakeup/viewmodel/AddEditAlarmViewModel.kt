package com.example.gamingwakeup.viewmodel

import android.app.Application
import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gamingwakeup.model.data.database.AlarmDatabase
import com.example.gamingwakeup.model.data.repository.AlarmRepository
import com.example.gamingwakeup.model.model.Alarm
import com.example.gamingwakeup.model.model.SoundSetting
import com.example.gamingwakeup.model.model.WeeklyRecurringSetting
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class AddEditAlarmViewModel private constructor(
    private val repository: AlarmRepository,
    private val applicationContext: Context,
    private val alarm: Alarm?,
    calender: Calendar
) : BaseObservable() {
    @Bindable
    var soundVolume: Int = 50
    @Bindable
    var hour = calender.get(Calendar.HOUR_OF_DAY)
    @Bindable
    var minute = calender.get(Calendar.MINUTE)
    val recurring: LiveData<Boolean>
        get() = _recurring
    val hasVibration: LiveData<Boolean>
        get() = _hasVibration
    val weeklyRecurringSetting: WeeklyRecurringSetting
        get() = _weeklyRecurringSetting
    val navigateToAlarmListFragment: LiveData<Boolean>
        get() = _navigateToAlarmListFragment
    val toastMessageForAlarmListFragment: String
        get() = _toastMessageForAlarmListFragment
    private var _alarmId = 0
    private val _recurring = MutableLiveData(true)
    private val _hasVibration = MutableLiveData(true)
    private var _weeklyRecurringSetting = WeeklyRecurringSetting(
        monday = true,
        tuesday = true,
        wednesday = true,
        thursday = true,
        friday = true,
        saturday = true,
        sunday = true
    )
    private var isNewAlarm = false
    private val _navigateToAlarmListFragment = MutableLiveData(false)
    private var _toastMessageForAlarmListFragment = ""

    init {
        if (alarm == null) {
            isNewAlarm = true
        } else {
            _alarmId = alarm.id
            hour = alarm.hour
            minute = alarm.minute
            soundVolume = alarm.sound.volume
            _recurring.value = alarm.recurring
            _hasVibration.value = alarm.vibration
            _weeklyRecurringSetting = alarm.weeklyRecurring
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
                sound = SoundSetting(name = "name", volume = soundVolume),
                vibration = currentHasVibration,
                recurring = true,
                weeklyRecurring = WeeklyRecurringSetting(
                    monday = true,
                    tuesday = true,
                    wednesday = true,
                    thursday = true,
                    friday = true,
                    saturday = true,
                    sunday = true
                ),
                active = true
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

    fun currentAlarm(): Alarm {
        return Alarm(
            id = _alarmId,
            hour = hour,
            minute = minute,
            vibration = _hasVibration.value!!,
            recurring = true,
            sound = SoundSetting(
                name = "sound title",
                volume = soundVolume
            ),
            weeklyRecurring = WeeklyRecurringSetting(
                monday = true,
                tuesday = true,
                wednesday = true,
                thursday = true,
                friday = true,
                saturday = true,
                sunday = true
            ),
            active = true
        )
    }

    fun onChangeRecurringSetting(isRecurring: Boolean) {
        _recurring.value = isRecurring
    }

    fun onChangeWeeklyRecurringSetting(index: Int) {
        val currentWeeklyRecurringSetting = _weeklyRecurringSetting
        _weeklyRecurringSetting = when (index) {
            0 -> currentWeeklyRecurringSetting.copy(monday = !currentWeeklyRecurringSetting.monday)
            1 -> currentWeeklyRecurringSetting.copy(tuesday = !currentWeeklyRecurringSetting.tuesday)
            2 -> currentWeeklyRecurringSetting.copy(wednesday = !currentWeeklyRecurringSetting.wednesday)
            3 -> currentWeeklyRecurringSetting.copy(thursday = !currentWeeklyRecurringSetting.thursday)
            4 -> currentWeeklyRecurringSetting.copy(friday = !currentWeeklyRecurringSetting.friday)
            5 -> currentWeeklyRecurringSetting.copy(saturday = !currentWeeklyRecurringSetting.saturday)
            else -> currentWeeklyRecurringSetting.copy(sunday = !currentWeeklyRecurringSetting.sunday)
        }
    }

    fun dayOfWeekRecurringSetting(index: Int): Boolean {
        return when (index) {
            0 -> _weeklyRecurringSetting.monday
            1 -> _weeklyRecurringSetting.tuesday
            2 -> _weeklyRecurringSetting.wednesday
            3 -> _weeklyRecurringSetting.thursday
            4 -> _weeklyRecurringSetting.friday
            5 -> _weeklyRecurringSetting.saturday
            else -> _weeklyRecurringSetting.sunday
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
        fun create(application: Application, alarm: Alarm?): AddEditAlarmViewModel {
            val applicationContext = application.applicationContext
            val calender = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
            val database = AlarmDatabase.getInstance(applicationContext)
            val repository = AlarmRepository(database)
            return AddEditAlarmViewModel(repository, applicationContext, alarm, calender)
        }
    }
}

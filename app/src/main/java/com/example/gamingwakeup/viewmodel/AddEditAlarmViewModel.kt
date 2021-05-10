package com.example.gamingwakeup.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gamingwakeup.R
import com.example.gamingwakeup.data.database.AlarmDatabase
import com.example.gamingwakeup.data.repository.AlarmRepository
import com.example.gamingwakeup.model.alarm.Alarm
import com.example.gamingwakeup.model.alarm.SoundSetting
import com.example.gamingwakeup.model.alarm.WeeklyRecurringSetting
import kotlinx.coroutines.*
import java.util.*

class AddEditAlarmViewModel private constructor(
    private val repository: AlarmRepository,
    private val applicationContext: Context,
    alarm: Alarm?,
    calender: Calendar
) : ViewModel(), Observable {
    @Bindable
    var hour: Int
    @Bindable
    var minute: Int
    val recurring: LiveData<Boolean>
        get() = _recurring
    val navigateToAlarmListFragment: LiveData<Boolean>
        get() = _navigateToAlarmListFragment
    val toastMessageForAlarmListFragment: String
        get() = _toastMessageForAlarmListFragment
    val soundTitle: String
        get() = _soundSetting.title
    private lateinit var _soundSetting: SoundSetting
    private val callbacks = PropertyChangeRegistry()
    private var _alarmId: Int
    private val _recurring = MutableLiveData<Boolean>()
    private val _hasVibration = MutableLiveData<Boolean>()
    private val _navigateToAlarmListFragment = MutableLiveData(false)
    private var _weeklyRecurringSetting: WeeklyRecurringSetting
    private var _isNewAlarm = false
    private var _toastMessageForAlarmListFragment = ""

    init {
        if (alarm == null || alarm.id == 0) _isNewAlarm = true

        if (alarm == null) {
            _alarmId = 0
            hour = calender.get(Calendar.HOUR_OF_DAY)
            minute = calender.get(Calendar.MINUTE)
            _recurring.value = true
            _hasVibration.value = true
            _weeklyRecurringSetting = WeeklyRecurringSetting(
                monday = true,
                tuesday = true,
                wednesday = true,
                thursday = true,
                friday = true,
                saturday = true,
                sunday = true
            )
            setupDefaultSoundSetting()
        } else {
            _alarmId = alarm.id
            hour = alarm.hour
            minute = alarm.minute
            _soundSetting = alarm.sound
            _recurring.value = alarm.recurring
            _hasVibration.value = alarm.vibration
            _weeklyRecurringSetting = alarm.weeklyRecurring
        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }

    fun toolBarTitle(): String {
        return if (_isNewAlarm) {
            applicationContext.getString(R.string.toolbar_title_create_alarm)
        } else {
            applicationContext.getString(R.string.toolbar_title_edit_alarm)
        }
    }

    fun onVibrationValueChanged(isChecked: Boolean) {
        _hasVibration.value = isChecked
    }

    fun saveAndScheduleAlarm() {
        try {
            val alarm = currentAlarm()
            runBlocking {
                if (_isNewAlarm) {
                    withContext(Dispatchers.IO) { alarm.create(repository) }
                } else {
                    withContext(Dispatchers.IO) { alarm.update(repository) }
                }
            }
            GlobalScope.launch { scheduleAlarm(alarm) }
            navigateBackToAlarmListFragment("Alarm set to ${alarm.clockTimeStringFormat()}")
        } catch (e: Exception) {
            navigateBackToAlarmListFragment("Failed to set alarm.")
        }
    }

    fun deleteAlarm() {
        try {
            runBlocking {
                if (!_isNewAlarm) {
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
            sound = _soundSetting,
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

    private fun setupDefaultSoundSetting() {
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.IS_ALARM
        )
        val selection = "${MediaStore.Audio.AudioColumns.IS_ALARM} != 0"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            applicationContext.contentResolver?.query(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                projection,
                Bundle().apply {
                    putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
                    putString(
                        ContentResolver.QUERY_ARG_SORT_COLUMNS,
                        MediaStore.Audio.AudioColumns.TITLE
                    )
                    putInt(
                        ContentResolver.QUERY_ARG_SORT_DIRECTION,
                        ContentResolver.QUERY_SORT_DIRECTION_ASCENDING
                    )
                    putInt(ContentResolver.QUERY_ARG_LIMIT, 1)
                },
                null
            )
        } else {
            val order = "${MediaStore.Audio.AudioColumns.TITLE} ASC LIMIT 1"
            applicationContext.contentResolver?.query(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                order
            )
        }.use { cursor ->
            val idColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val titleColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            if (cursor!!.moveToFirst()) {
                _soundSetting = SoundSetting(
                    id = cursor.getLong(idColumn!!),
                    title = cursor.getString(titleColumn!!),
                    volume = 50
                )
            }
        }
    }

    private fun scheduleAlarm(alarm: Alarm) {
        alarm.schedule(applicationContext)
    }

    private fun navigateBackToAlarmListFragment(toastMessage: String) {
        _toastMessageForAlarmListFragment = toastMessage
        _navigateToAlarmListFragment.value = true
    }

    class Factory(private val context: Context, private val alarm: Alarm?) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddEditAlarmViewModel::class.java)) {
                val applicationContext = context.applicationContext
                val calender =
                    Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"), Locale.JAPAN)
                val database = AlarmDatabase.getInstance(applicationContext)
                val repository = AlarmRepository(database)
                return AddEditAlarmViewModel(repository, applicationContext, alarm, calender) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class!")
        }
    }
}

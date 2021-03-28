package com.example.gamingwakeup.viewmodel.alarmlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamingwakeup.model.Alarm
import com.example.gamingwakeup.model.AlarmList

class AlarmListViewModel : ViewModel() {
    private val _alarmList = MutableLiveData<AlarmList>()
    val alarmList: LiveData<AlarmList>
        get() = _alarmList

    private val _navigateToAlarmDetailEdit = MutableLiveData<Alarm>()
    val navigateToAlarmDetailEdit: LiveData<Alarm>
        get() = _navigateToAlarmDetailEdit

    init {
        _alarmList.value =
            AlarmList(
                listOf(
                    Alarm(1, 29, 10, false,  true),
                    Alarm(18, 39, 12, false, false)
                )
            )
    }

    fun navigateToAlarmDetailEdit(alarm: Alarm) {
        _navigateToAlarmDetailEdit.value = alarm
    }
}

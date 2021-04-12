package com.example.gamingwakeup.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gamingwakeup.model.Alarm
import com.example.gamingwakeup.model.AlarmList
import com.example.gamingwakeup.repository.AlarmListRepository
import com.example.gamingwakeup.database.AlarmDatabase
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AlarmListViewModel(private val repository: AlarmListRepository) : ViewModel() {
    private val _alarmList = MutableLiveData<AlarmList>()
    val alarmList: LiveData<AlarmList>
        get() = _alarmList

    private val _navigateToAddEditAlarmFragment = MutableLiveData<Alarm>()
    val navigateToAddEditAlarmFragment: LiveData<Alarm>
        get() = _navigateToAddEditAlarmFragment

    init {
        viewModelScope.launch {
            val alarmList = repository.getAlarmList()
            _alarmList.value = alarmList
        }
    }

    fun navigateToAddEditAlarmFragment(alarm: Alarm) {
        _navigateToAddEditAlarmFragment.value = alarm
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlarmListViewModel::class.java)) {
                val database = AlarmDatabase.getInstance(application.applicationContext)
                val repository = AlarmListRepository(database)
                return AlarmListViewModel(
                    repository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class!")
        }
    }
}

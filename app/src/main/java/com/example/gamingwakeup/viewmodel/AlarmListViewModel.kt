package com.example.gamingwakeup.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.gamingwakeup.data.database.AlarmDatabase
import com.example.gamingwakeup.data.repository.AlarmListRepository
import com.example.gamingwakeup.model.alarm.Alarm
import com.example.gamingwakeup.model.alarm.AlarmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlarmListViewModel(private val repository: AlarmListRepository) : ViewModel() {
    val alarmList: LiveData<AlarmList>
        get() = _alarmList
    val navigateToAddEditAlarmFragment: LiveData<Alarm>
        get() = _navigateToAddEditAlarmFragment
    private val _alarmList = MutableLiveData<AlarmList>()
    private val _navigateToAddEditAlarmFragment = MutableLiveData<Alarm>()

    init {
        viewModelScope.launch {
            _alarmList.value = withContext(Dispatchers.IO) {
                repository.getAlarmList().sort()
            }
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
                return AlarmListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class!")
        }
    }
}

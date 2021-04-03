package com.example.gamingwakeup.viewmodel.alarmlist

import android.app.Application
import androidx.lifecycle.*
import com.example.gamingwakeup.model.Alarm
import com.example.gamingwakeup.model.AlarmList
import com.example.gamingwakeup.repository.AlarmListRepository
import com.example.gamingwakeup.repository.AlarmRepository
import com.example.gamingwakeup.room.database.AlarmDatabase
import com.example.gamingwakeup.viewmodel.AddEditAlarmViewModel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AlarmListViewModel(private val repository: AlarmListRepository) : ViewModel() {
    private val _alarmList = MutableLiveData<AlarmList>()
    val alarmList: LiveData<AlarmList>
        get() = _alarmList

    private val _navigateToAlarmDetailEdit = MutableLiveData<Alarm>()
    val navigateToAlarmDetailEdit: LiveData<Alarm>
        get() = _navigateToAlarmDetailEdit

    init {
        viewModelScope.launch {
            val alarmList = repository.getAlarmList()
            _alarmList.value = alarmList
        }
    }

    fun navigateToAlarmDetailEdit(alarm: Alarm) {
        _navigateToAlarmDetailEdit.value = alarm
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

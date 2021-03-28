package com.example.gamingwakeup.viewmodel.alarmlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class AlarmListViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmListViewModel::class.java)) {
            return AlarmListViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class!")
    }
}

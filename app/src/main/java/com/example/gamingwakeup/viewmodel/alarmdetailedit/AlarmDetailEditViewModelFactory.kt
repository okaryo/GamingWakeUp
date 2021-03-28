package com.example.gamingwakeup.viewmodel.alarmdetailedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class AlarmDetailEditViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmDetailEditViewModel::class.java)) {
            return AlarmDetailEditViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class!")
    }
}

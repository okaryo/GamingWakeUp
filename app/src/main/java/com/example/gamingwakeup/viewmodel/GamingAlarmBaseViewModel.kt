package com.example.gamingwakeup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gamingwakeup.model.model.GamingAlarmBase
import java.lang.IllegalArgumentException

interface GamingAlarmBaseViewModel {
    val isComplete: LiveData<Boolean>
    val isFailure: LiveData<Boolean>
}

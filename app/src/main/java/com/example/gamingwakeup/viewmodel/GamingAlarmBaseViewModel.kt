package com.example.gamingwakeup.viewmodel

import androidx.lifecycle.LiveData

interface GamingAlarmBaseViewModel {
    val isComplete: LiveData<Boolean>
    val isFailure: LiveData<Boolean>
}

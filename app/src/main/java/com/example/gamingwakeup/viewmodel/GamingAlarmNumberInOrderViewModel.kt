package com.example.gamingwakeup.viewmodel

import androidx.lifecycle.*
import com.example.gamingwakeup.model.game.GameNumberInOrder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

class GamingAlarmNumberInOrderViewModel(private val gamingAlarm: GameNumberInOrder) : ViewModel(),
    GamingAlarmBaseViewModel {
    override val isComplete: LiveData<Boolean>
        get() = _isComplete
    override val isFailure: LiveData<Boolean>
        get() = _isFailure
    val currentTimeHour: LiveData<Int>
        get() = _currentTimeHour
    val currentTimeMinute: LiveData<Int>
        get() = _currentTimeMinute
    private val _isComplete = MutableLiveData(false)
    private val _isFailure = MutableLiveData(false)
    private val _currentTimeHour = MutableLiveData<Int>()
    private val _currentTimeMinute = MutableLiveData<Int>()

    init {
        val currentTime = LocalTime.now()
        _currentTimeHour.value = currentTime.hour
        _currentTimeMinute.value = currentTime.minute
        startTimeScheduler()
    }

    fun onTappedNumberButton(number: Int): Boolean {
        return if (gamingAlarm.verifySubmission(number)) {
            if (gamingAlarm.isCompleted()) _isComplete.value = true
            true
        } else {
            false
        }
    }

    private fun startTimeScheduler() {
        viewModelScope.launch {
            while (!_isComplete.value!!) {
                delay(1000)
                updateCurrentTime()
            }
        }
    }

    private fun updateCurrentTime() {
        val currentTime = LocalTime.now()
        _currentTimeHour.value = currentTime.hour
        _currentTimeMinute.value = currentTime.minute
    }

    class Factory(private val gamingAlarm: GameNumberInOrder) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GamingAlarmNumberInOrderViewModel::class.java)) {
                return GamingAlarmNumberInOrderViewModel(gamingAlarm) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class!")
        }
    }
}

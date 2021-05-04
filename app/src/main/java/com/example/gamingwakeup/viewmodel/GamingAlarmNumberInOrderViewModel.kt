package com.example.gamingwakeup.viewmodel

import androidx.lifecycle.*
import com.example.gamingwakeup.model.model.GamingAlarmBase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

class GamingAlarmNumberInOrderViewModel: ViewModel(), GamingAlarmBaseViewModel {
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
    private val tappedNumber = mutableListOf<Int>()
    private val _currentTimeHour = MutableLiveData<Int>()
    private val _currentTimeMinute = MutableLiveData<Int>()

    init {
        val currentTime = LocalTime.now()
        _currentTimeHour.value = currentTime.hour
        _currentTimeMinute.value = currentTime.minute
        startTimeScheduler()
    }

    fun onTappedNumberButton(number: Int): Boolean {
        val lastNumber = 9
        val nextCorrectNumber = tappedNumber.size + 1
        val isGameComplete = number == lastNumber && number == nextCorrectNumber
        if (number != nextCorrectNumber) {
            tappedNumber.clear()
            return false
        }
        if (isGameComplete) {
            _isComplete.value = true
        }
        tappedNumber.add(number)
        return true
    }

    private fun startTimeScheduler() {
        val updateCurrentTime = {
            val currentTime = LocalTime.now()
            println(currentTime)
            _currentTimeHour.value = currentTime.hour
            _currentTimeMinute.value = currentTime.minute
        }
        viewModelScope.launch {
            while(!_isComplete.value!!) {
                delay(10000)
                updateCurrentTime()
            }
        }
    }

    class Factory(private val gamingAlarm: GamingAlarmBase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GamingAlarmNumberInOrderViewModel::class.java)) {
                return GamingAlarmNumberInOrderViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class!")
        }
    }
}

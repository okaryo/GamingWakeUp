package com.example.gamingwakeup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gamingwakeup.model.model.GamingAlarmBase
import java.lang.IllegalArgumentException
import java.util.*

class GamingAlarmNumberInOrderViewModel: ViewModel(), GamingAlarmBaseViewModel {
    private val _isComplete = MutableLiveData(false)
    override val isComplete: LiveData<Boolean>
        get() = _isComplete
    private val _isFailure = MutableLiveData(false)
    override val isFailure: LiveData<Boolean>
        get() = _isFailure

    private val tappedNumber = mutableListOf<Int>()

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

    class Factory(private val gamingAlarm: GamingAlarmBase) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GamingAlarmNumberInOrderViewModel::class.java)) {
                return GamingAlarmNumberInOrderViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class!")
        }

    }
}

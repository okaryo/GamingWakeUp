package com.example.gamingwakeup.model

data class Alarm(
    val hour: Int,
    val minute: Int,
    val soundVolume: Int,
    val hasVibration: Boolean = true,
    val isOn: Boolean = false
)

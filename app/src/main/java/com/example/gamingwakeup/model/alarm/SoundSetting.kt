package com.example.gamingwakeup.model.alarm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoundSetting(
    val id: Long,
    val title: String,
    val volume: Int
): Parcelable

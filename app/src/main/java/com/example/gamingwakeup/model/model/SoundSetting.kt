package com.example.gamingwakeup.model.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoundSetting(
    val name: String,
    val volume: Int
): Parcelable

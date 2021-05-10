package com.example.gamingwakeup.model.alarm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeeklyRecurringSetting(
    val monday: Boolean,
    val tuesday: Boolean,
    val wednesday: Boolean,
    val thursday: Boolean,
    val friday: Boolean,
    val saturday: Boolean,
    val sunday: Boolean
): Parcelable

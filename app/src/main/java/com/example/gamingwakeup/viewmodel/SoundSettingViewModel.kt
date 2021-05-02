package com.example.gamingwakeup.viewmodel

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamingwakeup.model.model.Alarm
import com.example.gamingwakeup.model.model.SoundSetting

class SoundSettingViewModel private constructor(
    private val alarm: Alarm,
    private val applicationContext: Context
) : ViewModel() {
    val volume: LiveData<Int>
        get() = _volume
    val name: LiveData<String>
        get() = _name
    val soundTitles: List<String>
        get() = _soundTitles
    private val _volume = MutableLiveData<Int>()
    private val _name = MutableLiveData<String>()
    private val _soundTitles = mutableListOf<String>()

    init {
        _volume.value = alarm.sound.volume
        _name.value = alarm.sound.name
        fetchLocalAlarms()
    }

    fun currentAlarm(): Alarm {
        return alarm.copy(
            sound = SoundSetting(
                name = _name.value!!,
                volume = _volume.value!!
            )
        )
    }

    private fun fetchLocalAlarms() {
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.TITLE,
            MediaStore.Audio.AudioColumns.IS_ALARM
        )
        val selection = "${MediaStore.Audio.AudioColumns.IS_ALARM} != 0"
        val order = "${MediaStore.Audio.AudioColumns.TITLE} ASC"
        applicationContext.contentResolver?.query(
            MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            order
        ).use { cursor ->
            val titleColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            while (cursor!!.moveToNext()) {
                _soundTitles.add(cursor.getString(titleColumn!!))
            }
        }
    }

    companion object {
        fun crate(alarm: Alarm, context: Context): SoundSettingViewModel {
            val applicationContext = context.applicationContext
            return SoundSettingViewModel(alarm, applicationContext)
        }
    }
}

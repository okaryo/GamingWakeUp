package com.example.gamingwakeup.viewmodel

import android.content.ContentUris
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
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
    val selectedSoundTitle: LiveData<String>
        get() = _selectedSoundTitle
    val isSoundPlaying: Boolean
        get() = _mediaPlayer.isPlaying
    val soundTitles: Map<String, Long>
        get() = _soundTitles
    private val _volume = MutableLiveData<Int>()
    private val _selectedSoundTitle = MutableLiveData<String>()
    private val _soundTitles = mutableMapOf<String, Long>()
    private var _mediaPlayer = MediaPlayer()

    init {
        _volume.value = alarm.sound.volume
        _selectedSoundTitle.value = alarm.sound.title
        fetchLocalAlarms()
    }

    override fun onCleared() {
        _mediaPlayer.release()
        super.onCleared()
    }

    fun currentAlarm(): Alarm {
        return alarm.copy(
            sound = SoundSetting(
                title = _selectedSoundTitle.value!!,
                volume = _volume.value!!
            )
        )
    }

    fun onTapSoundTitle(title: String) {
        if (title == _selectedSoundTitle.value) {
            if (_mediaPlayer.isPlaying) stopSelectedSound()
            else startSelectedSound()
        } else {
            stopSelectedSound()
            _selectedSoundTitle.value = title
            startSelectedSound()
        }
    }

    private fun startSelectedSound() {
        val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, _soundTitles[_selectedSoundTitle.value]!!)
        _mediaPlayer = MediaPlayer().apply {
            isLooping = true
            setAudioAttributes(AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
            setDataSource(applicationContext, uri)
            prepare()
        }
        _mediaPlayer.start()
    }

    private fun stopSelectedSound() = _mediaPlayer.stop()

    private fun fetchLocalAlarms() {
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns._ID,
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
            val idColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val titleColumn = cursor?.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            while (cursor!!.moveToNext()) {
                val title = cursor.getString(titleColumn!!)
                val id = cursor.getLong(idColumn!!)
                _soundTitles[title] = id
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

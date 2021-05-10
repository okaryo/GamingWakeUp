package com.example.gamingwakeup.viewmodel

import android.content.ContentUris
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gamingwakeup.model.alarm.Alarm
import com.example.gamingwakeup.model.alarm.SoundSetting

class SoundSettingViewModel private constructor(
    private val alarm: Alarm,
    private val applicationContext: Context
) : ViewModel() {
    val volume: Int
        get() = _volume
    val selectedSoundTitle: LiveData<String>
        get() = _selectedSoundTitle
    val isSoundPlaying: LiveData<Boolean>
        get() = _isSoundPlaying
    val soundTitles: Map<String, Long>
        get() = _soundTitles
    private var _volume = 50
    private val _selectedSoundTitle = MutableLiveData<String>()
    private val _isSoundPlaying = MutableLiveData(false)
    private val _soundTitles = mutableMapOf<String, Long>()
    private var _mediaPlayer = MediaPlayer()

    init {
        _volume = alarm.sound.volume
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
                id = soundTitles[_selectedSoundTitle.value]!!,
                title = _selectedSoundTitle.value!!,
                volume = _volume
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

    fun onChangeSoundVolume(value: Int) {
        _volume = value
        val soundVolume = _volume.toFloat() / 100
        _mediaPlayer.setVolume(soundVolume, soundVolume)
    }

    private fun startSelectedSound() {
        val uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
            _soundTitles[_selectedSoundTitle.value]!!
        )
        val soundVolume = _volume.toFloat() / 100
        _mediaPlayer = MediaPlayer().apply {
            isLooping = true
            setVolume(soundVolume, soundVolume)
            setAudioAttributes(
                AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build()
            )
            setDataSource(applicationContext, uri)
            prepare()
        }
        _mediaPlayer.start()
        _isSoundPlaying.value = true
    }

    private fun stopSelectedSound() {
        _mediaPlayer.stop()
        _isSoundPlaying.value = false
    }

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

    class Factory(private val context: Context, private val alarm: Alarm) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SoundSettingViewModel::class.java)) {
                return SoundSettingViewModel(alarm, context.applicationContext) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class!")
        }
    }
}

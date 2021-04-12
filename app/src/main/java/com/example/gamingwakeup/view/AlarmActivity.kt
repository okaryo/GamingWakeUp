package com.example.gamingwakeup.view

import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.ActivityAlarmBinding

class AlarmActivity : Activity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm).apply {
            isLooping = true
        }
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        mediaPlayer.start()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val vibrationEffect =
                VibrationEffect.createWaveform(longArrayOf(100, 100), intArrayOf(100, 100), 1)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(300)
        }

        val binding: ActivityAlarmBinding = DataBindingUtil.setContentView(this, R.layout.activity_alarm)
        val button = binding.button
        button.setOnClickListener {
            Log.d("activity", "click!")
            mediaPlayer.stop()
        }


    }
}

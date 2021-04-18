package com.example.gamingwakeup.view.fragment

import android.content.Context
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.gamingwakeup.R
import com.example.gamingwakeup.viewmodel.GamingAlarmBaseViewModel

interface GamingAlarmBaseFragment {
    val mediaPlayer: MediaPlayer
    val vibrator: Vibrator

    fun observeOnGameCompleted()
    fun startAlarmAndVibration()
    fun stopAlarmAndVibration()
//    private val mediaPlayer = MediaPlayer.create(context, R.raw.alarm)
//    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

//    private val viewModel: GamingAlarmViewModel by lazy {
//        val gamingAlarm = GamingAlarmNumberInOrder()
//        GamingAlarmViewModel.Factory(gamingAlarm).create(GamingAlarmViewModel::class.java)
//    }

//    private fun observeOnGameCompleted() {
//        viewModel.isComplete.observe(viewLifecycleOwner, Observer { isComplete ->
//            if (isComplete) {
//               stopAlarmAndVibration()
//                this.onDestroy()
//            }
//        })
//    }

//    private fun startAlarmAndVibration() {
//        mediaPlayer.isLooping = true
//        mediaPlayer.start()
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            val vibrationEffect =
//                VibrationEffect.createWaveform(longArrayOf(100, 100), intArrayOf(100, 100), 1)
//            vibrator.vibrate(vibrationEffect)
//        } else {
//            vibrator.vibrate(300)
//        }
//    }
//
//    private fun stopAlarmAndVibration() {
//        mediaPlayer.stop()
//    }
}


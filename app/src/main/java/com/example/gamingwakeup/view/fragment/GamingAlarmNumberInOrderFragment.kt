package com.example.gamingwakeup.view.fragment

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.FragmentGamingAlarmNumberInOrderBinding
import com.example.gamingwakeup.model.model.GamingAlarmNumberInOrder
import com.example.gamingwakeup.viewmodel.GamingAlarmNumberInOrderViewModel

class GamingAlarmNumberInOrderFragment(context: Context) : Fragment(), GamingAlarmBaseFragment {
    override val mediaPlayer = MediaPlayer.create(context, R.raw.alarm)!!
    override val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private lateinit var binding: FragmentGamingAlarmNumberInOrderBinding
    private val viewModel: GamingAlarmNumberInOrderViewModel by lazy {
        val gamingAlarm = GamingAlarmNumberInOrder()
        GamingAlarmNumberInOrderViewModel.Factory(gamingAlarm).create(
            GamingAlarmNumberInOrderViewModel::class.java
        )
    }

    init {
        startAlarmAndVibration()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGamingAlarmNumberInOrderBinding.inflate(inflater, container, false)
        setupNumberButton()
        observeOnGameCompleted()

        return binding.root
    }

    override fun observeOnGameCompleted() {
        viewModel.isComplete.observe(viewLifecycleOwner, Observer { isComplete ->
            if (isComplete) {
                stopAlarmAndVibration()
                // TODO: ゲームクリアしたらアクティビティを破棄したい。
                this.onDestroy()
            }
        })
    }

    override fun startAlarmAndVibration() {
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val vibrationEffect =
                VibrationEffect.createWaveform(longArrayOf(100, 100), intArrayOf(100, 100), 1)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(300)
        }
    }

    override fun stopAlarmAndVibration() {
        mediaPlayer.stop()
    }

    private fun setupNumberButton() {
        val numberButtons = listOf(
            binding.gameNumberInOrderButton1,
            binding.gameNumberInOrderButton2,
            binding.gameNumberInOrderButton3,
            binding.gameNumberInOrderButton4,
            binding.gameNumberInOrderButton5,
            binding.gameNumberInOrderButton6,
            binding.gameNumberInOrderButton7,
            binding.gameNumberInOrderButton8,
            binding.gameNumberInOrderButton9
        )
        for (button in numberButtons) {
            button.setOnClickListener {
                val number = Integer.parseInt(button.text.toString())
                val isSuccess = viewModel.onTappedNumberButton(number)
                if (isSuccess) {
                    val successColor = resources.getColor(R.color.primary)
                    button.setBackgroundColor(successColor)
                } else {
                    numberButtons.forEach {
                        val originColor = resources.getColor(R.color.white)
                        it.setBackgroundColor(originColor)
                    }
                }
            }
        }
    }
}

package com.example.gamingwakeup.view.fragment

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
        setupBinding(inflater, container)
        setupNumberButton()
        observeOnGameCompleted()
        setupCurrentTimeObserver()

        return binding.root
    }

    override fun onDestroy() {
        activity?.finishAndRemoveTask()

        super.onDestroy()
    }

    override fun observeOnGameCompleted() {
        viewModel.isComplete.observe(viewLifecycleOwner, Observer { isCompleted ->
            if (isCompleted) {
                stopAlarmAndVibration()
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

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentGamingAlarmNumberInOrderBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
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
        setupNumberButtonText(numberButtons)
        setupNumberButtonListener(numberButtons)
    }

    private fun setupNumberButtonText(numberButtons: List<Button>) {
        val numbers = (1..9).shuffled()
        for ((index, button) in numberButtons.withIndex()) {
            button.text = numbers[index].toString()
        }
    }

    private fun setupNumberButtonListener(numberButtons: List<Button>) {
        for (button in numberButtons) {
            button.setOnClickListener {
                val number = Integer.parseInt(button.text.toString())
                val isSuccess = viewModel.onTappedNumberButton(number)
                if (isSuccess) {
                    button.background =
                        resources.getDrawable(R.drawable.style_game_number_panel_tapped, null)
                } else {
                    numberButtons.forEach {
                        it.background =
                            resources.getDrawable(R.drawable.style_game_number_panel_untapped, null)
                    }
                }
            }
        }
    }

    private fun setupCurrentTimeObserver() {
        val currentTime = binding.currentTime
        val changeCurrentTime = {
            currentTime.text = String.format(
                getString(R.string.time_format),
                viewModel.currentTimeHour.value,
                viewModel.currentTimeMinute.value
            )
        }
        viewModel.currentTimeHour.observe(viewLifecycleOwner, Observer { changeCurrentTime() })
        viewModel.currentTimeMinute.observe(viewLifecycleOwner, Observer { changeCurrentTime() })
    }
}

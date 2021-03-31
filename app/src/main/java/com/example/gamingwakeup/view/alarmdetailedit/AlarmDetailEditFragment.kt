package com.example.gamingwakeup.view.alarmdetailedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gamingwakeup.databinding.FragmentAlarmDetailEditBinding
import com.example.gamingwakeup.viewmodel.alarmdetailedit.AlarmDetailEditViewModel
import com.example.gamingwakeup.viewmodel.alarmdetailedit.AlarmDetailEditViewModelFactory

class AlarmDetailEditFragment : Fragment() {
    private lateinit var viewModel: AlarmDetailEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAlarmDetailEditBinding.inflate(layoutInflater)
        val viewModelFactory = AlarmDetailEditViewModelFactory()
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(AlarmDetailEditViewModel::class.java)
        binding.viewmodel = viewModel

        setTimePickerTo24HourView(binding)

        return binding.root
    }

    private fun setTimePickerTo24HourView(binding: FragmentAlarmDetailEditBinding) {
        val timePicker = binding.alarmEditTimePicker
        timePicker.setIs24HourView(true)
    }
}

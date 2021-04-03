package com.example.gamingwakeup.view.addeditalarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gamingwakeup.databinding.FragmentAddEditAlarmBinding
import com.example.gamingwakeup.viewmodel.AddEditAlarmViewModel
import java.lang.Exception

class AddEditAlarmFragment : Fragment() {
    private lateinit var binding: FragmentAddEditAlarmBinding

    private val viewModel: AddEditAlarmViewModel by lazy {
        val activity = requireNotNull(this.activity)
        AddEditAlarmViewModel.Factory(activity.application)
            .create(AddEditAlarmViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditAlarmBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val arguments = AddEditAlarmFragmentArgs.fromBundle(requireArguments())
        viewModel.initialize(arguments.alarmId)

        setTimePickerTo24HourView()
        setClickButtonListener()

        return binding.root
    }

    private fun setTimePickerTo24HourView() {
        val timePicker = binding.alarmEditTimePicker
        timePicker.setIs24HourView(true)
    }

    private fun setClickButtonListener() {
        val button = binding.addEditButton
        button.setOnClickListener {
            try {
                viewModel.saveAlarm()
                this.findNavController()
                    .navigate(AddEditAlarmFragmentDirections.actionAddEditAlarmFragmentToAlarmListFragment())
            } catch (e: Exception) {
                // TODO: いつか良い感じにハンドリングする！
                println("Failed to create or update alarm...")
            }
        }
    }
}

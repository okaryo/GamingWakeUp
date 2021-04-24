package com.example.gamingwakeup.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.FragmentAddEditAlarmBinding
import com.example.gamingwakeup.viewmodel.AddEditAlarmViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class AddEditAlarmFragment : Fragment() {
    private lateinit var binding: FragmentAddEditAlarmBinding
    private lateinit var arguments: AddEditAlarmFragmentArgs

    private val viewModel: AddEditAlarmViewModel by lazy {
        val activity = requireNotNull(this.activity)
        AddEditAlarmViewModel.create(activity.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBinding(inflater, container)
        setupViewModel()
        setupTimePickerTo24HourView()
        setupClickButtonListener()
        setupToolbar()
        setupListener()
        setupNavigationObserver()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentAddEditAlarmBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupViewModel() {
        arguments = AddEditAlarmFragmentArgs.fromBundle(requireArguments())
        viewModel.initialize(arguments.alarmId)
    }

    private fun setupTimePickerTo24HourView() {
        val timePicker = binding.alarmEditTimePicker
        timePicker.setIs24HourView(true)
    }

    private fun setupClickButtonListener() {
        val button = binding.addEditButton
        button.setOnClickListener {
            viewModel.saveAndScheduleAlarm()
        }
    }

    private fun setupToolbar() {
        val navigation = this.findNavController()
        binding.toolbar.apply {
            title = if (arguments.alarmId == 0) getString(R.string.toolbar_title_create_alarm)
            else getString(R.string.toolbar_title_edit_alarm)

            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                navigation
                    .navigate(
                        AddEditAlarmFragmentDirections.actionAddEditAlarmFragmentToAlarmListFragment(
                            viewModel.toastMessageForAlarmListFragment
                        )
                    )
            }
            if (arguments.alarmId != 0) {
                inflateMenu(R.menu.menu_delete_alarm)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_item_delete_alarm -> viewModel.deleteAlarm()
                    }
                    true
                }
            }
        }
    }

    private fun setupListener() {
        val vibrationSwitch = binding.vibrationSwitch
        vibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onVibrationValueChanged(isChecked)
        }
    }

    private fun setupNavigationObserver() {
        viewModel.navigateToAlarmListFragment.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(
                    AddEditAlarmFragmentDirections.actionAddEditAlarmFragmentToAlarmListFragment(
                        viewModel.toastMessageForAlarmListFragment
                    )
                )
            }
        })
    }
}

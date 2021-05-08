package com.example.gamingwakeup.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.FragmentAddEditAlarmBinding
import com.example.gamingwakeup.viewmodel.AddEditAlarmViewModel

class AddEditAlarmFragment : Fragment() {
    private lateinit var binding: FragmentAddEditAlarmBinding
    private lateinit var arguments: AddEditAlarmFragmentArgs

    private val viewModel: AddEditAlarmViewModel by lazy {
        arguments = AddEditAlarmFragmentArgs.fromBundle(requireArguments())
        val activity = requireNotNull(this.activity)
        val factory = AddEditAlarmViewModel.Factory(activity.applicationContext, arguments.alarm)
        ViewModelProvider(this, factory).get(AddEditAlarmViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBinding(inflater, container)
        setupTimePickerTo24HourView()
        setupClickButtonListener()
        setupToolbar()
        setupListener()
        setupNavigationObserver()
        setupNavigationToSoundSettingPage()
        setupRecurringSettingSection()
        setupWeeklyRecurringSettingSection()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentAddEditAlarmBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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
        binding.toolbar.apply {
            title = viewModel.toolBarTitle()
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                this.findNavController()
                    .navigate(
                        AddEditAlarmFragmentDirections.actionAddEditAlarmFragmentToAlarmListFragment(
                            viewModel.toastMessageForAlarmListFragment
                        )
                    )
            }
            if (arguments.alarm != null) {
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

    private fun setupNavigationToSoundSettingPage() {
        val soundSettingSection = binding.soundSettingSection
        soundSettingSection.setOnClickListener {
            findNavController().navigate(
                AddEditAlarmFragmentDirections.actionAddEditAlarmFragmentToSoundSettingFragment(
                    viewModel.currentAlarm()
                )
            )
        }
    }

    private fun setupRecurringSettingSection() {
        val recurringCheckbox = binding.recurringCheckbox
        recurringCheckbox.setOnCheckedChangeListener { _, isChecked ->
            val weeklyRecurringSettingSection = binding.WeeklyRecurringSettingSection
            viewModel.onChangeRecurringSetting(isChecked)
            if (isChecked) {
                weeklyRecurringSettingSection.visibility = View.VISIBLE
            } else {
                weeklyRecurringSettingSection.visibility = View.GONE
            }
        }
    }

    private fun setupWeeklyRecurringSettingSection() {
        val weeklyRecurringSettingButtons = listOf(
            binding.mondayRecurringButton,
            binding.tuesdayRecurringButton,
            binding.wednesdayRecurringButton,
            binding.thursdayRecurringButton,
            binding.fridayRecurringButton,
            binding.saturdayRecurringButton,
            binding.sundayRecurringButton
        )
        val changeButtonStyle = { button: View, recurringSetting: Boolean ->
            button.background = if (recurringSetting) {
                resources.getDrawable(
                    R.drawable.shape_weekly_recurring_setting_button_selected,
                    null
                )
            } else {
                resources.getDrawable(
                    R.drawable.shape_weekly_recurring_setting_button_unselected,
                    null
                )
            }
        }
        weeklyRecurringSettingButtons.forEachIndexed { index, button ->
            changeButtonStyle(button, viewModel.dayOfWeekRecurringSetting(index))
            button.setOnClickListener {
                viewModel.onChangeWeeklyRecurringSetting(index)
                changeButtonStyle(button, viewModel.dayOfWeekRecurringSetting(index))
            }
        }
    }
}

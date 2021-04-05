package com.example.gamingwakeup.view.addeditalarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.FragmentAddEditAlarmBinding
import com.example.gamingwakeup.viewmodel.AddEditAlarmViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class AddEditAlarmFragment : Fragment() {
    private lateinit var binding: FragmentAddEditAlarmBinding
    private lateinit var arguments: AddEditAlarmFragmentArgs

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
        arguments = AddEditAlarmFragmentArgs.fromBundle(requireArguments())
        viewModel.initialize(arguments.alarmId)

        setTimePickerTo24HourView()
        setClickButtonListener()
        setToolbar()

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

    private fun setToolbar() {
        val navigation = this.findNavController()
        binding.toolbar.apply {
            title = if (arguments.alarmId == 0) getString(R.string.toolbar_title_create_alarm)
            else getString(R.string.toolbar_title_edit_alarm)

            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                navigation
                    .navigate(AddEditAlarmFragmentDirections.actionAddEditAlarmFragmentToAlarmListFragment())
            }
            if (arguments.alarmId != 0) {
                inflateMenu(R.menu.menu_delete_alarm)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_item_delete_alarm -> {
                            GlobalScope.launch {
                                launch { viewModel.deleteAlarm() }.join()
                                findNavController().navigate(
                                    AddEditAlarmFragmentDirections.actionAddEditAlarmFragmentToAlarmListFragment()
                                )
                            }

                        }
                    }
                    true
                }
            }
        }
    }
}

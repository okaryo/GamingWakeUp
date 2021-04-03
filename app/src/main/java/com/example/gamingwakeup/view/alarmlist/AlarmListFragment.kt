package com.example.gamingwakeup.view.alarmlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gamingwakeup.databinding.FragmentAlarmListBinding
import com.example.gamingwakeup.viewmodel.alarmlist.AlarmListViewModel

class AlarmListFragment : Fragment() {
    private lateinit var binding: FragmentAlarmListBinding

    private val viewModel: AlarmListViewModel by lazy {
        val activity = requireNotNull(this.activity)
        AlarmListViewModel.Factory(activity.application)
            .create(AlarmListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        setAdapterToRecyclerView()
        setNavigationObserver()
        setFloatingActionButton()

        return binding.root
    }

    private fun setAdapterToRecyclerView() {
        viewModel.alarmList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val recyclerView = binding.alarmListRecyclerView
                recyclerView.adapter = AlarmListAdapter(AlarmListAdapter.OnClickListener {alarm ->
                    viewModel.navigateToAlarmDetailEdit(alarm)
                })
            }
        })
    }

    private fun setNavigationObserver() {
        val navigation = viewModel.navigateToAlarmDetailEdit
        navigation.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(AlarmListFragmentDirections.actionAlarmListFragmentToAlarmDetailEditFragment())
            }
        })
    }

    private fun setFloatingActionButton() {
        val fab = binding.fabCreateAlarm
        fab.setOnClickListener {
            this.findNavController().navigate(
                AlarmListFragmentDirections.actionAlarmListFragmentToAddEditAlarmFragment(null)
            )
        }
    }
}

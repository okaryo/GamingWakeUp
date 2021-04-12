package com.example.gamingwakeup.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.gamingwakeup.databinding.FragmentAlarmListBinding
import com.example.gamingwakeup.view.adapter.AlarmListAdapter
import com.example.gamingwakeup.viewmodel.AlarmListViewModel

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
        binding.viewModel = viewModel
        setupAdapterToRecyclerView()
        setupAdapter()
        setupNavigationObserver()
        setupFloatingActionButton()

        return binding.root
    }

    private fun setupAdapterToRecyclerView() {
        val recyclerView = binding.alarmListRecyclerView
        recyclerView.adapter = AlarmListAdapter(AlarmListAdapter.OnClickListener { alarm ->
            viewModel.navigateToAddEditAlarmFragment(alarm)
        })
    }

    private fun setupAdapter() {
        viewModel.alarmList.observe(viewLifecycleOwner, Observer { alarmList ->
            if (alarmList != null) {
                val adapter = binding.alarmListRecyclerView.adapter as AlarmListAdapter
                adapter.submitList(alarmList.values)
            }
        })
    }

    private fun setupNavigationObserver() {
        val navigation = viewModel.navigateToAddEditAlarmFragment
        navigation.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(
                        AlarmListFragmentDirections.actionAlarmListFragmentToAddEditAlarmFragment(
                            it.id
                        )
                    )
            }
        })
    }

    private fun setupFloatingActionButton() {
        val fab = binding.fabCreateAlarm
        fab.setOnClickListener {
            this.findNavController().navigate(
                AlarmListFragmentDirections.actionAlarmListFragmentToAddEditAlarmFragment(
                    0
                )
            )
        }
    }
}

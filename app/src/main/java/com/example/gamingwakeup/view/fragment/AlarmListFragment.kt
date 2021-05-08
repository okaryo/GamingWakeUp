package com.example.gamingwakeup.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamingwakeup.databinding.FragmentAlarmListBinding
import com.example.gamingwakeup.view.adapter.AlarmListAdapter
import com.example.gamingwakeup.viewmodel.AlarmListViewModel

class AlarmListFragment : Fragment() {
    private val args: AlarmListFragmentArgs by navArgs()
    private lateinit var binding: FragmentAlarmListBinding
    private val viewModel: AlarmListViewModel by lazy {
        val activity = requireNotNull(this.activity)
        AlarmListViewModel.Factory(activity.application).create(AlarmListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupBinding(inflater, container)
        setupRecyclerView()
        setupAdapter()
        setupNavigationObserver()
        setupFloatingActionButton()
        showToastMessage()

        return binding.root
    }

    private fun setupBinding(inflater: LayoutInflater, container: ViewGroup?) {
        binding = FragmentAlarmListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.alarmListRecyclerView
        recyclerView.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = AlarmListAdapter(AlarmListAdapter.OnClickListener { alarm ->
                viewModel.navigateToAddEditAlarmFragment(alarm)
            })
        }
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
                        AlarmListFragmentDirections.actionAlarmListFragmentToAddEditAlarmFragment(it)
                    )
            }
        })
    }

    private fun setupFloatingActionButton() {
        val fab = binding.fabCreateAlarm
        fab.setOnClickListener {
            this.findNavController().navigate(
                AlarmListFragmentDirections.actionAlarmListFragmentToAddEditAlarmFragment(null)
            )
        }
    }

    private fun showToastMessage() {
        val toastMessage = args.toastMessage
        if (!toastMessage.isNullOrBlank()) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    }
}

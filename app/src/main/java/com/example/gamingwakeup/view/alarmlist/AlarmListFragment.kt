package com.example.gamingwakeup.view.alarmlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.FragmentAlarmListBinding
import com.example.gamingwakeup.viewmodel.alarmlist.AlarmListViewModel
import com.example.gamingwakeup.viewmodel.alarmlist.AlarmListViewModelFactory

class AlarmListFragment : Fragment() {
    private lateinit var viewModel: AlarmListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAlarmListBinding.inflate(layoutInflater)
        val viewModelFactory = AlarmListViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(AlarmListViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.fragmentAlarmList.adapter = AlarmListAdapter()

        return binding.root
    }
}

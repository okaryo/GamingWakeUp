package com.example.gamingwakeup.view.alarmlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.R
import com.example.gamingwakeup.databinding.FragmentAlarmListBinding
import com.example.gamingwakeup.model.Alarm
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
        setAdapterToRecyclerView(binding.fragmentAlarmList)
        setNavigationObserver(viewModel.navigateToAlarmDetailEdit)

        return binding.root
    }

    private fun setAdapterToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = AlarmListAdapter(AlarmListAdapter.OnClickListener {
            viewModel.navigateToAlarmDetailEdit(it)
        })
    }

    private fun setNavigationObserver(navigation: LiveData<Alarm>) {
        navigation.observe(this, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(AlarmListFragmentDirections.actionAlarmListFragmentToAlarmDetailEditFragment())
            }
        })
    }
}

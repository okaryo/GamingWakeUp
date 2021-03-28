package com.example.gamingwakeup.view

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamingwakeup.model.AlarmList
import com.example.gamingwakeup.view.alarmlist.AlarmListAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: AlarmList) {
    val adapter = recyclerView.adapter as AlarmListAdapter
    adapter.submitList(data.values)
}

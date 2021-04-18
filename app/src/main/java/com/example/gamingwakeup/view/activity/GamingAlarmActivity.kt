package com.example.gamingwakeup.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gamingwakeup.R
import com.example.gamingwakeup.model.model.GamingAlarmNumberInOrder
import com.example.gamingwakeup.view.fragment.AlarmListFragment
import com.example.gamingwakeup.view.fragment.GamingAlarmNumberInOrderFragment
import com.example.gamingwakeup.viewmodel.GamingAlarmNumberInOrderViewModel

class GamingAlarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gaming_alarm)

        openSelectedGameFragment()
    }

    private fun openSelectedGameFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val context = this
        fragmentTransaction.apply {
            this.replace(R.id.activity_gaming_alarm, GamingAlarmNumberInOrderFragment(context))
            this.commit()
        }
    }
}

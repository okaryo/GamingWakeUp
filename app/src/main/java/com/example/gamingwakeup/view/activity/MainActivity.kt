package com.example.gamingwakeup.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import com.example.gamingwakeup.R
import com.example.gamingwakeup.view.fragment.AlarmListFragmentArgs

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavHostFragment()
    }

    private fun setupNavHostFragment() {
        val navController = findNavController(R.id.nav_host_fragment)
        val args = AlarmListFragmentArgs("").toBundle()
        navController.setGraph(R.navigation.main_nav_graph, args)
    }
}

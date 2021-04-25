package com.example.gamingwakeup.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.gamingwakeup.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavHostFragment()
    }

    private fun setupNavHostFragment() {
        val navController = findNavController(R.id.nav_host_fragment)
        val bundle = Bundle().apply { putString("toastMessage", "") }
        navController.setGraph(R.navigation.main_nav_graph, bundle)
    }
}

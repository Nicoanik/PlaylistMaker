package com.example.playlistmaker.root.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
////            val bottomPadding =systemBars.bottom
//            val bottomPadding = if (ime.bottom > 0) {
//                ime.bottom
//            } else {
//                systemBars.bottom
//            }
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding)
//            insets
//        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment,
                R.id.mediaFragment,
                R.id.settingsFragment -> binding.bottomNavigationView.isVisible = true
                else -> binding.bottomNavigationView.isVisible = false
            }
        }
    }

    fun setSoftInputMode(mode: Int) {
        window.setSoftInputMode(mode)
    }
}

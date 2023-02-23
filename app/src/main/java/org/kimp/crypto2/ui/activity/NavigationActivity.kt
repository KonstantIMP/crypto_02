package org.kimp.crypto2.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import org.kimp.crypto2.R
import org.kimp.crypto2.databinding.ActivityNavigationLayoutBinding

@AndroidEntryPoint
class NavigationActivity: AppCompatActivity() {
    private lateinit var binding: ActivityNavigationLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.navigation_host)
        NavigationUI.setupWithNavController(
            binding.navigationView, navController
        )
    }
}

package com.example.marghappclonepriyankaparmar.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.marghappclonepriyankaparmar.R
import com.example.marghappclonepriyankaparmar.databinding.ActivityMainBinding
import com.example.marghappclonepriyankaparmar.ui.fragment.CacheImageFragment
import com.example.marghappclonepriyankaparmar.ui.fragment.HomeFragment
import com.example.marghappclonepriyankaparmar.ui.fragment.ProfileFragment
import com.example.marghappclonepriyankaparmar.ui.fragment.SavedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment())
            .commit()
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, HomeFragment())
                        .commit()
                    true
                }
                R.id.navigation_saved -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, SavedFragment())
                        .commit()
                    true
                }
                R.id.navigation_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ProfileFragment())
                        .commit()
                    true
                }
                R.id.navigation_img -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CacheImageFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

}


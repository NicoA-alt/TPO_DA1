package com.example.tpo_da1.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tpo_da1.R
import com.example.tpo_da1.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNavigation()
    }
    private fun setupBottomNavigation() {
        val bottomNavigation: BottomNavigationView = binding.bottomNavigation

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.navigation_favorites -> {
                    loadFragment(FavoritosFragment())
                    true
                }
                R.id.navigation_profile -> {
                    loadFragment(PerfilFragment())
                    true
                }
                else -> false
            }
        }

        bottomNavigation.selectedItemId = R.id.navigation_home
    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
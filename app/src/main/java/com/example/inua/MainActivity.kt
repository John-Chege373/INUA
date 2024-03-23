package com.example.inua

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.inua.databinding.ActivityMainBinding
import com.example.inua.fragments.DonationsFragment
import com.example.inua.fragments.HomeFragment
import com.example.inua.fragments.MapsFragment
import com.example.inua.fragments.ProfileFragment
import com.google.android.material.navigation.NavigationBarView

@Suppress("UNUSED_EXPRESSION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        setCurrentFragment(homeFragment) // Initial fragment

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.donations -> setCurrentFragment(DonationsFragment())
                R.id.map -> setCurrentFragment(MapsFragment())
                R.id.profile -> setCurrentFragment(ProfileFragment())
                else -> false
            }
            true // Indicate that the selection was handled
        }
    }

    private fun setCurrentFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.flFragment, fragment)
        commit()
    }

    // Public method to control the visibility of the bottom navigation bar
    fun setBottomNavigationVisibility(visible: Boolean) {
        binding.bottomNavigation.visibility = if (visible) View.VISIBLE else View.GONE
    }
}

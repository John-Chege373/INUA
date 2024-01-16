package com.example.inua

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.inua.fragments.DonationsFragment
import com.example.inua.fragments.HomeFragment
import com.example.inua.fragments.MapsFragment
import com.example.inua.fragments.ProfileFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationBarView: NavigationBarView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val donationsFragment = DonationsFragment()
        val mapsFragment = MapsFragment()
        val profileFragment = ProfileFragment()

        bottomNavigationBarView = findViewById(R.id.bottom_navigation)

        bottomNavigationBarView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    setCurrentFragment(homeFragment)
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                }
                R.id.donations -> {
                    setCurrentFragment(donationsFragment)
                    Toast.makeText(this, "Donations", Toast.LENGTH_SHORT).show()
                }
                R.id.map -> {
                    setCurrentFragment(mapsFragment)
                    Toast.makeText(this, "Map", Toast.LENGTH_SHORT).show()
                }
                R.id.profile -> {
                    setCurrentFragment(profileFragment)
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }

    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.flFragment, fragment)
                commit()
            }
    }
}
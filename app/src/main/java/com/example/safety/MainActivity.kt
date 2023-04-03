package com.example.safety

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomBar =  findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomBar.setOnItemSelectedListener {


            when (it.itemId) {
                R.id.btm_guard -> {
                    inflateFragment(GuardFragment.newInstance())
                }
                R.id.btm_home -> {
                    inflateFragment(HomeFragment.newInstance())
                }
                R.id.btm_dashboard -> {
                    inflateFragment(DashboardFragment.newInstance())
                }
                R.id.btm_profile -> {
                    inflateFragment(ProfileFragment.newInstance())
                }
            }
            true
        }

        bottomBar.selectedItemId = R.id.btm_home

    }

    private fun inflateFragment(newInstance : Fragment) {

        val tranc = supportFragmentManager.beginTransaction()
        tranc.replace(R.id.container,newInstance)
        tranc.commit()

    }


}
package com.example.safety

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.safety.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val permissionArray= arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION ,
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
    android.Manifest.permission.READ_CONTACTS,
    android.Manifest.permission.SEND_SMS)
    private val permissionCode = 23

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        askForPerm()
        binding.bottomNav.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.btm_guard -> {
                    inflateFragment(GuardFragment.newInstance())
                }
                R.id.btm_home -> {
                    inflateFragment(HomeFragment.newInstance())
                }
                R.id.btm_dashboard -> {
                    inflateFragment(MapsFragment())
                }
                R.id.btm_profile -> {
                    inflateFragment(ProfileFragment.newInstance())
                }
            }
            true
        }

        binding.bottomNav.selectedItemId = R.id.btm_home

    }

    private fun askForPerm() {
        ActivityCompat.requestPermissions(this,permissionArray,permissionCode)
    }

    private fun inflateFragment(newInstance : Fragment) {

        val tranc = supportFragmentManager.beginTransaction()
        tranc.replace(R.id.container,newInstance)
        tranc.commit()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == permissionCode){
            if(allPermissonGranted()){
                // TODO:
            //openCamera()
            }
        }
    }

    private fun openCamera() {
        val intent = Intent()
            intent.action = ACTION_IMAGE_CAPTURE
        startActivity(intent)
    }

    private fun allPermissonGranted(): Boolean {
        for(items in permissionArray){
            if(ActivityCompat.checkSelfPermission(this,items) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

}
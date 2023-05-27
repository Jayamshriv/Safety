package com.example.safety

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.safety.databinding.ActivityMainBinding
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
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

        if(isAllPermissonGranted()) {
            if(isLocationEnabled(this)){
                setUpLocationListener()
            }else{
                showGPSNotEnabledDialog(this)
            }
        }
        else{
            askForPerm()

        }
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

    private fun setUpLocationListener() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val locationRequest = LocationRequest.create().apply {
            interval = 2000 // Set the interval to 2000 milliseconds (2 seconds)
            fastestInterval = 2000 // Set the fastest interval to 2000 milliseconds (2 seconds)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
//        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
//            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)

                    for (location in locationResult.locations) {
                        Log.d("LocationData","latitude ${location.latitude}")
                        Log.d("LocationData","Longitude ${location.longitude}")

                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val mail = currentUser?.email.toString()

                        val db = Firebase.firestore

                        val locationData = hashMapOf<String,Any>(
                            "lat" to location.latitude.toString(),
                            "long" to  location.longitude.toString()
                        )

                        FirebaseFirestore.setLoggingEnabled(true)

                        db.collection("User Data")
                            .document(mail)
                            .update(locationData)
                            .addOnSuccessListener {
//                                Log.d("location",it)
                                Toast.makeText(
                                    baseContext,
                                    "loaction added",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                            .addOnFailureListener {
                                FirebaseFirestore.setLoggingEnabled(true)
                                Log.d("TAG", "dataStoring: Failure")
                                Toast.makeText(
                                    baseContext,
                                    "loaction added failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                    }
                    // Few more things we can do here:
                    // For example: Update the location of user on server
                }
            },
            Looper.myLooper()
        )
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
            if(isAllPermissonGranted()){
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

    private fun isAllPermissonGranted(): Boolean {
        for(items in permissionArray){
            if(ActivityCompat.checkSelfPermission(
                    this,
                    items
                ) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    fun showGPSNotEnabledDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.enable_gps))
            .setMessage(context.getString(R.string.required_for_this_app))
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.enable_now)) { _, _ ->
                context.startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }

}
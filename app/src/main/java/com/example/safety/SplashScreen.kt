package com.example.safety

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class SplashScreen : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        SharedPrefFile.init(this)
        val isLoggedIn = SharedPrefFile.getLoggedInfo("isUserLoggedIn")

        if(isLoggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this, LoginUser::class.java))
            finish()
        }
    }
}
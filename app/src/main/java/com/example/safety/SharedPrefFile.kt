package com.example.safety

import android.content.Context
import android.content.SharedPreferences

object SharedPrefFile {

    private lateinit var preferences: SharedPreferences
    private const val NAME = "SafetySharedPref"

    fun init(context : Context){
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun putLoggedInfo(key: String, value: Boolean){
        preferences.edit().putBoolean(key,value).commit()
    }

    fun getLoggedInfo(key : String): Boolean{
        return preferences.getBoolean(key,false)
    }
}
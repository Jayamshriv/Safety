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
        preferences.edit().putBoolean(key,value).apply()
    }

    fun getLoggedInfo(key : String): Boolean{
        return preferences.getBoolean(key,false)
    }

    fun putPhoneNum(key: String,value: String){
        preferences.edit().putString(key, value).apply()
    }

    fun getPhoneNum(key: String): String? {
        return preferences.getString(key,"0")
    }

}
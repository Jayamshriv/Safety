package com.example.safety

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ContactModel::class],version = 1, exportSchema = false)
public abstract class SafetyDataBase: RoomDatabase() {

    abstract fun contactDao():ContactDao

    companion object{

        @Volatile
        private var INSTANCE : SafetyDataBase? = null

        fun getDataBase(context: Context): SafetyDataBase {

            INSTANCE?.let {
                return  it
            }
            return synchronized(SafetyDataBase::class.java) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SafetyDataBase::class.java,
                    "safety_dataBase"
                ).build()

                INSTANCE = instance

            instance
            }
        }

    }
}
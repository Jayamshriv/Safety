package com.example.safety

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {

    @Query("SELECT * FROM contactModel")
    fun getAllData() : LiveData<List<ContactModel>>

    @Insert
    suspend fun insert(contactInfo: ContactModel)

    @Insert
    suspend fun insertAll(contactInfo: List<ContactModel>)

}
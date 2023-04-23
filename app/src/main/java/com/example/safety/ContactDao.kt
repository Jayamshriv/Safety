package com.example.safety

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {

    @Query("SELECT * FROM ContactModel")
    fun getAllData() : LiveData<List<ContactModel>>

    @Query("SELECT * FROM ContactModel where conName = :name")
    fun getNumber(name : String): ContactModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contactInfo: ContactModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contactInfo: List<ContactModel>)

}

package com.example.safety

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class ContactModel(
    val conName: String,

    @PrimaryKey
    val number: String
)
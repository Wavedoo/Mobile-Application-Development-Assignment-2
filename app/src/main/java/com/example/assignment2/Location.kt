package com.example.assignment2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "address")
    val address: String?,
    @ColumnInfo(name = "latitude")
    val latitude: Double?,
    @ColumnInfo(name = "longitude")
    val longitude: Double?
)
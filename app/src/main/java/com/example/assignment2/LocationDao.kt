package com.example.assignment2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("SELECT * FROM location")
    fun getAll(): List<Location>

    @Query("SELECT * FROM location WHERE address LIKE :address LIMIT 1")
    fun findByAddress(address: String): Location

    @Insert
    fun insert(location: Location)


    @Delete
    fun delete(location: Location)
}
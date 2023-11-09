package com.example.assignment2

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var db: LocationRoomDatabase
    private lateinit var locationDao: LocationDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = Room.databaseBuilder(
                this,
                LocationRoomDatabase::class.java, "location-database"
            ).build()
        locationDao = db.locationDao()
        populateDatabase()
    }

    private fun populateDatabase(){
        val reader = BufferedReader(
            InputStreamReader(assets.open("Locations.txt"))
        )

        var line: String? = reader.readLine();
        var id = 1
        while(line != null){
            val arr = line.split(", ")
            val lat = arr[0].toDouble()
            val long = arr[1].toDouble()
            val addressObj = coordinatesToAddress(lat, long)
            var address: String
            if(addressObj != null){
                address = "${addressObj.countryName}, ${addressObj.adminArea}, ${addressObj.locality}, ${addressObj.getAddressLine(0)}"
            }else{
                continue
            }
            val location = Location(id = id++, address = address, latitude = lat, longitude = long)
            println("Location is: $location")
            locationDao.insert(location)

            line = reader.readLine()
        }
    }

    private fun coordinatesToAddress(lat: Double, long:Double): Address?{
        val geocoder: Geocoder = Geocoder(this, Locale.CANADA)
        val addressList = geocoder.getFromLocation(lat, long, 1)
        return addressList?.get(0) ?: null
    }

    fun searchForLocation(v: View){
        val searchBar = findViewById<EditText>(R.id.searchBar)
        val search = searchBar.text.toString()

        if(search == ""){
            Toast.makeText(
                this,
                "Enter something to search it",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val location = locationDao.findByAddress(search)
        displayResults(location)
    }

    private fun displayResults(location: Location){
        val textAddress = findViewById<TextView>(R.id.textAddress)
        val textLatitude = findViewById<TextView>(R.id.textLatitude)
        val textLongitude = findViewById<TextView>(R.id.textLongitude)

        textAddress.text = location.address
        textLatitude.text = "Latitude ${location.latitude}"
        textLongitude.text = "Latitude ${location.longitude}"

    }
}
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
    //Using room for database as recommended by Android Developers official website
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

    /*
    A function for populating the database with addresses that are found from
    looking at the coordinates in the Locations.txt file
     */
    private fun populateDatabase(){
        val reader = BufferedReader(
            InputStreamReader(assets.open("Locations.txt"))
        )

        var line: String? = reader.readLine();
        var id = 1

        //Goes over every line in Locations.txt
        while(line != null){
            //Split each line into latitude and longitude
            val arr = line.split(", ")
            val lat = arr[0].toDouble()
            val long = arr[1].toDouble()

            //Get the address from the coordinates to address function
            val addressObj = coordinatesToAddress(lat, long)
            println(addressObj)

            //Format address to be saved in the database
            var address: String
            if(addressObj != null){
                address = "${addressObj.countryName}, ${addressObj.adminArea}, ${addressObj.locality}, ${addressObj.getAddressLine(0)}"
            }else{
                continue
            }

            val location = Location(id = id++, address = address, latitude = lat, longitude = long)
//            println("Location is: $location")
//            locationDao.insert(location) //Currently causes errors

            line = reader.readLine()
        }
    }

    /*Takes in a latitude and longitude value and returns a single Address object
    that comes from the geoCoder list
     */
    private fun coordinatesToAddress(lat: Double, long:Double): Address?{
        val geocoder: Geocoder = Geocoder(this, Locale.CANADA)
        val addressList = geocoder.getFromLocation(lat, long, 1)
        return addressList?.get(0) ?: null
    }

    /*
    Function for the search button
    Searches for an address within the database
     */
    fun searchForLocation(v: View){
        val searchBar = findViewById<EditText>(R.id.searchBar)
        val search = searchBar.text.toString()

        //Shows a toast message if the user did not enter an address to search
        if(search == ""){
            Toast.makeText(
                this,
                getString(R.string.empty_search_error),
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val location = locationDao.findByAddress(search)
        displayResults(location)
    }

    /*
    Displays the results of the search in the activity by
    which includes the address, latitude, and longitude saved
    */
    private fun displayResults(location: Location){
        val textAddress = findViewById<TextView>(R.id.textAddress)
        val textLatitude = findViewById<TextView>(R.id.textLatitude)
        val textLongitude = findViewById<TextView>(R.id.textLongitude)

        textAddress.text = location.address
        textLatitude.text = "Latitude ${location.latitude}"
        textLongitude.text = "Latitude ${location.longitude}"

    }
}
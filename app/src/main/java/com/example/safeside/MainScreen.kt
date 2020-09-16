package com.example.safeside.com.example.safeside

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.safeside.Fragments.*
import com.example.safeside.R
import kotlinx.android.synthetic.main.activity_main_screen.*
import java.util.*


class MainScreen : AppCompatActivity() {


    var gps_loc: Location? = null
    var network_loc: Location? = null
    var final_loc: Location? = null
    var longitude = 0.0
    var latitude = 0.0

    private val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    var homeFragment : HomeFragment = HomeFragment()
    var addFragment : AddFragment = AddFragment()
    var profileFragment : ProfileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        val locationManager = getSystemService( LOCATION_SERVICE) as LocationManager

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this, permission,
                100
            )
        }


        try {
            gps_loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            network_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception){
            Log.d("Location status", e.localizedMessage)
        }

        getLocation()

        chipNavigationBar.setItemSelected(R.id.home,true)
        setFragment(homeFragment)

        chipNavigationBar.setOnItemSelectedListener{
            when(it){
                R.id.home -> setFragment(homeFragment)

                R.id.add -> setFragment(addFragment)

                R.id.profile -> setFragment(profileFragment)
            }

        }

    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainView, fragment)
        fragmentTransaction.commit()
    }

    private fun getLocation(){

        if(gps_loc != null) {
            final_loc = gps_loc
            latitude = final_loc!!.latitude
            longitude = final_loc!!.longitude
        } else if (network_loc != null) {
            final_loc = network_loc
            latitude = final_loc!!.latitude
            longitude = final_loc!!.longitude
        } else {
            latitude = 0.0
            longitude = 0.0
        }

        try {
            val geoCoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address>? = geoCoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val zone : String = addresses[0].subLocality
                val state: String = addresses[0].adminArea
                val country: String = addresses[0].countryName
                Log.d("LOCATION", "$zone, $state, $country")
                val MY_PREFS_NAME = "SharedPrefFile"
                val editor = getSharedPreferences(MY_PREFS_NAME, AppCompatActivity.MODE_PRIVATE).edit()
                if (state !== "null" && country !== "null") {
                    editor.putString("location", "$zone, $state")
                    editor.putString("lat", latitude.toString())
                    editor.putString("long", longitude.toString())
                }
                editor.apply()
                editor.commit()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }
}

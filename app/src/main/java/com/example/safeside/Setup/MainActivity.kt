package com.example.safeside.Setup

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.safeside.R
import com.example.safeside.com.example.safeside.MainScreen
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            val sharedPreferences: SharedPreferences = this.getSharedPreferences(
                "sharedPrefFile",
                Context.MODE_PRIVATE
            )
            val email: String? = sharedPreferences.getString("email", "default")

            if (email == "default") {
                val i = Intent(this, SignInActivity::class.java)

                startActivity(i)
                finish()
            } else {
                val i = Intent(this, MainScreen::class.java)

                startActivity(i)
                finish()
            }

        }, 1500)
    }

//
//    override fun onBackPressed() {
//        val alertDialogBuilder: AlertDialog.Builder = Builder(this)
////        alertDialogBuilder.setTitle("ⓘ Exit ! " + getString(R.string.app_name))
////        alertDialogBuilder
////            .setMessage(Html.fromHtml("<p style='text-align:center;'>Please Fill the required details</p><h3 style='text-align:center;'>Click Yes to Exit !</h4>"))
////            .setCancelable(false)
////            .setPositiveButton("Yes",
////                DialogInterface.OnClickListener { dialog, id ->
////                    moveTaskToBack(true)
////                    Process.killProcess(Process.myPid())
////                    System.exit(0)
////                })
////            .setNegativeButton("No",
////                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
////        val alertDialog: AlertDialog = alertDialogBuilder.create()
////        alertDialog.show()
//    }
}
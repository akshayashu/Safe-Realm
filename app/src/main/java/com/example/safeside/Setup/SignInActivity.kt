package com.example.safeside.Setup

import android.Manifest
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Pair
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.safeside.Backend.AuthenticationInterface
import com.example.safeside.R
import com.example.safeside.com.example.safeside.MainScreen
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.regex.Matcher
import java.util.regex.Pattern

class SignInActivity : AppCompatActivity() {

    private val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signInBtn.setOnClickListener {
            if(TextUtils.isEmpty(signInEmail.text.toString().trim())){
                signInEmail.error = "Field is empty"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(signInPassword.text.toString().trim())){
                Toast.makeText(applicationContext, "Field is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this, permission,
                    100
                )
            } else {
                loginUser(
                    signInEmail.text.toString().trim(),
                    signInPassword.text.toString().trim(),
                    this
                )
            }
        }

        signUpBtn.setOnClickListener {
            if(TextUtils.isEmpty(signUpUsername.text.toString().trim())){
                signUpUsername.error = "Field is empty"
                return@setOnClickListener
            }
            val pattern1: Pattern = Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+")
            val matcher1: Matcher = pattern1.matcher(signUpEmail.text.toString().trim())

            if (!matcher1.matches()) {
                signUpEmail.error = "Error in Email format!"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(signUpEmail.text.toString().trim())){
                signUpEmail.error = "Field is empty"
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(signUpPassword.text.toString().trim())){
                Toast.makeText(applicationContext, "Field is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(signUpPassword.text.toString().trim().length < 6){
                Toast.makeText(applicationContext, "Enter atleast 6 characters!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(
                    this, permission,
                    200
                )
            } else {
                registerUser(
                    signUpUsername.text.toString().trim(),
                    signUpEmail.text.toString().trim(),
                    signUpPassword.text.toString().trim(),
                    this
                )
            }
        }

    }

    private fun registerUser(userName: String, email: String, password: String, act: Activity) {

        progressBar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://sheltered-beyond-13726.herokuapp.com/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .setLenient()
                .create()))
            .build()

        val myInterface = retrofit.create(AuthenticationInterface::class.java)

        myInterface.registration(userName, email, password).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.body().toString() == "Registered Succesfully!") {
                    progressBar.visibility = View.VISIBLE
                    Toast.makeText(applicationContext, response.body().toString(), Toast.LENGTH_SHORT).show()

                    val sharedPreferences: SharedPreferences = act.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
                    val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                    editor.putString("userName",userName)
                    editor.putString("email",email)
                    editor.putString("password", password)
                    editor.apply()
                    editor.commit()
                    val i = Intent(applicationContext, InfoFirst::class.java)

                    val p1 = Pair.create<View, String>(imageView, "bk_image")
                    val p2 = Pair.create<View, String>(signUp, "ll")
                    val option = ActivityOptions.makeSceneTransitionAnimation(act, p1, p2)

                    startActivity(i, option.toBundle())
                    finish()
                } else {
                    progressBar.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, response.body().toString(), Toast.LENGTH_SHORT).show()
                }

            }

        })
    }

    private fun loginUser(email: String, password: String, act: Activity) {

        progressBar.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://sheltered-beyond-13726.herokuapp.com/api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder()
                .setLenient()
                .create()))
            .build()

        val myInterface = retrofit.create(AuthenticationInterface::class.java)

        myInterface.login(email, password).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.body().toString() == "Logged in successfully"){
                    val sharedPreferences: SharedPreferences = act.getSharedPreferences("sharedPrefFile", Context.MODE_PRIVATE)
                    val editor:SharedPreferences.Editor =  sharedPreferences.edit()
                    editor.putString("userName","")
                    editor.putString("email",email)
                    editor.putString("password", password)
                    editor.apply()
                    editor.commit()
                    Toast.makeText(applicationContext, response.body().toString(), Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, MainScreen::class.java))
                    finish()
                }else {
                    Toast.makeText(applicationContext, response.body().toString(), Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
                loginUser(
                    signInEmail.text.toString().trim(),
                    signInPassword.text.toString().trim(),
                    this
                )
            }else {
                loginUser(
                    signInEmail.text.toString().trim(),
                    signInPassword.text.toString().trim(),
                    this
                )
            }
        }
        else if (requestCode == 200) {
            if ( grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
                registerUser(
                    signUpUsername.text.toString().trim(),
                    signUpEmail.text.toString().trim(),
                    signUpPassword.text.toString().trim(),
                    this
                )
            } else{
                registerUser(
                    signUpUsername.text.toString().trim(),
                    signUpEmail.text.toString().trim(),
                    signUpPassword.text.toString().trim(),
                    this
                )
            }
        }
        else {
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }
}
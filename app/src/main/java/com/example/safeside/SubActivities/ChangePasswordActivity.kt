package com.example.safeside.SubActivities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.safeside.Backend.AuthenticationInterface
import com.example.safeside.R
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.CubeGrid
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var pro : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        pro = spin_kit
        val cubeGrid: Sprite = CubeGrid()
        pro.indeterminateDrawable = cubeGrid


        changeBtn.setOnClickListener {
            if(TextUtils.isEmpty(oldPassword.text.toString().trim())){
                Toast.makeText(applicationContext, "Field is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(newPassword.text.toString().trim())){
                Toast.makeText(applicationContext, "Field is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(newPassword.text.toString().trim().length < 6){
            Toast.makeText(applicationContext, "Enter at least 6 characters!", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
            }
            if(TextUtils.isEmpty(confirmPassword.text.toString().trim())){
                Toast.makeText(applicationContext, "Field is empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(confirmPassword.text.toString().trim() != newPassword.text.toString().trim()){
                Toast.makeText(applicationContext, "Password doesn't match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreferences: SharedPreferences = getSharedPreferences(
                "sharedPrefFile",
                Context.MODE_PRIVATE
            )
            val email: String? = sharedPreferences.getString("email", "default")

            if (email != null) {
                update(
                    email,
                    oldPassword.text.toString().trim(),
                    newPassword.text.toString().trim()
                )
            }
        }
    }

    private fun update(email: String, password: String, nPassword: String){

        pro.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://sheltered-beyond-13726.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        val myInterface = retrofit.create(AuthenticationInterface::class.java)

        myInterface.update(email, password, nPassword).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.body().toString() == "OK") {
                    oldPassword.setText("")
                    confirmPassword.setText("")
                    newPassword.setText("")
                    pro.visibility = View.INVISIBLE
                    Log.d("UPDATE", response.body().toString())
                    Toast.makeText(applicationContext, "Password updated!", Toast.LENGTH_SHORT)
                        .show()
                } else if(response.body().toString() == "Internal Server Error"){
                    Log.d("UPDATE", response.body().toString())
                    pro.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Something went wrong 😟!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    pro.visibility = View.INVISIBLE
                    Toast.makeText(applicationContext, "Password doesn't match!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                pro.visibility = View.INVISIBLE
                t.localizedMessage?.let { Log.d("UPDATE", it) }
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })

    }
}
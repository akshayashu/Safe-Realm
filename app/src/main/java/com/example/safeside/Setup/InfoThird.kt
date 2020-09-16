package com.example.safeside.Setup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safeside.R
import com.example.safeside.com.example.safeside.MainScreen
import kotlinx.android.synthetic.main.activity_info_third.*

class InfoThird : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_third)

        nextBtn1.setOnClickListener {
            startActivity(Intent(applicationContext, MainScreen::class.java))
            finish()
        }
    }

}

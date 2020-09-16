package com.example.safeside.Setup

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Pair
import android.view.View
import com.example.safeside.R
import com.example.safeside.com.example.safeside.MainScreen
import kotlinx.android.synthetic.main.activity_info_first.*

class InfoFirst : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_first)

        nextBtn.setOnClickListener {
            val i = Intent(applicationContext,InfoSecond::class.java)

            val p2 = Pair.create<View, String>(linearLayout3,"ll")
            val p3 = Pair.create<View, String>(infoBody,"infoBody")
            val p4 = Pair.create<View, String>(infoTitle,"infoTitle")
            val p5 = Pair.create<View, String>(dots,"dots")
            val p6 = Pair.create<View, String>(ll1,"ll1")
            val option = ActivityOptions.makeSceneTransitionAnimation(this, p2, p3,p4,p5,p6)

            startActivity(i,option.toBundle())
        }

        skipBtn.setOnClickListener {
            startActivity(Intent(applicationContext, MainScreen::class.java))
            finish()
        }
    }
}

package com.example.pr2v6.com.example.pr2v6

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.pr2v6.MainActivity
import com.example.pr2v6.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DURATION: Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // on below line we are configuring
        // our window to full screen
        setContentView(R.layout.activity_splash)

        // on below line we are calling
        // handler to run a task
        // for specific time interval
        Handler().postDelayed({
            // on below line we are
            // creating a new intent
            val i = Intent(
                this@SplashActivity,
                MainActivity::class.java
            )
            // on below line we are
            // starting a new activity.
            startActivity(i)

            // on the below line we are finishing
            // our current activity.
            finish()
        }, SPLASH_DURATION)
    }
}
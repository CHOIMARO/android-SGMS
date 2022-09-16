package com.tngen.sgms_android.presentation.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.tngen.sgms_android.R
import com.tngen.sgms_android.presentation.main.MainActivity


class BaseSplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_splash)

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)

    }
    override fun onPause() {
        super.onPause()
        finish()
    }
}
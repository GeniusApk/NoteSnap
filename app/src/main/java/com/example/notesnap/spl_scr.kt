package com.example.notesnap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class spl_scr : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spl_scr)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this , login::class.java)
            startActivity(intent)
            finish()
        },3000)


    }
}
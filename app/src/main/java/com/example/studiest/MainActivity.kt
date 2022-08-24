package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Studiest)
        setContentView(R.layout.activity_main)

        var handler = Handler()
        handler.postDelayed(
        {
            val intent = Intent(this@MainActivity, bemvindo::class.java)
            startActivity(intent)
            finish()
        },900)
    }
}
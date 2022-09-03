package com.example.studiest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Studiest)
        setContentView(R.layout.activity_main)

        val sharedPreference = getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
        var email = sharedPreference.getString("email", "Null") as String

        if (email != "Null") {
            var handler = Handler()
            handler.postDelayed(
                {
            val intent = Intent(applicationContext, tela_principal::class.java)
            finish()
            startActivity(intent)
                },900)
        } else{
            var handler = Handler()
            handler.postDelayed(
                {
                    val intent = Intent(this@MainActivity, bemvindo::class.java)
                    startActivity(intent)
                    finish()
                },900)
        }

    }
}
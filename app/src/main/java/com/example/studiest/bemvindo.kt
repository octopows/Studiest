package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class bemvindo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bemvindo)

        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val btnCadastrar = findViewById<Button>(R.id.btnCadastrar)


        btnEntrar.setOnClickListener{
            val intent = Intent(this, entrar::class.java)
            startActivity(intent)

        }

        btnCadastrar.setOnClickListener {
            val intent = Intent(this, cadastrar::class.java)
            startActivity(intent)

        }

    }
}
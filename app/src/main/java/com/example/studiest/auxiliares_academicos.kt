package com.example.studiest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class auxiliares_academicos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auxiliares_academicos)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

    }
}
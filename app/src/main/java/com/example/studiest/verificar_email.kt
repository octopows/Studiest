package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class verificar_email : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verificar_email)

        val btnVoltarEspecial = findViewById<ImageView>(R.id.btnVoltarEspecial)
        var emailInserido = findViewById<TextView>(R.id.emailInserido)

        val intent: Intent = this.intent
        var email = intent.getStringExtra("email")

        emailInserido.text = "$email"
        //botão para voltar
        btnVoltarEspecial.setOnClickListener{
            finish()
        }


    }
}
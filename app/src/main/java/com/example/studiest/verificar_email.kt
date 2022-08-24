package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class verificar_email : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verificar_email)

        val btnVoltarEspecial = findViewById<ImageView>(R.id.btnVoltarEspecial)

        //botão para voltar
        btnVoltarEspecial.setOnClickListener{
            finish()
            val intent = Intent(this, recuperar_senha::class.java)
            startActivity(intent)
        }


    }
}
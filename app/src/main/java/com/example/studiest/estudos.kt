package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class estudos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudos)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val retangulo_anexo = findViewById<View>(R.id.retangulo_anexo)
        val retangulo_resumos = findViewById<View>(R.id.retangulo_resumos)
        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

        //abrir anexador de documentos
        retangulo_anexo.setOnClickListener{
            val intent = Intent(this, anexo_documentos::class.java)
            startActivity(intent)
        }

        //abrir resumos
        retangulo_resumos.setOnClickListener{
            val intent = Intent(this, resumos::class.java)
            startActivity(intent)
        }

    }
}
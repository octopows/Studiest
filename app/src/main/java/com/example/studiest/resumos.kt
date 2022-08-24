package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class resumos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumos)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnAddResumo = findViewById<ImageView>(R.id.btnAddResumo)

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }
        //btn Adicionar Resumo
        btnAddResumo.setOnClickListener{
            val intent = Intent(this, adicionar_resumo::class.java)
            startActivity(intent)
        }

    }
}
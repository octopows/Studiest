package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class auxiliares_academicos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auxiliares_academicos)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val tabela = findViewById<View>(R.id.btnTabela)
        val btnFisica = findViewById<View>(R.id.btnFisica)
        val btnQuimica = findViewById<View>(R.id.btnQuimica)
        val btnMat = findViewById<View>(R.id.btnMat)



        tabela.setOnClickListener {
            val intent = Intent(this, pdfReader::class.java)
            intent.putExtra("pdfName","tabela-periodica.pdf")
            startActivity(intent)
        }

        btnFisica.setOnClickListener {
            val intent = Intent(this, pdfReader::class.java)
            intent.putExtra("pdfName","formulas_de_fisica.pdf")
            startActivity(intent)
        }

        btnMat.setOnClickListener {
            val intent = Intent(this, pdfReader::class.java)
            intent.putExtra("pdfName","formulas_de_matematica.pdf")
            startActivity(intent)
        }

        btnQuimica.setOnClickListener {
            val intent = Intent(this, pdfReader::class.java)
            intent.putExtra("pdfName","formulas_de_quimica.pdf")
            startActivity(intent)
        }



        //botão para voltar
        btnVoltar.setOnClickListener {
            finish()
        }
    }
}
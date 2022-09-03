package com.example.studiest

import android.os.Bundle
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class calendario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        var para = findViewById<TextView>(R.id.Para)
        var faltam = findViewById<TextView>(R.id.Para)
        val calendario = findViewById<CalendarView>(R.id.calendar_view)
        val sdf = SimpleDateFormat("dd/MM/yyyy")

        calendario.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            var dataSelecionada = calendario.date
            para.text = "PARA: $dataSelecionada"
        }



        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

    }
}
package com.example.studiest

import android.text.format.DateFormat
import android.os.Bundle
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.lang.String.format
import java.text.MessageFormat.format
import java.util.*

class calendario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendario)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        var para = findViewById<TextView>(R.id.Para)
        var faltam = findViewById<TextView>(R.id.Faltam)
        val calendario = findViewById<CalendarView>(R.id.calendar_view)



        calendario.setOnDateChangeListener { calendarView, ano, mes, dia ->
            val data_atual = Date()
            val data_selecionada = GregorianCalendar()
            data_selecionada.set(ano, mes, dia)
            calendarView.date = data_selecionada.time.time
            val diff =  (data_selecionada.time.time-data_atual.time)/(24*60*60*1000).toLong()
            var texto = DateFormat.format("dd/MM/yyyy",data_selecionada).toString()
            para.text = "Para: $texto"
            if(diff>0 && diff!=1L){
                faltam.text = "Faltam: $diff dias"
            }
            else if(diff == 1L) {
                faltam.text = "Falta: 1 dia"
            } else if(diff == 0L)
                faltam.text = "Hoje"
            else
                faltam.text = "Esse dia já passou "
        }

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

    }
}
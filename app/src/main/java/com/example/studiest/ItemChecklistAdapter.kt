package com.example.studiest

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class ItemChecklistAdapter(contexto: Context) : ArrayAdapter<ItemChecklist>(contexto, 0){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val v: View
        if(convertView != null) {
            v = convertView
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.checklist_item, parent, false)
        }

        val item = getItem(position)

        val tituloItem = v.findViewById<TextView>(R.id.tituloItem)
        val disciplinaItem = v.findViewById<TextView>(R.id.disciplinaItem)
        val prazoitem = v.findViewById<TextView>(R.id.prazoItem)
        val fundoItem = v.findViewById<View>(R.id.fundoItem)

        val data = SimpleDateFormat("dd/MM/yyyy").parse(item?.prazo)
        val data_atual = Date()
        val diff =  (data.time - data_atual.time)/(24*60*60*1000).toLong()
        if(diff >= 2){
            fundoItem.setBackgroundResource(R.drawable.retangulo_item_azul)
        } else if(diff>=1){
            fundoItem.setBackgroundResource(R.drawable.retangulo_item_amarelo)
        } else if(diff<1){
            fundoItem.setBackgroundResource(R.drawable.retangulo_item_vermelho)
        }


        tituloItem.text = item?.titulo
        disciplinaItem.text = item?.disciplina
        prazoitem.text = item?.prazo

        return v
    }
}
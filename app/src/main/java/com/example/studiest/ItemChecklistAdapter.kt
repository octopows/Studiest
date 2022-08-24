package com.example.studiest

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

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

        tituloItem.text = item?.titulo
        disciplinaItem.text = item?.disciplina
        prazoitem.text = item?.prazo

        return v
    }
}
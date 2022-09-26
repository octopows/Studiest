package com.example.studiest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ResumoAdapter(contexto: Context): ArrayAdapter<Resumo>(contexto,0) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val v: View
        if(convertView != null) {
            v = convertView
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.resumo_view, parent, false)
        }

        val item = getItem(position)

        val tituloResumo = v.findViewById<TextView>(R.id.tituloResumo)
        val disciplinaResumo = v.findViewById<TextView>(R.id.disciplinaResumo)
        val conteudoResumo = v.findViewById<TextView>(R.id.conteudoResumo)

        tituloResumo.text = item?.titulo.toString()
        disciplinaResumo.text = item?.disciplina.toString()
        conteudoResumo.text = item?.conteudo.toString()

        return v
    }


}
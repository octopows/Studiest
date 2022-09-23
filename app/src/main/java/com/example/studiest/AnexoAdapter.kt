package com.example.studiest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class AnexoAdapter(contexto: Context): ArrayAdapter<Anexo>(contexto,0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val v: View
        if(convertView != null) {
            v = convertView
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.anexo_view, parent, false)
        }

        val item = getItem(position)

        val tituloAnexo = v.findViewById<TextView>(R.id.tituloAnexo)
        val campoSelecionarArquivo = v.findViewById<TextView>(R.id.campoSelecionarArquivo)
        tituloAnexo.text = item?.titulo.toString()

        return v
    }
}
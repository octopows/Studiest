package com.example.studiest

import java.text.SimpleDateFormat

object AvaliacaoController {

    private val AVALIACOES: MutableList<ItemChecklist> = arrayListOf()

    fun sortPrazo() = AVALIACOES.sortBy{
        val data = SimpleDateFormat("dd/MM/yyyy").parse(it.prazo)
        data
    }

    fun sortCriacao() = AVALIACOES.sortBy{it.id}

    fun cadastra(itemChecklist: ItemChecklist){
        AVALIACOES.add(itemChecklist)
    }

    fun listaDeAvaliacoes() = AVALIACOES

    fun getAvaliacao(i: Int) = AVALIACOES.get(i)

    fun atualiza(i: Int, itemChecklist: ItemChecklist){
        AVALIACOES.set(i, itemChecklist)
    }

    fun apaga(i: Int){
        AVALIACOES.removeAt(i)
    }
}
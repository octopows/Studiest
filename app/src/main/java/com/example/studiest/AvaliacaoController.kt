package com.example.studiest

object AvaliacaoController {

    private val AVALIACOES: MutableList<ItemChecklist> = arrayListOf()

    fun sortPrazo() = AVALIACOES.sortBy{it.prazo}

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
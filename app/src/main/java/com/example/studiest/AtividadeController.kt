package com.example.studiest

import java.text.SimpleDateFormat

object AtividadeController {

    private val ATIVIDADES: MutableList<ItemChecklist> = arrayListOf()

    fun sortPrazo() = ATIVIDADES.sortBy{
        val data = SimpleDateFormat("dd/MM/yyyy").parse(it.prazo)
        data}

    fun sortCriacao() = ATIVIDADES.sortBy{it.id}

    fun cadastra(itemChecklist: ItemChecklist){
        ATIVIDADES.add(itemChecklist)
    }

    fun listaDeAtividades() = ATIVIDADES

    fun getAtividade(i: Int) = ATIVIDADES.get(i)

    fun atualiza(i: Int, itemChecklist: ItemChecklist){
        ATIVIDADES.set(i, itemChecklist)
    }

    fun apaga(i: Int){
        ATIVIDADES.removeAt(i)
    }
}

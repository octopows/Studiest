package com.example.studiest

import java.text.SimpleDateFormat

object LembreteController {

    private val LEMBRETES: MutableList<ItemChecklist> = arrayListOf()

    fun sortPrazo() = LEMBRETES.sortBy{val data = SimpleDateFormat("dd/MM/yyyy").parse(it.prazo)
        data}

    fun sortCriacao() = LEMBRETES.sortBy{it.id}

    fun cadastra(itemChecklist: ItemChecklist){
        LEMBRETES.add(itemChecklist)
    }

    fun listaDeLembretes() = LEMBRETES

    fun getLembrete(i: Int) = LEMBRETES.get(i)

    fun atualiza(i: Int, itemChecklist: ItemChecklist){
        LEMBRETES.set(i, itemChecklist)
    }

    fun apaga(i: Int){
        LEMBRETES.removeAt(i)
    }
}
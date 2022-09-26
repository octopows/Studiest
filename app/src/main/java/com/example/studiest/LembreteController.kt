package com.example.studiest

object LembreteController {

    private val LEMBRETES: MutableList<ItemChecklist> = arrayListOf()

    fun sortPrazo() = LEMBRETES.sortBy{it.prazo}

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
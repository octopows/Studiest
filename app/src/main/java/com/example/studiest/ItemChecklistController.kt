package com.example.studiest

object ItemChecklistController {

    private val ITENSCHECKLIST: MutableList<ItemChecklist> = arrayListOf()

    fun cadastra(itemChecklist: ItemChecklist){
        ITENSCHECKLIST.add(itemChecklist)
    }

    fun listaDeItensChecklist() = ITENSCHECKLIST

    fun getItemChecklist(i: Int) = ITENSCHECKLIST.get(i)

    fun atualiza(i: Int, itemChecklist: ItemChecklist){
        ITENSCHECKLIST.set(i, itemChecklist)
    }

    fun apaga(i: Int){
        ITENSCHECKLIST.removeAt(i)
    }
}
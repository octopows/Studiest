package com.example.studiest

object AtividadeController {

    private val ATIVIDADES: MutableList<ItemChecklist> = arrayListOf()

    fun sortPrazo() = ATIVIDADES.sortBy{it.prazo}

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

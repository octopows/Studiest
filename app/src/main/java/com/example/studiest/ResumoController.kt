package com.example.studiest

object ResumoController {

    private val RESUMOS: MutableList<Resumo> = arrayListOf()

    fun cadastra(resumo: Resumo){
            RESUMOS.add(resumo)
    }

    fun listaDeResumos() = RESUMOS

    fun getResumo(i: Int) = RESUMOS.get(i)

    fun atualiza(i: Int, resumo: Resumo){
        RESUMOS.set(i, resumo)
    }

    fun apaga(i: Int){
        RESUMOS.removeAt(i)
    }


}
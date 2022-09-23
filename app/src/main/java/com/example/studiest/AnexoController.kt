package com.example.studiest

object AnexoController {

    private val ANEXOS: MutableList<Anexo> = arrayListOf()

    fun cadastra(anexo: Anexo){
        ANEXOS.add(anexo)
    }

    fun listaDeAnexos() = ANEXOS

    fun getAnexo(i: Int) = ANEXOS.get(i)

    fun atualiza(i: Int, anexo: Anexo){
        ANEXOS.set(i, anexo)
    }

    fun apaga(i: Int){
        ANEXOS.removeAt(i)
    }
}
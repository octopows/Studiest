package com.example.studiest

data class ItemChecklist (
    var id: Int,
    var titulo: String?,
    var disciplina:String?,
    var prazo:String?,
    var descricao:String? = null,
    var tipo: Int
    )

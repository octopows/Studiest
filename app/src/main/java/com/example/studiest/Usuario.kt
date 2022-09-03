package com.example.studiest

data class Usuario(
    var id: Int,
    var nome: String?,
    var email: String?,
    var senha: String?
) {
    constructor(id: Int, nome: String?, email: String) : this(id,nome, email, null)
}
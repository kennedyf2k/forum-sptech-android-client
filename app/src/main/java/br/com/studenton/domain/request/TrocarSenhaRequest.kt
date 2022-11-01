package br.com.studenton.domain.request

data class TrocarSenhaRequest(

    val senhaAntiga: String,
    val senhaNova: String,

)

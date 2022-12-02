package br.com.studenton.domain.request

data class EsqueceuSenhaRequest(
    val ra: String,
    val email: String,
    val senhaNova: String
)
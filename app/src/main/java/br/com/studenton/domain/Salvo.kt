package br.com.studenton.domain

data class Salvo (
    val titulo_publicacao: String,
    val descricao_publicacao: String,
    val date: String,
    var selected: Boolean = false
)
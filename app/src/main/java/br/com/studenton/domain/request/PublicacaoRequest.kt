package br.com.studenton.domain.request

data class PublicacaoRequest(
    val titulo: String,
    val texto: String,
    val fkCategoria: Int,
    var tipoPublicacao: Int,
    val fkUsuario: Int,
    var status: Int
)
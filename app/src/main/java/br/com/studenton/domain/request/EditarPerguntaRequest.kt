package br.com.studenton.domain.request

data class EditarPerguntaRequest(
    val idPublicacao: Int,
    val titulo: String,
    val texto: String,
    val fkCategoria: Int
)

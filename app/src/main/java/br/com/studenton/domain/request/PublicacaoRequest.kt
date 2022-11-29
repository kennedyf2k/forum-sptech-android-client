package br.com.studenton.domain.request

data class PublicacaoRequest(
        val titulo: String,
        val texto: String,
        val fkCategoria: Int,
        val tipoPublicacao: Int,
        val fkUsuario: Int,
        val status: Int
)
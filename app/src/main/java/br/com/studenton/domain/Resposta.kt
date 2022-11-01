package br.com.studenton.domain

data class Resposta(

    val idResposta: Int,
    val texto: String,
    val fkUsuario: Int,
    val nomeUsuario: String,
    val fotoUsuario: String,
    val fkPublicacao: Int,
    val dataHora: String,
    val diasAtras: Int,
    val cursoSemestre: String

)

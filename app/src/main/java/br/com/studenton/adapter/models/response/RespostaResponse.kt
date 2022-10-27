package br.com.studenton.adapter.models.response

data class RespostaResponse(

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

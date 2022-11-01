package br.com.studenton.domain

data class Publicacao(

    val idPublicacao: Int,
    val titulo: String,
    val nomeUsuario: String,
    val fotoUsuario: String,
    val texto: String,
    val fkCategoria: Int,
    val categoria: String,
    val tipoPublicacao: Int,
    val fkUsuario: Int,
    val dataHora: String,
    val status: Int,
    val usuariosCurtidas: MutableList<Int>,
    val usuariosSalvos: MutableList<Int>,
    val respostasByIdPublicacao: MutableList<Resposta>,
    val countCurtidas: Int,
    val diasAtras: Int,
    val cursoSemestre: String

)

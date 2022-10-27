package br.com.studenton.adapter.models.response

data class PublicacaoResponse(

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
    val respostasByIdPublicacao: MutableList<RespostaResponse>,
    val countCurtidas: Int,
    val diasAtras: Int,
    val cursoSemestre: String

)

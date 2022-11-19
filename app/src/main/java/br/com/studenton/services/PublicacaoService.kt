package br.com.studenton.services

import br.com.studenton.domain.Publicacao
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PublicacaoService {

    @GET("publicacoes/ordenado")
    fun getAllpublicacoes(): Call<MutableList<Publicacao>>

    @GET("publicacoes/filtro-categoria")
    fun getpublicacoesByCategoria(@Query("idCategoria") idCategoria: Int): Call<MutableList<Publicacao>>

    @GET("publicacoes/minhas-duvidas")
    fun getMinhasPublicacoes(@Query("id") idUsuario: Int): Call<MutableList<Publicacao>>

    @GET("publicacoes/perguntas")
    fun getPerguntas(): Call<MutableList<Publicacao>>

}
package br.com.studenton.services

import br.com.studenton.domain.Publicacao
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PublicacaoService {

    @GET("publicacoes")
    fun GetAllpublicacoes(): Call<MutableList<Publicacao>>

    @GET("publicacoes/filtro-categoria")
    fun GetpublicacoesByCategoria(@Query("idCategoria") idCategoria: Int): Call<MutableList<Publicacao>>

}
package br.com.studenton.services

import br.com.studenton.domain.Publicacao
import retrofit2.Call
import retrofit2.http.GET

interface PublicacaoService {

    @GET("publicacoes")
    fun GetAllpublicacoes(): Call<MutableList<Publicacao>>

}
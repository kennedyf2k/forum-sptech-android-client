package br.com.studenton.services

import br.com.studenton.adapter.models.response.PublicacaoResponse
import retrofit2.Call
import retrofit2.http.GET

interface Publicacao {

    @GET("publicacoes")
    fun GetAllpublicacoes(): Call<MutableList<PublicacaoResponse>>

}
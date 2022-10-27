package br.com.studenton.services

import br.com.studenton.adapter.models.response.CategoriaResponse
import retrofit2.Call
import retrofit2.http.GET

interface Categoria {

    @GET("categorias")
    fun categorias(): Call<MutableList<CategoriaResponse>>

}
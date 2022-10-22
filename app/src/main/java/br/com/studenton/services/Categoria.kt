package br.com.studenton.services

import br.com.studenton.models.request.LoginRequest
import br.com.studenton.models.response.CategoriaResponse
import br.com.studenton.models.response.LoginResponse
import retrofit2.Call
import retrofit2.http.GET

interface Categoria {

    @GET("categorias")
    fun categorias(): Call<MutableList<CategoriaResponse>>

}
package br.com.studenton.services

import br.com.studenton.domain.Categoria
import retrofit2.Call
import retrofit2.http.GET

interface CategoriaService {

    @GET("categorias")
    fun categorias(): Call<MutableList<Categoria>>

}
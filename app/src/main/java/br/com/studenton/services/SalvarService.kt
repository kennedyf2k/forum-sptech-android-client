package br.com.studenton.services

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface SalvarService {

    @POST("favoritos")
    fun favoritar(@Query("idUsuario") idUsuario: Int, @Query("idPublicacao") idPublicacao: Int): Call<Boolean>

}
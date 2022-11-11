package br.com.studenton.services

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CurtirService {

    @POST("curtidas")
    fun curtir(@Query("idUsuario") idUsuario: Int, @Query("idPublicacao") idPublicacao: Int): Call<Boolean>

    @GET("curtidas/verificar")
    fun getCurtir(@Query("idUsu") idUsuario: Int, @Query("idPub") idPublicacao: Int): Call<Boolean>
}
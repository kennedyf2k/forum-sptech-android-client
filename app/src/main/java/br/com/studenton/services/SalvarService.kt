package br.com.studenton.services

import br.com.studenton.domain.Publicacao
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SalvarService {

    @POST("favoritos")
    fun favoritar(@Query("idUsuario") idUsuario: Int, @Query("idPublicacao") idPublicacao: Int): Call<Boolean>

    @GET("favoritos/filtro-favoritos")
    fun getFavoritosByUsuario(@Query("idUsuario") idUsuario: Int): MutableList<Publicacao>
}
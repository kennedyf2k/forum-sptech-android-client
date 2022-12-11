package br.com.studenton.services

import br.com.studenton.domain.Publicacao
import br.com.studenton.domain.request.EditarPerguntaRequest
import retrofit2.Call
import retrofit2.http.*

interface PublicacaoService {

    @GET("publicacoes/ordenado")
    fun getAllpublicacoes(): Call<MutableList<Publicacao>>

    @GET("publicacoes/fltro-status")
    fun getAllPublicacoesAprovadas(@Query("status") status: Int): Call<MutableList<Publicacao>>

    @GET("publicacoes/filtro-categoria")
    fun getpublicacoesByCategoria(@Query("idCategoria") idCategoria: Int): Call<MutableList<Publicacao>>

    @GET("publicacoes/minhas-duvidas")
    fun getMinhasPublicacoes(@Query("id") idUsuario: Int): Call<MutableList<Publicacao>>

    @GET("publicacoes/minha-colaboracao")
    fun getMinhasColaboracoes(@Query("id") idUsuario: Int): Call<MutableList<Publicacao>>

    @GET("publicacoes/perguntas")
    fun getPerguntas(): Call<MutableList<Publicacao>>

    @GET("publicacoes/publicacao")
    fun getPublicacao(@Query("id") idPublicacao: Int): Call<Publicacao>

    @PUT("publiacoes/atualizar-duvida")
    fun putAtualizarDuvida(@Body body: EditarPerguntaRequest): Call<Publicacao>

    @DELETE("publicacoes/apagar-duvida")
    fun deletarDuvida(@Query("id") idPublicacao: Int): Call<Boolean>

    @PATCH("publicacoes/update-status")
    fun atualizarStatus(@Query("idPublicacao") idPublicacao: Int, @Query("status") status: Int): Call<Unit>
}

package br.com.studenton.services

import br.com.studenton.domain.Resposta
import br.com.studenton.domain.request.RespostaRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface RespostaService {

    @POST("respostas")
    fun postarResposta(@Query("idPublicacao") idPublicacao: Int, @Body respostaRequest: RespostaRequest) : Call<Resposta>

}
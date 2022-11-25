package br.com.studenton.services



import br.com.studenton.domain.Publicacao
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PerguntasService {

    @GET("publicacoes/minhas-duvidas")
    fun getPergunta(@Query("id") id: Int): Call<MutableList<Publicacao>>

}
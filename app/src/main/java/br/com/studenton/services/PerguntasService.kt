package br.com.studenton.services



import br.com.studenton.domain.Publicacao
import br.com.studenton.domain.request.PublicacaoRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PerguntasService {

    @GET("publicacoes/minhas-duvidas")
    fun getPergunta(@Query("id") id: Int): Call<MutableList<Publicacao>>

    @POST("publicacoes/publicar")
    fun createPergunta(@Body body: PublicacaoRequest): Call<Unit>

}
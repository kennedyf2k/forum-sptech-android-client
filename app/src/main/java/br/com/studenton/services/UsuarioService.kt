package br.com.studenton.services

import br.com.studenton.domain.Publicacao
import br.com.studenton.domain.request.TrocarSenhaRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface UsuarioService {

    @PATCH("usuarios/mudar-senha")
    fun trocarSenha(@Query("id") id: Int, @Body trocarSenhaRequest: TrocarSenhaRequest): Call<Unit>

}
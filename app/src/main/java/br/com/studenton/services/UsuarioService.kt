package br.com.studenton.services

import br.com.studenton.domain.Login
import br.com.studenton.domain.Publicacao
import br.com.studenton.domain.request.EsqueceuSenhaRequest

import br.com.studenton.domain.request.TrocarSenhaRequest
import retrofit2.Call
import retrofit2.http.*

interface UsuarioService {

    @PATCH("usuarios/mudar-senha")
    fun trocarSenha(@Query("id") id: Int, @Body trocarSenhaRequest: TrocarSenhaRequest): Call<Unit>

    @GET("/usuarios")
    fun getAllUsuarios(): Call<MutableList<Login>>

    @PATCH("usuarios/esqueci-senha")
    fun esqueciSenha(@Body body: EsqueceuSenhaRequest): Call<Unit>

    @PATCH("usuarios/mudar-perfil")
    fun trocarFotoPerfil(@Query("id") id: Int, @Query("perfil") trocarPerfilRequest: String): Call<Unit>




}
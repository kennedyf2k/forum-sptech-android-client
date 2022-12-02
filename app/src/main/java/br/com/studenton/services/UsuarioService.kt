package br.com.studenton.services

import br.com.studenton.domain.Login
<<<<<<< HEAD
=======
import br.com.studenton.domain.Publicacao
import br.com.studenton.domain.request.EsqueceuSenhaRequest
>>>>>>> fe7ea54378e3a37e86aa80f3792077c2784d5b8d
import br.com.studenton.domain.request.TrocarSenhaRequest
import retrofit2.Call
import retrofit2.http.*

interface UsuarioService {

    @PATCH("usuarios/mudar-senha")
    fun trocarSenha(@Query("id") id: Int, @Body trocarSenhaRequest: TrocarSenhaRequest): Call<Unit>

    @GET("/usuarios")
    fun getAllUsuarios(): Call<MutableList<Login>>

<<<<<<< HEAD
    @PATCH("usuarios/mudar-perfil")
    fun trocarFotoPerfil(@Query("id") id: Int, @Query("perfil") trocarPerfilRequest: String): Call<Unit>
=======
    @PATCH("usuarios/esqueci-senha")
    fun esqueciSenha(@Body body: EsqueceuSenhaRequest): Call<Unit>
>>>>>>> fe7ea54378e3a37e86aa80f3792077c2784d5b8d

}
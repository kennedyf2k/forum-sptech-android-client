package br.com.studenton.services

import br.com.studenton.domain.request.LoginRequest
import br.com.studenton.domain.Login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {

    @POST("usuarios")
    fun login(@Body body: LoginRequest): Call<Login>

}
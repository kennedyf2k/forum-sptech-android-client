package br.com.studenton.services

import br.com.studenton.models.request.LoginRequest
import br.com.studenton.models.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Login {

    @POST("usuarios")
    fun login(@Body body: LoginRequest): Call<LoginResponse>

}
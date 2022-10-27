package br.com.studenton.services

import br.com.studenton.adapter.models.request.LoginRequest
import br.com.studenton.adapter.models.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Login {

    @POST("usuarios")
    fun login(@Body body: LoginRequest): Call<LoginResponse>

}
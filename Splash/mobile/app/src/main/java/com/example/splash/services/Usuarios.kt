package com.example.splash.services


import com.example.splash.models.LoginRequest
import com.example.splash.models.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface Usuarios {

    @POST
    fun login(@Body body: LoginRequest): Call



}
package br.com.studenton.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Rest {

    var baseURL = "http://3.218.7.172:8080/";

    inline fun<reified T> getInstance(): T {

        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseURL).build()

        return retrofit.create(T::class.java)
    }



}
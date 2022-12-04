package br.com.studenton.repository

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Rest {

    var baseURLAws = "http://52.205.107.174:8080/"
    var baseURL = "https://sptechforum-backend.azurewebsites.net/"

    inline fun<reified T> getInstance(): T {

        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseURL).build()

        return retrofit.create(T::class.java)
    }

    inline fun<reified T> getInstanceByAws(): T {

        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseURLAws).build()

        return retrofit.create(T::class.java)
    }

}
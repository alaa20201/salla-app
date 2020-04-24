package com.example.salaahapp.network.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface SallaApi {
    @GET("/v1/timingsByCity")
    fun getPosts(@Query("city") city: String = "Bangalore",
                 @Query("country") country: String? = "India",
                 @Query("method") method: Int? = 8): Call<ResponseBody>

    companion object {
        private const val BASE_URL = "http://api.aladhan.com"

        fun createService(): SallaApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SallaApi::class.java)
        }
    }
}
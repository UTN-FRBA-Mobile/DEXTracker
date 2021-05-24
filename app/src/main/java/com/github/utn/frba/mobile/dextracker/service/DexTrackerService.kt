package com.github.utn.frba.mobile.dextracker.service

import com.github.utn.frba.mobile.dextracker.data.LoginRequest
import com.github.utn.frba.mobile.dextracker.data.User
import com.github.utn.frba.mobile.dextracker.data.UserDex
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val host = "https://dex-tracker.herokuapp.com"

private val dexTrackerClient: Retrofit = Retrofit.Builder()
    .baseUrl("$host/api/v1/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val dexTrackerService: DexTrackerService = dexTrackerClient.create(DexTrackerService::class.java)

interface DexTrackerService {
    @GET("users/{user_id}/pokedex/{dex_id}")
    fun fetchUserDex(
        @Path("user_id") userId: String,
        @Path("dex_id") dexId: String,
    ): Call<UserDex>

    @POST("login")
    fun loginFromMail(
        @Body login: LoginRequest,
    ): Call<User>

    @POST("token")
    fun validate(
        @Header("dex-token") token: String,
    ): Call<Unit>
}
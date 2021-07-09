package com.github.utn.frba.mobile.dextracker.service

import com.github.utn.frba.mobile.dextracker.data.*
import com.github.utn.frba.mobile.dextracker.utils.objectMapper
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.*

private const val host = "https://dex-tracker.herokuapp.com"

private val dexTrackerClient: Retrofit = Retrofit.Builder()
    .baseUrl("$host/api/v1/")
    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
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
    ): Call<User>

    @PATCH("users/{user_id}")
    fun updateUser(
        @Path("user_id") userId: String,
        @Header("dex-token") token: String,
        @Body updateUser: UpdateUserDTO,
    ): Call<User>

    @GET("games/{game}/pokemon/{pokemon}")
    fun fetchPokemon(
        @Path("game") game: String,
        @Path("pokemon") pokemon: String,
    ): Call<Pokemon>

    @GET("users/{userId}")
    fun fetchUser(
        @Path("userId") userId: String,
    ): Call<User>

    @GET("pokedex")
    fun fetchPokedex(
    ): Call<List<Pokedex>>

    @POST("users/{user_id}/pokedex")
    fun newDex(
        @Path("user_id") userId: String,
        @Header("dex-token") token: String,
        @Body newDex: DexRequest,
    ): Call<UserDex>

    @POST("users/{user_id}/subscriptions")
    fun subscribe(
        @Path("user_id") userId: String,
        @Header("dex-token") token: String,
        @Body subscription: SubscribeDTO,
    )

    @DELETE("users/{user_id}/subscriptions/{id}")
    fun unsubscribe(
        @Path("user_id") userId: String,
        @Header("dex-token") token: String,
        @Path("id") id: String,
    )
}
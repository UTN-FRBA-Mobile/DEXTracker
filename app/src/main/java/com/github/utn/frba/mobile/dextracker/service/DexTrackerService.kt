package com.github.utn.frba.mobile.dextracker.service

import com.github.utn.frba.mobile.dextracker.data.UserDex
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DexTrackerService {
    @GET("users/{user_id}/pokedex/{dex_id}")
    fun fetchUserDex(
        @Path("user_id") userId: String,
        @Path("dex_id") dexId: String
    ): Call<UserDex>
}
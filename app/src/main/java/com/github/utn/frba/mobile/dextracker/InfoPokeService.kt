package com.github.utn.frba.mobile.dextracker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface InfoPokeService {
    @GET("games/{game}/pokemon/{pokemon}")
    fun getPoke(
        @Path("game") game:String,
        @Path("pokemon") pokemon:String
    ): Call<GetPokeResponse>
}
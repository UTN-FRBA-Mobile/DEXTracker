package com.github.utn.frba.mobile.dextracker

import retrofit2.Call
import retrofit2.http.GET

interface InfoPokeService {
    @GET()
    fun getPoke(): Call<GetPokeResponse>
}
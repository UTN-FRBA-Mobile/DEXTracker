package com.github.utn.frba.mobile.dextracker.data

data class LoginRequest(
    val mail: String,
    val googleToken: String,
)

data class SubscribeDTO(
    val userId: String,
    val dexId: String,
)

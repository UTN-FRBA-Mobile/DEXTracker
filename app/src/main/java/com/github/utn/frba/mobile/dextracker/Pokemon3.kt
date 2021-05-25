package com.github.utn.frba.mobile.dextracker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pokemon3(
    val name: String,
    val nationalPokedexNumber: Int,
    val primaryAbility: String,
    val secondaryAbility: String?,
    val hiddenAbility: String?,
    @SerializedName("evolutions")
    val evolutions: List<Evolution3>,
    @SerializedName("forms")
    val forms: List<Form3>,
    val gen: Int,
)

data class Evolution3(
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("method")
    @Expose
    val method: EvolutionMethod3,
)
data class EvolutionMethod3(
    val type: String,
    val level: Int?,
    val friendship: Int?,
    val move: String?,
    val location: String?,
    val time: String?,
    val item: String?,
    val gender: String?,
    val upsideDown: Boolean?,
    val region: String?,
    val pokemon: String?,
)
data class Form3(
    @SerializedName("name")
    @Expose
    val name: String,
)
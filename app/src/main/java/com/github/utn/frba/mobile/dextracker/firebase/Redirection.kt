package com.github.utn.frba.mobile.dextracker.firebase

import androidx.fragment.app.Fragment
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.utn.frba.mobile.dextracker.PokedexFragment

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    visible = true,
)
@JsonSubTypes(
    JsonSubTypes.Type(value = RedirectToPokedex::class, name = "POKEDEX"),
)
interface Redirection {
    fun to(): Fragment

    fun location(): String
}

data class RedirectToPokedex(
    val userId: String,
    val dexId: String,
    val type: String = "POKEDEX",
) : Redirection {
    override fun to(): Fragment = PokedexFragment.newInstance(
        userId = userId,
        dexId = dexId,
    )

    override fun location(): String = "/users/$userId/dex/$dexId"
}

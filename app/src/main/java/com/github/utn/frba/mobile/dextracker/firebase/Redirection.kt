package com.github.utn.frba.mobile.dextracker.firebase

import androidx.fragment.app.Fragment
import com.github.utn.frba.mobile.dextracker.PokedexFragment

var redirect: Redirection? = null
    get() = field?.also { field = null }

interface Redirection {
    fun to(): Fragment
}

data class RedirectToPokedex(
    val userId: String,
    val dexId: String,
) : Redirection {
    override fun to(): Fragment = PokedexFragment.newInstance(
        userId = userId,
        dexId = dexId,
    )
}

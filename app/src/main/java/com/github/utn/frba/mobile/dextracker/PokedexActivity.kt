package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.utn.frba.mobile.dextracker.ui.pokedex.PokemonFragment

class PokedexActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokedex)

        supportFragmentManager.beginTransaction().apply {
            add(
                R.id.pokemon_table,
                PokemonFragment.newInstance(
                    pokemonName = "bulbasaur",
                    pokemonPictureResourceId = R.drawable.placeholder,
                )
            )
            add(
                R.id.pokemon_table,
                PokemonFragment.newInstance(
                    pokemonName = "ivysaur",
                    pokemonPictureResourceId = R.drawable.placeholder,
                )
            )
            add(
                R.id.pokemon_table,
                PokemonFragment.newInstance(
                    pokemonName = "venusaur",
                    pokemonPictureResourceId = R.drawable.placeholder,
                )
            )

            commit()
        }
    }


}
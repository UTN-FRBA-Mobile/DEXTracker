package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.utn.frba.mobile.dextracker.adapter.PokemonEvolutionsAdapter
import com.github.utn.frba.mobile.dextracker.adapter.PokemonFormsAdapter
import com.github.utn.frba.mobile.dextracker.adapter.PokemonInfoAdapter
import com.github.utn.frba.mobile.dextracker.data.Game
import com.github.utn.frba.mobile.dextracker.data.Pokemon
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import com.github.utn.frba.mobile.dextracker.utils.objectMapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val GAME = "game"
private const val POKEMON = "ppokemon"

class PokemonInfoFragment private constructor() : Fragment() {
    private lateinit var game: Game
    private lateinit var pokemon: String
    private lateinit var infoAdapter: PokemonInfoAdapter
    private lateinit var evolutionAdapter: PokemonEvolutionsAdapter
    private lateinit var formsAdapter: PokemonFormsAdapter
    private lateinit var infoRecyclerView: RecyclerView
    private lateinit var evolutionRecyclerView: RecyclerView
    private lateinit var formsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            game = objectMapper.readValue(it.getString(GAME)!!)
            pokemon = it.getString(POKEMON)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_info_poke, container, false)
        infoRecyclerView = view.findViewById(R.id.info_recycler_view)
        evolutionRecyclerView = view.findViewById(R.id.evolution_recycler_view)
        formsRecyclerView = view.findViewById(R.id.forms_recycler_view)
        return view
    }

    override fun onStart() {
        super.onStart()

        dexTrackerService.fetchPokemon(game.name, pokemon)
            .enqueue(object : Callback<Pokemon> {
                override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                    if (response.body() != null) {
                        infoAdapter = PokemonInfoAdapter(
                            pokemon = response.body()!!,
                            game = game,
                        )

                        with(infoRecyclerView) {
                            layoutManager = LinearLayoutManager(context)
                            adapter = infoAdapter
                        }

                        evolutionAdapter = PokemonEvolutionsAdapter(
                            game = game,
                            pokemon = response.body()!!,
                        )

                        with(evolutionRecyclerView) {
                            layoutManager = LinearLayoutManager(context)
                            adapter = evolutionAdapter
                        }

                        formsAdapter = PokemonFormsAdapter(response.body()!!.forms)

                        with(formsRecyclerView) {
                            layoutManager = LinearLayoutManager(context)
                            adapter = formsAdapter
                        }
                    } else
                        Toast.makeText(activity, "Pokemon no encontrado", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<Pokemon>, error: Throwable) {
                    Toast.makeText(activity, "No responde API", Toast.LENGTH_SHORT).show()
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance(game: Game, pokemon: String) =
            PokemonInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(GAME, objectMapper.writeValueAsString(game))
                    putString(POKEMON, pokemon)
                }
            }
    }
}
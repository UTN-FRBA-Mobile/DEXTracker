package com.github.utn.frba.mobile.dextracker

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
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
private const val POKEMON = "pokemon"
private const val FAVOURITE = "favourite"

class PokemonInfoFragment private constructor() : Fragment() {
    private lateinit var game: Game
    private lateinit var pokemonName: String
    private lateinit var infoAdapter: PokemonInfoAdapter
    private lateinit var evolutionAdapter: PokemonEvolutionsAdapter
    private lateinit var formsAdapter: PokemonFormsAdapter
    private lateinit var infoRecyclerView: RecyclerView
    private lateinit var evolutionRecyclerView: RecyclerView
    private lateinit var formsRecyclerView: RecyclerView
    private lateinit var favouriteButton: ImageView
    private lateinit var pokemon: Pokemon
    private var isFavourite: Boolean = false
    lateinit var addFavourite: (Pokemon) -> Unit
    lateinit var removeFavourite: (Pokemon) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            game = objectMapper.readValue(it.getString(GAME)!!)
            pokemonName = it.getString(POKEMON)!!
            isFavourite = it.getBoolean(FAVOURITE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_info_poke, container, false)
        infoRecyclerView = view.findViewById(R.id.info_recycler_view)
        evolutionRecyclerView = view.findViewById(R.id.evolution_recycler_view)
        formsRecyclerView = view.findViewById(R.id.forms_recycler_view)
        favouriteButton = view.findViewById(R.id.favourite)
        favouriteButton.visibility = View.INVISIBLE
        favouriteButton.setColorFilter(if (isFavourite) R.color.yellow else R.color.white)
        setFavourite(isFavourite)

        return view
    }

    override fun onStart() {
        super.onStart()

        dexTrackerService.fetchPokemon(game.name, pokemonName)
            .enqueue(object : Callback<Pokemon> {
                override fun onResponse(call: Call<Pokemon>, response: Response<Pokemon>) {
                    if (response.body() != null) {
                        pokemon = response.body()!!
                        infoAdapter = PokemonInfoAdapter(
                            pokemon = pokemon,
                            game = game,
                            onFavourite = addFavourite,
                            context = context!!,
                        )

                        favouriteButton.visibility = View.VISIBLE
                        favouriteButton.setOnClickListener {
                            setFavourite(!isFavourite)

                            if (isFavourite) {
                                addFavourite(pokemon)
                            } else {
                                removeFavourite(pokemon)
                            }
                        }

                        with(infoRecyclerView) {
                            layoutManager = LinearLayoutManager(context)
                            adapter = infoAdapter
                        }

                        evolutionAdapter = PokemonEvolutionsAdapter(
                            game = game,
                            pokemon = response.body()!!,
                            context = context!!,
                        )

                        with(evolutionRecyclerView) {
                            layoutManager = LinearLayoutManager(context)
                            adapter = evolutionAdapter
                        }

                        formsAdapter = PokemonFormsAdapter(
                            pokemon = response.body()!!,
                            context = context!!,
                        )

                        with(formsRecyclerView) {
                            layoutManager = GridLayoutManager(context, 3)
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

    private fun setFavourite(b: Boolean) {
        isFavourite = b

        val color = if (isFavourite) {
            R.color.yellow
        } else {
            R.color.white
        }

        favouriteButton.setColorFilter(
            ContextCompat.getColor(requireContext(), color),
            PorterDuff.Mode.SRC_IN,
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(game: Game, pokemon: String, favourite: Boolean) =
            PokemonInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(GAME, objectMapper.writeValueAsString(game))
                    putString(POKEMON, pokemon)
                    putBoolean(FAVOURITE, favourite)
                }
            }
    }
}
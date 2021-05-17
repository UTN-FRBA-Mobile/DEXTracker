package com.github.utn.frba.mobile.dextracker.ui.pokedex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.utn.frba.mobile.dextracker.R

private const val POKEMON_NAME = "pokemon_name"
private const val POKEMON_PICTURE = "pokemon_picture"

/**
 * A simple [Fragment] subclass.
 * Use the [PokemonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PokemonFragment : Fragment() {
    private var pokemonName: String? = null
    private var pokemonPictureResourceId: Int? = null

    private var caught: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemonName = it.getString(POKEMON_NAME)
            pokemonPictureResourceId = it.getInt(POKEMON_PICTURE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pokemon, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<LinearLayout>(R.id.bg).setOnClickListener {
            caught = !caught
            val color = if (caught) R.color.green else R.color.white
            it.setBackgroundResource(color)
        }

        view.findViewById<TextView>(R.id.pokemon_name).apply {
            text = pokemonName
        }

        view.findViewById<ImageView>(R.id.pokemon_picture).apply {
            this.setImageResource(pokemonPictureResourceId!!)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param pokemonName Parameter 1.
         * @param pokemonPictureResourceId Parameter 2.
         * @return A new instance of fragment PokemonFragment.
         */
        @JvmStatic
        fun newInstance(pokemonName: String, pokemonPictureResourceId: Int) =
            PokemonFragment().apply {
                arguments = Bundle().apply {
                    putString(POKEMON_NAME, pokemonName)
                    putInt(POKEMON_PICTURE, pokemonPictureResourceId)
                }
            }
    }
}
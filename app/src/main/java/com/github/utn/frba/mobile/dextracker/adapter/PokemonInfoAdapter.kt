package com.github.utn.frba.mobile.dextracker.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.Game
import com.github.utn.frba.mobile.dextracker.data.Pokemon

class PokemonInfoAdapter(
    private val pokemon: Pokemon,
    private val game: Game,
    private val context: Context,
) : RecyclerView.Adapter<PokemonInfoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_pokeinfo,
                parent,
                false,
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gameKey = game.name.takeWhile { it != '-' }
        //Me parece mejor usar gif para todos directamente
        /*val url = if(gameKey == "bw" || gameKey == "b2w2")
            "https://dex-tracker.herokuapp.com/sprites/bw/${pokemon.name}.gif"
        else
            "https://dex-tracker.herokuapp.com/sprites/$gameKey/${pokemon.name}.png"*/
        val url = "https://dex-tracker.herokuapp.com/sprites/bw/${pokemon.name}.gif"
        holder.itemView.findViewById<TextView>(R.id.pokename).text =
            pokemon.name
        holder.itemView.findViewById<TextView>(R.id.pokenationalPokedexNumber).text =
            "pokedex number: ${pokemon.nationalPokedexNumber}"
        holder.itemView.findViewById<TextView>(R.id.pokeprimaryAbility).text =
            "primary ability: ${pokemon.primaryAbility}"
        if (pokemon.secondaryAbility != null) holder.itemView.findViewById<TextView>(R.id.pokesecondaryAbility).text =
            "secondary ability: ${pokemon.secondaryAbility}"
        else holder.itemView.findViewById<TextView>(R.id.pokesecondaryAbility).visibility =
            View.GONE
        if (pokemon.hiddenAbility != null) holder.itemView.findViewById<TextView>(R.id.pokehiddenAbility).text =
            "hidden ability: ${pokemon.hiddenAbility}"
        else holder.itemView.findViewById<TextView>(R.id.pokehiddenAbility).visibility =
            View.GONE
        holder.itemView.findViewById<TextView>(R.id.pokegen).text =
            "Generation: ${pokemon.gen}"

        Glide.with(context)
            .load(Uri.parse(url))
            .into(holder.itemView.findViewById(R.id.pokeimage))
    }

    override fun getItemCount(): Int = 1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }
}
package com.github.utn.frba.mobile.dextracker.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.*
import com.squareup.picasso.Picasso

class PokemonEvolutionsAdapter(
    private val game: Game,
    private val pokemon: Pokemon,
) : RecyclerView.Adapter<PokemonEvolutionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_poke_evolution, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val evolution = pokemon.evolutions[position]

        val gameKey = game.name.takeWhile { it != '-' }

        holder.itemView.findViewById<TextView>(R.id.pokeevoname).text = evolution.name
        holder.itemView.findViewById<TextView>(R.id.pokeevotype).text =
            "type: ${evolution.method.type}"
        when (val method = evolution.method) {
            is LevelUp -> levelUpEvolution(method, holder)
            is UseItem -> itemEvolution(holder, method)
            is Trade -> tradeEvolution(method, holder)
        }

        val url = "https://dex-tracker.herokuapp.com/sprites/$gameKey/${evolution.name}.png"
        val item = holder.itemView.findViewById<ImageView>(R.id.pokemon_evolution_image)
        item.visibility = View.VISIBLE
        Picasso.get()
            .load(Uri.parse(url))
            .into(item)
    }

    private fun levelUpEvolution(method: LevelUp, holder: ViewHolder) {
        if (method.level != null) holder.itemView.findViewById<TextView>(R.id.pokelevel).text =
            "level: ${method.level}"
        else holder.itemView.findViewById<TextView>(R.id.pokelevel).visibility = View.GONE
        if (method.friendship != null) holder.itemView.findViewById<TextView>(R.id.pokefriendship).text =
            "friendship: ${method.friendship}"
        else holder.itemView.findViewById<TextView>(R.id.pokefriendship).visibility =
            View.GONE
        if (method.move != null) holder.itemView.findViewById<TextView>(R.id.pokemove).text =
            "move: ${method.move}"
        else holder.itemView.findViewById<TextView>(R.id.pokemove).visibility = View.GONE
        if (method.location != null) holder.itemView.findViewById<TextView>(R.id.pokelocation).text =
            "location: ${method.location}"
        else holder.itemView.findViewById<TextView>(R.id.pokelocation).visibility =
            View.GONE
        if (method.time != null) holder.itemView.findViewById<TextView>(R.id.poketime).text =
            "time: ${method.time}"
        else holder.itemView.findViewById<TextView>(R.id.poketime).visibility = View.GONE
        if (method.item != null) holder.itemView.findViewById<TextView>(R.id.pokeitem).text =
            "item: ${method.item}"
        else holder.itemView.findViewById<TextView>(R.id.pokeitem).visibility = View.GONE
        if (method.region != null) holder.itemView.findViewById<TextView>(R.id.pokeregion).text =
            "region: ${method.region}"
        else holder.itemView.findViewById<TextView>(R.id.pokeregion).visibility = View.GONE
        if (method.gender != null) holder.itemView.findViewById<TextView>(R.id.pokegender).text =
            "gender: ${method.gender}"
        else holder.itemView.findViewById<TextView>(R.id.pokegender).visibility = View.GONE
        if (method.upsideDown != null) holder.itemView.findViewById<TextView>(R.id.pokeupsidedown).text =
            "upsidedown: ${method.upsideDown}"
        else holder.itemView.findViewById<TextView>(R.id.pokeupsidedown).visibility =
            View.GONE
    }

    private fun itemEvolution(holder: ViewHolder, method: UseItem) {
        holder.itemView.findViewById<TextView>(R.id.pokeitem).text = "item: ${method.item}"
        if (method.region != null) holder.itemView.findViewById<TextView>(R.id.pokeregion).text =
            "region: ${method.region}"
        else holder.itemView.findViewById<TextView>(R.id.pokeregion).visibility = View.GONE
        if (method.gender != null) holder.itemView.findViewById<TextView>(R.id.pokegender).text =
            "gender: ${method.gender}"
        else holder.itemView.findViewById<TextView>(R.id.pokegender).visibility = View.GONE
    }

    private fun tradeEvolution(method: Trade, holder: ViewHolder) {
        if (method.item != null) holder.itemView.findViewById<TextView>(R.id.pokeitem).text =
            "item: ${method.item}"
        else holder.itemView.findViewById<TextView>(R.id.pokeitem).visibility = View.GONE
        if (method.pokemon != null) holder.itemView.findViewById<TextView>(R.id.pokepokemon).text =
            "pokemon: ${method.pokemon}"
        else holder.itemView.findViewById<TextView>(R.id.pokepokemon).visibility = View.GONE
    }

    override fun getItemCount(): Int = pokemon.evolutions.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
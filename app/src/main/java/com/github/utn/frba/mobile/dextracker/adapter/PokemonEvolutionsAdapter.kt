package com.github.utn.frba.mobile.dextracker.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.*

class PokemonEvolutionsAdapter(
    private val game: Game,
    private val pokemon: Pokemon,
    private val context: Context,
) : RecyclerView.Adapter<PokemonEvolutionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_poke_evolution, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val evolution = pokemon.evolutions[position]
        val gameKey = game.name.takeWhile { it != '-' }

        holder.itemView.findViewById<TextView>(R.id.pokeevoname).text =
            evolution.name
        holder.itemView.findViewById<TextView>(R.id.pokeevotype).text =
            "type: ${evolution.method.type}"
        when (val method = evolution.method) {
            is LevelUp -> levelUpEvolution(method, holder)
            is UseItem -> itemEvolution(holder, method)
            is Trade -> tradeEvolution(method, holder)
        }
        val url = "https://dex-tracker.herokuapp.com/sprites/bw/${evolution.name}.gif"
        val item = holder.itemView.findViewById<ImageView>(R.id.pokemon_evolution_image)
        item.visibility = View.VISIBLE
        Glide.with(context)
            .load(Uri.parse(url))
            .into(item)
    }

    private fun fillText(item: TextView, text: String){
        item.text = text
        item.visibility = View.VISIBLE
    }

    private fun levelUpEvolution(method: LevelUp, holder: ViewHolder) {
        if (method.level != null)
            fillText(holder.itemView.findViewById(R.id.pokelevel),"level: ${method.level}")
        if (method.friendship != null)
            fillText(holder.itemView.findViewById(R.id.pokefriendship),"friendship: ${method.friendship}")
        if (method.move != null)
            fillText(holder.itemView.findViewById(R.id.pokemove),"move: ${method.move}")
        if (method.location != null)
            fillText(holder.itemView.findViewById(R.id.pokelocation),"location: ${method.location}")
        if (method.time != null)
            fillText(holder.itemView.findViewById(R.id.poketime),"time: ${method.time}")
        if (method.item != null)
            fillText(holder.itemView.findViewById(R.id.pokeitem),"item: ${method.item}")
        if (method.region != null)
            fillText(holder.itemView.findViewById(R.id.pokeregion),"region: ${method.region}")
        if (method.gender != null)
            fillText(holder.itemView.findViewById(R.id.pokegender),"gender: ${method.gender}")
        if (method.upsideDown != null)
            fillText(holder.itemView.findViewById(R.id.pokeupsidedown),"upsidedown: ${method.upsideDown}")
    }

    private fun itemEvolution(holder: ViewHolder, method: UseItem) {
        fillText(holder.itemView.findViewById(R.id.pokeitem),"item: ${method.item}")
        if (method.region != null)
            fillText(holder.itemView.findViewById(R.id.pokeregion),"region: ${method.region}")
        if (method.gender != null)
            fillText(holder.itemView.findViewById(R.id.pokegender),"gender: ${method.gender}")
    }

    private fun tradeEvolution(method: Trade, holder: ViewHolder) {
        if (method.item != null)
            fillText(holder.itemView.findViewById(R.id.pokeitem),"item: ${method.item}")
        if (method.pokemon != null)
            fillText(holder.itemView.findViewById<TextView>(R.id.pokepokemon),"pokemon: ${method.pokemon}")
    }

    override fun getItemCount(): Int = pokemon.evolutions.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
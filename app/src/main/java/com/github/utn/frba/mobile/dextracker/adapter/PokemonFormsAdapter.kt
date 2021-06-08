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
import com.github.utn.frba.mobile.dextracker.data.Pokemon

class PokemonFormsAdapter(
    private val pokemon: Pokemon,
    private val context: Context,
) : RecyclerView.Adapter<PokemonFormsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_poke_form, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val form = pokemon.forms[position]
        holder.itemView.findViewById<TextView>(R.id.pokeformname).text =
            form.name
        val url = "https://dex-tracker.herokuapp.com/sprites/bw/${form.name}.gif"
        val item = holder.itemView.findViewById<ImageView>(R.id.pokeimage)
        item.visibility = View.VISIBLE
        Glide.with(context)
            .load(Uri.parse(url))
            .into(item)
    }

    override fun getItemCount(): Int = pokemon.forms.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
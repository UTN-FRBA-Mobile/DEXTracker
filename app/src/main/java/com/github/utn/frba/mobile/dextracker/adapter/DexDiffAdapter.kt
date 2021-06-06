package com.github.utn.frba.mobile.dextracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.UserDexPokemon
import com.github.utn.frba.mobile.dextracker.extensions.observableDataset
import java.util.*

class DexDiffAdapter : RecyclerView.Adapter<DexDiffAdapter.ViewHolder>() {
    private var pokemon: List<UserDexPokemon> by observableDataset(emptyList())

    fun setDataset(pokemon: List<UserDexPokemon>) {
        this.pokemon = pokemon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.pokedex_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = pokemon[position]
        holder.setPokemon(p)
    }

    override fun getItemCount(): Int = pokemon.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.pokemon_picture)
        private val nameView: TextView = itemView.findViewById(R.id.pokemon_name)
        private val numberView: TextView = itemView.findViewById(R.id.pokemon_number)

        fun setPokemon(p: UserDexPokemon) {
            imageView.setImageResource(R.drawable.placeholder)
            itemView.setBackgroundResource(if (p.caught) R.color.green else R.color.background_disabled)
            numberView.apply {
                text = pad(p.dexNumber)
                if (!p.caught) disabled()
            }

            nameView.apply {
                text = p.name.capitalize(Locale.getDefault())
                if (!p.caught) disabled()
            }

            if (!p.caught) imageView.alpha = 0.5f
        }

        private fun TextView.disabled() {
            setTextColor(resources.getColor(R.color.text_disabled))
        }
    }

    private fun pad(n: Int) = when (n) {
        in 1..9 -> "00$n"
        in 10..100 -> "0$n"
        else -> n.toString()
    }
}
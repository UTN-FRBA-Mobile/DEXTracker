package com.github.utn.frba.mobile.dextracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.UserDexPokemon
import java.util.*
import kotlin.properties.Delegates

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.pokemon_picture)
    private val nameView: TextView = itemView.findViewById(R.id.pokemon_name)
    private val numberView: TextView = itemView.findViewById(R.id.pokemon_number)

    var name: String by Delegates.observable("") { _, _, new ->
        nameView.text = new.capitalize(Locale.getDefault())
    }
    var number: Int by Delegates.observable(0) { _, _, new ->
        numberView.text = pad(new)
    }

    var caught: Boolean by Delegates.observable(false) { _, _, new ->
        setBackground(new)
    }

    init {
        imageView.setImageResource(R.drawable.placeholder)
        itemView.setOnClickListener {
            caught = !caught
        }
    }

    private fun pad(n: Int) = when (n) {
        in 1..9 -> "00$n"
        in 10..100 -> "0$n"
        else -> n.toString()
    }

    private fun setBackground(caught: Boolean) {
        itemView.setBackgroundResource(if (caught) R.color.green else R.color.light_gray)
    }
}

class UserDexAdapter(
    private val context: Context,
) : RecyclerView.Adapter<ViewHolder>() {
    private val dataset: MutableList<UserDexPokemon> = mutableListOf()

    fun add(userDex: List<UserDexPokemon>) {
        dataset.addAll(userDex)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.pokedex_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = dataset[position]
        holder.name = p.name
        holder.number = position + 1
        holder.caught = p.caught
    }

    override fun getItemCount(): Int = dataset.size
}
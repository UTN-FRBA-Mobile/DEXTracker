package com.github.utn.frba.mobile.dextracker.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.Game
import com.github.utn.frba.mobile.dextracker.data.UserDexPokemon
import com.github.utn.frba.mobile.dextracker.extensions.mapIf
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.properties.Delegates

class UserDexAdapter(
    private val game: String,
    private val canEdit: Boolean,
    private val openEditor: () -> Unit,
    private val openPokemonInfo: (String) -> Unit,
) : RecyclerView.Adapter<UserDexAdapter.ViewHolder>() {
    var searchText: String by Delegates.observable("") { _, _, new ->
        filter(new)
    }

    private var dataset: MutableList<UserDexPokemon> = mutableListOf()
    var fullDataset: List<UserDexPokemon> = emptyList()
    var isEditing: Boolean = false

    fun add(userDex: List<UserDexPokemon>) {
        dataset.addAll(userDex)
        fullDataset = userDex
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.pokedex_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = dataset[position]
        holder.name = p.name
        holder.number = p.dexNumber
        holder.caught = p.caught
        /*val gameKey = game.name.takeWhile { it != '-' }
        val url = if(gameKey == "bw" || gameKey == "b2w2")
            "https://dex-tracker.herokuapp.com/sprites/$game/${p.name}.gif"
        else
            "https://dex-tracker.herokuapp.com/sprites/$gameKey/${p.name}.png"*/
        val url = "https://dex-tracker.herokuapp.com/sprites/$game/${p.name}.gif"
        Picasso.get()
            .load(Uri.parse(url))
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = dataset.size

    private fun filter(search: String) {
        dataset = if (search == "") {
            fullDataset.toMutableList()
        } else {
            fullDataset.filter {
                it.name
                    .toLowerCase(Locale.getDefault())
                    .contains(
                        search.toLowerCase(Locale.getDefault())
                    ) || it.dexNumber.toString()
                    .toLowerCase(Locale.getDefault())
                    .startsWith(search.toLowerCase(Locale.getDefault()))
            }
                .toMutableList()
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.pokemon_picture)
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
            fullDataset = fullDataset.mapIf({ it.dexNumber == this.number }) {
                it.copy(caught = new)
            }
        }

        init {
            imageView.setImageResource(R.drawable.placeholder)
            itemView.setOnLongClickListener {
                if (canEdit) {
                    openEditor()
                    caught = !caught
                }
                true
            }
            itemView.setOnClickListener {
                if (isEditing && canEdit) caught = !caught
                else openPokemonInfo(this.name)
            }
        }

        private fun pad(n: Int) = when (n) {
            in 1..9 -> "00$n"
            in 10..100 -> "0$n"
            else -> n.toString()
        }

        private fun setBackground(caught: Boolean) {
            itemView.setBackgroundResource(if (caught) R.drawable.custom_background_poke else R.drawable.custom_background_poke_uncaught)
        }
    }
}
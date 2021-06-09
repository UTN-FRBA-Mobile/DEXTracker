package com.github.utn.frba.mobile.dextracker

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.utn.frba.mobile.dextracker.data.UserDexPokemon

import com.github.utn.frba.mobile.dextracker.dummy.DummyContent.DummyItem
import java.util.*
import kotlin.properties.Delegates

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class FavPokesRecyclerViewAdapter(
    private val values: List<DummyItem>
) : RecyclerView.Adapter<FavPokesRecyclerViewAdapter.ViewHolder>() {

    var searchText: String by Delegates.observable("") { _, _, new ->
        filter(new)
    }
    private var dataset: MutableList<UserDexPokemon> = mutableListOf()
    var fullDataset: List<UserDexPokemon> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_fav_pokes_, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = dataset[position]
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_numberFAVPOKES)
        val contentView: TextView = view.findViewById(R.id.contentFAVPOKES)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

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
}
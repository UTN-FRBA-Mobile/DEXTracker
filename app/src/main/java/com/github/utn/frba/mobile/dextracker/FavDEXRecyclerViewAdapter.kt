package com.github.utn.frba.mobile.dextracker

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.github.utn.frba.mobile.dextracker.dummy.DummyContent.DummyItem
import com.github.utn.frba.mobile.dextracker.model.PokedexRef
import java.util.*
import kotlin.properties.Delegates

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class FavDEXRecyclerViewAdapter(
    private var dex: List<PokedexRef>,
    private val onClick: (String, String) -> Unit,
) : RecyclerView.Adapter<FavDEXRecyclerViewAdapter.ViewHolder>() {

    private val originalDex = dex
    var searchText: String by Delegates.observable("") { _, _, new ->
        filter(new)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_fav_dex_, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dex[position]
        holder.idView.text = item.id
    }

    override fun getItemCount(): Int = dex.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_numberFAVDEX)
        val contentView: TextView = view.findViewById(R.id.contentFAVDEX)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    private fun filter(search: String) {
        dex = if (search == "") {
            originalDex
        } else {
            originalDex.filter {
                it.game.displayName
                    .toLowerCase(Locale.getDefault())
                    .contains(search.toLowerCase(Locale.getDefault()))
            }
        }
        notifyDataSetChanged()
    }
}
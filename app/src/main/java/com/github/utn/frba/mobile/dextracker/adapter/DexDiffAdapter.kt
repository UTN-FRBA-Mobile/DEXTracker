package com.github.utn.frba.mobile.dextracker.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.data.UserDexPokemon
import kotlin.math.min

class DexDiffAdapter : RecyclerView.Adapter<DexDiffAdapter.ViewHolder>() {
    private val left: MutableList<UserDexPokemon> = mutableListOf()
    private val right: MutableList<UserDexPokemon> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = min(left.size, right.size)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
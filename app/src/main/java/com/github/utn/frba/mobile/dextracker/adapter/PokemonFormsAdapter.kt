package com.github.utn.frba.mobile.dextracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.Form

class PokemonFormsAdapter(
    private val forms: List<Form>,
) : RecyclerView.Adapter<PokemonFormsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_poke_form, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val index = forms[position]
        holder.itemView.findViewById<TextView>(R.id.pokeformname).text = index.name
    }

    override fun getItemCount(): Int = forms.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
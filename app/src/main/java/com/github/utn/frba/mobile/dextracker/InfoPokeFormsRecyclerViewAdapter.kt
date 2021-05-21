package com.github.utn.frba.mobile.dextracker

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.utn.frba.mobile.dextracker.dummy.DummyContent.DummyItem
import com.squareup.picasso.Picasso

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class InfoPokeFormsRecyclerViewAdapter(
        private val forms: List<Form4>,
): RecyclerView.Adapter<InfoPokeFormsRecyclerViewAdapter.ViewHolder>() {

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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /*val idView: TextView = view.findViewById(R.id.item_numberMISDEX)
        val contentView: TextView = view.findViewById(R.id.contentMISDEX)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }*/
    }
}
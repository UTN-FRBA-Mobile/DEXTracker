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
class InfoPokeRecyclerViewAdapter(
    private val listener: InfoPokeFragment.OnFragmentInteractionListener?,
    private val poke: GetPokeResponse
    ): RecyclerView.Adapter<InfoPokeRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pokeinfo, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //IMAGEN
        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+poke.nationalPokedexNumber.toString()+".png"

        holder.itemView.findViewById<TextView>(R.id.pokename).text = poke.name
        holder.itemView.findViewById<TextView>(R.id.pokenationalPokedexNumber).text = "pokedex number: "+poke.nationalPokedexNumber.toString()
        holder.itemView.findViewById<TextView>(R.id.pokeprimaryAbility).text = "primary ability: "+poke.primaryAbility
        if(poke.secondaryAbility!=null)holder.itemView.findViewById<TextView>(R.id.pokesecondaryAbility).text = "secondary ability: "+poke.secondaryAbility
            else holder.itemView.findViewById<TextView>(R.id.pokesecondaryAbility).visibility = View.GONE
        if(poke.hiddenAbility!=null)holder.itemView.findViewById<TextView>(R.id.pokehiddenAbility).text = "hidden ability: "+poke.hiddenAbility
            else holder.itemView.findViewById<TextView>(R.id.pokehiddenAbility).visibility = View.GONE
        holder.itemView.findViewById<TextView>(R.id.pokegen).text = "Generation: "+poke.gen.toString()

        if (url != null){//checkear esto
            Picasso.get().load(Uri.parse(url)).into(holder.itemView.findViewById<ImageView>(R.id.pokeimage))
        }
    }

    override fun getItemCount(): Int = 1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /*val idView: TextView = view.findViewById(R.id.item_numberMISDEX)
        val contentView: TextView = view.findViewById(R.id.contentMISDEX)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }*/
    }
}
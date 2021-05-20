package com.github.utn.frba.mobile.dextracker

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.utn.frba.mobile.dextracker.dummy.DummyContent.DummyItem
import com.squareup.picasso.Picasso

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
class InfoPokeEvoRecyclerViewAdapter(
        private val evolutions: List<Evolution4>,
): RecyclerView.Adapter<InfoPokeEvoRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_poke_evolution, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        for(item in evolutions){
            holder.itemView.findViewById<TextView>(R.id.pokeevoname).text = "name: "+item.name
            holder.itemView.findViewById<TextView>(R.id.pokeevotype).text = "type: "+item.method.type
            if(item.method.level!=null)holder.itemView.findViewById<TextView>(R.id.pokelevel).text = "level: "+item.method.level.toString()
                else holder.itemView.findViewById<TextView>(R.id.pokelevel).visibility = View.GONE
            if(item.method.friendship!=null)holder.itemView.findViewById<TextView>(R.id.pokefriendship).text = "friendship: "+item.method.friendship.toString()
                else holder.itemView.findViewById<TextView>(R.id.pokefriendship).visibility = View.GONE
            if(item.method.move!=null)holder.itemView.findViewById<TextView>(R.id.pokemove).text = "move: "+item.method.move
                else holder.itemView.findViewById<TextView>(R.id.pokemove).visibility = View.GONE
            if(item.method.location!=null)holder.itemView.findViewById<TextView>(R.id.pokelocation).text = "location: "+item.method.location
                else holder.itemView.findViewById<TextView>(R.id.pokelocation).visibility = View.GONE
            if(item.method.time!=null)holder.itemView.findViewById<TextView>(R.id.poketime).text = "time: "+item.method.time
                else holder.itemView.findViewById<TextView>(R.id.poketime).visibility = View.GONE
            if(item.method.item!=null)holder.itemView.findViewById<TextView>(R.id.pokeitem).text = "item: "+item.method.item
                else holder.itemView.findViewById<TextView>(R.id.pokeitem).visibility = View.GONE
            if(item.method.gender!=null)holder.itemView.findViewById<TextView>(R.id.pokegender).text = "gender: "+item.method.gender
                else holder.itemView.findViewById<TextView>(R.id.pokegender).visibility = View.GONE
            if(item.method.upsideDown!=null)holder.itemView.findViewById<TextView>(R.id.pokeupsidedown).text = "upsidedown: "+item.method.upsideDown.toString()
                else holder.itemView.findViewById<TextView>(R.id.pokeupsidedown).visibility = View.GONE
            if(item.method.region!=null)holder.itemView.findViewById<TextView>(R.id.pokeregion).text = "region: "+item.method.region
                else holder.itemView.findViewById<TextView>(R.id.pokeregion).visibility = View.GONE
            if(item.method.pokemon!=null)holder.itemView.findViewById<TextView>(R.id.pokepokemon).text = "pokemon: "+item.method.pokemon
                else holder.itemView.findViewById<TextView>(R.id.pokepokemon).visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = evolutions.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        /*val idView: TextView = view.findViewById(R.id.item_numberMISDEX)
        val contentView: TextView = view.findViewById(R.id.contentMISDEX)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }*/
    }
}
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        val index = evolutions[position]

        val service = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://dex-tracker.herokuapp.com/api/v1/")
                .build()
                .create(InfoPokeService::class.java)

        service.getPoke("b2w2-national",index.name).enqueue(object: Callback<GetPokeResponse> {
            override fun onResponse(call: Call<GetPokeResponse>, response: Response<GetPokeResponse>) {
                val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+response.body()!!.nationalPokedexNumber.toString()+".png"
                val item = holder.itemView.findViewById<ImageView>(R.id.pokeimage)
                Picasso.get().load(Uri.parse(url)).into(item)
                item.visibility=View.VISIBLE
            }
            override fun onFailure(call: Call<GetPokeResponse>, error: Throwable) {

            }
        })

        holder.itemView.findViewById<TextView>(R.id.pokeevoname).text = index.name
        holder.itemView.findViewById<TextView>(R.id.pokeevotype).text = "type: "+ index.method.type
        if(index.method.level!=null)holder.itemView.findViewById<TextView>(R.id.pokelevel).text = "level: "+ index.method.level.toString()
            else holder.itemView.findViewById<TextView>(R.id.pokelevel).visibility = View.GONE
        if(index.method.friendship!=null)holder.itemView.findViewById<TextView>(R.id.pokefriendship).text = "friendship: "+ index.method.friendship.toString()
            else holder.itemView.findViewById<TextView>(R.id.pokefriendship).visibility = View.GONE
        if(index.method.move!=null)holder.itemView.findViewById<TextView>(R.id.pokemove).text = "move: "+ index.method.move
            else holder.itemView.findViewById<TextView>(R.id.pokemove).visibility = View.GONE
        if(index.method.location!=null)holder.itemView.findViewById<TextView>(R.id.pokelocation).text = "location: "+ index.method.location
            else holder.itemView.findViewById<TextView>(R.id.pokelocation).visibility = View.GONE
        if(index.method.time!=null)holder.itemView.findViewById<TextView>(R.id.poketime).text = "time: "+ index.method.time
            else holder.itemView.findViewById<TextView>(R.id.poketime).visibility = View.GONE
        if(index.method.item!=null)holder.itemView.findViewById<TextView>(R.id.pokeitem).text = "item: "+ index.method.item
            else holder.itemView.findViewById<TextView>(R.id.pokeitem).visibility = View.GONE
        if(index.method.gender!=null)holder.itemView.findViewById<TextView>(R.id.pokegender).text = "gender: "+ index.method.gender
            else holder.itemView.findViewById<TextView>(R.id.pokegender).visibility = View.GONE
        if(index.method.upsideDown!=null)holder.itemView.findViewById<TextView>(R.id.pokeupsidedown).text = "upsidedown: "+ index.method.upsideDown.toString()
            else holder.itemView.findViewById<TextView>(R.id.pokeupsidedown).visibility = View.GONE
        if(index.method.region!=null)holder.itemView.findViewById<TextView>(R.id.pokeregion).text = "region: "+ index.method.region
            else holder.itemView.findViewById<TextView>(R.id.pokeregion).visibility = View.GONE
        if(index.method.pokemon!=null)holder.itemView.findViewById<TextView>(R.id.pokepokemon).text = "pokemon: "+ index.method.pokemon
            else holder.itemView.findViewById<TextView>(R.id.pokepokemon).visibility = View.GONE
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
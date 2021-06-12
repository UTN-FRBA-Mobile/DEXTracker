package com.github.utn.frba.mobile.dextracker.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.*

class PokemonEvolutionsAdapter(
    private val game: Game,
    private val pokemon: Pokemon,
    private val context: Context,
) : RecyclerView.Adapter<PokemonEvolutionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_poke_evolution, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val evolution = pokemon.evolutions[position]
        /*var gameKey = game.name.takeWhile { it != '-' }
        gameKey.replace("b2w2","bw").also { gameKey = it }
        gameKey.replace("dppt","dp").also { gameKey = it }
        var url = "https://dex-tracker.herokuapp.com/sprites/$gameKey/${evolution.name}.png"
        if(gameKey == "bw")
            url.replaceAfterLast(".","gif").also { url = it }*/

        anim(holder.evolutions, 1200)
        anim(holder.image, 1300)
        anim(holder.name, 1400)
        anim(holder.method, 1500)
        anim(holder.type, 1900)
        anim(holder.level, 2000)
        anim(holder.friendship, 2000)
        anim(holder.move, 2000)
        anim(holder.location, 2000)
        anim(holder.time, 2000)
        anim(holder.item, 2000)
        anim(holder.gender, 2000)
        anim(holder.upsidedown, 2000)
        anim(holder.region, 2000)
        anim(holder.pokemon, 2000)

        holder.name.text = evolution.name
        holder.type.text = "type: ${evolution.method.type}"
        when (val method = evolution.method) {
            is LevelUp -> levelUpEvolution(method, holder)
            is UseItem -> itemEvolution(holder, method)
            is Trade -> tradeEvolution(method, holder)
        }

        val url = "https://dex-tracker.herokuapp.com/sprites/bw/${evolution.name}.gif"
        holder.image.visibility = View.VISIBLE
        Glide.with(context)
            .load(Uri.parse(url))
            .placeholder(R.drawable.placeholder_pokeball)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    //TODO: something on exception
                    holder.image.layoutParams.height = (32 * Resources.getSystem().displayMetrics.density).toInt()
                    holder.image.layoutParams.width = (32 * Resources.getSystem().displayMetrics.density).toInt()
                    return false
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    //do something when picture already loaded

                    return false
                }
            })
            .into(holder.image)
    }

    private fun anim(item: View, delay: Long){
        item.animate().apply {
            duration = 1000
            startDelay = delay
            alpha(1F)
        }.start()
    }

    private fun fillText(item: TextView, text: String){
        item.text = text
        item.visibility = View.VISIBLE
    }

    private fun levelUpEvolution(method: LevelUp, holder: ViewHolder) {
        if (method.level != null)
            fillText(holder.level,"level: ${method.level}")
        if (method.friendship != null)
            fillText(holder.friendship,"friendship: ${method.friendship}")
        if (method.move != null)
            fillText(holder.move,"move: ${method.move}")
        if (method.location != null)
            fillText(holder.location,"location: ${method.location}")
        if (method.time != null)
            fillText(holder.time,"time: ${method.time}")
        if (method.item != null)
            fillText(holder.item,"item: ${method.item}")
        if (method.region != null)
            fillText(holder.region,"region: ${method.region}")
        if (method.gender != null)
            fillText(holder.gender,"gender: ${method.gender}")
        if (method.upsideDown != null)
            fillText(holder.upsidedown,"upsidedown: ${method.upsideDown}")
    }

    private fun itemEvolution(holder: ViewHolder, method: UseItem) {
        fillText(holder.item,"item: ${method.item}")
        if (method.region != null)
            fillText(holder.region,"region: ${method.region}")
        if (method.gender != null)
            fillText(holder.gender,"gender: ${method.gender}")
    }

    private fun tradeEvolution(method: Trade, holder: ViewHolder) {
        if (method.item != null)
            fillText(holder.item,"item: ${method.item}")
        if (method.pokemon != null)
            fillText(holder.pokemon,"pokemon: ${method.pokemon}")
    }

    override fun getItemCount(): Int = pokemon.evolutions.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val evolutions: TextView = itemView.findViewById(R.id.pokeevolutions)
        val image: ImageView = itemView.findViewById(R.id.pokemon_evolution_image)
        val name: TextView = itemView.findViewById(R.id.pokeevoname)
        val method: TextView = itemView.findViewById(R.id.pokemethod)
        val type: TextView = itemView.findViewById(R.id.pokeevotype)
        val level: TextView = itemView.findViewById(R.id.pokelevel)
        val friendship: TextView = itemView.findViewById(R.id.pokefriendship)
        val move: TextView = itemView.findViewById(R.id.pokemove)
        val location: TextView = itemView.findViewById(R.id.pokelocation)
        val time: TextView = itemView.findViewById(R.id.poketime)
        val item: TextView = itemView.findViewById(R.id.pokeitem)
        val gender: TextView = itemView.findViewById(R.id.pokegender)
        val upsidedown: TextView = itemView.findViewById(R.id.pokeupsidedown)
        val region: TextView = itemView.findViewById(R.id.pokeregion)
        val pokemon: TextView = itemView.findViewById(R.id.pokepokemon)
    }
}
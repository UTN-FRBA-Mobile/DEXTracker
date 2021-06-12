package com.github.utn.frba.mobile.dextracker.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.Target
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.Game
import com.github.utn.frba.mobile.dextracker.data.Pokemon

class PokemonInfoAdapter(
    private val pokemon: Pokemon,
    private val game: Game,
    private val context: Context,
) : RecyclerView.Adapter<PokemonInfoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.fragment_pokeinfo,
                parent,
                false,
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Me parece mejor usar gif para todos directamente
        /*val gameKey = game.name.takeWhile { it != '-' }
        val url = if(gameKey == "bw" || gameKey == "b2w2")
            "https://dex-tracker.herokuapp.com/sprites/bw/${pokemon.name}.gif"
        else
            "https://dex-tracker.herokuapp.com/sprites/$gameKey/${pokemon.name}.png"*/

        val url = "https://dex-tracker.herokuapp.com/sprites/bw/${pokemon.name}.gif"

        holder.name.text   = pokemon.name
        anim(holder.name, 200)
        holder.number.text = "pokedex number: ${pokemon.nationalPokedexNumber}"
        anim(holder.number,400)
        holder.primAb.text = "primary ability: ${pokemon.primaryAbility}"
        anim(holder.primAb,600)
        if (pokemon.secondaryAbility != null){
            holder.secAb.text = "secondary ability: ${pokemon.secondaryAbility}"
            anim(holder.secAb, 800)
        }
        else holder.secAb.visibility = View.GONE
        if (pokemon.hiddenAbility != null){
            holder.hidAb.text = "hidden ability: ${pokemon.hiddenAbility}"
            anim(holder.hidAb,1000)
        }
        else holder.hidAb.visibility = View.GONE
        holder.gen.text = "Generation: ${pokemon.gen}"
        anim(holder.gen,1200)

        anim(holder.image,200)
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

    override fun getItemCount(): Int = 1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = itemView.findViewById(R.id.pokename)
        val number: TextView = itemView.findViewById(R.id.pokenationalPokedexNumber)
        val primAb: TextView = itemView.findViewById(R.id.pokeprimaryAbility)
        val secAb: TextView = itemView.findViewById(R.id.pokesecondaryAbility)
        val hidAb: TextView = itemView.findViewById(R.id.pokehiddenAbility)
        val gen: TextView = itemView.findViewById(R.id.pokegen)
        val image: ImageView = itemView.findViewById(R.id.pokeimage)
    }
}
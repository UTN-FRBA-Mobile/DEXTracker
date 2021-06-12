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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.Pokemon

class PokemonFormsAdapter(
    private val pokemon: Pokemon,
    private val context: Context,
) : RecyclerView.Adapter<PokemonFormsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_poke_form, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val form = pokemon.forms[position]
        /*var gameKey = game.name.takeWhile { it != '-' }
        gameKey.replace("b2w2","bw").also { gameKey = it }
        gameKey.replace("dppt","dp").also { gameKey = it }
        var url = "https://dex-tracker.herokuapp.com/sprites/$gameKey/${form.name}.png"
        if(gameKey == "bw")
            url.replaceAfterLast(".","gif").also { url = it }*/

        anim(holder.forms, 1200)
        anim(holder.image, 1300)
        anim(holder.name, 1400)

        holder.name.text = form.name
        val url = "https://dex-tracker.herokuapp.com/sprites/bw/${form.name}.gif"
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

    override fun getItemCount(): Int = pokemon.forms.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val forms: TextView = itemView.findViewById(R.id.pokeforms)
        val image: ImageView = itemView.findViewById(R.id.pokeimage)
        val name: TextView = itemView.findViewById(R.id.pokeformname)
    }
}
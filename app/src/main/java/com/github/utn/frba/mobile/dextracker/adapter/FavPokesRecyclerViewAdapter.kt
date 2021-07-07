package com.github.utn.frba.mobile.dextracker.adapter

import android.net.Uri
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.utn.frba.mobile.dextracker.FavPokes_Fragment
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.Favourite
import com.github.utn.frba.mobile.dextracker.data.Game
import com.github.utn.frba.mobile.dextracker.data.UserDex
import com.github.utn.frba.mobile.dextracker.repository.inMemoryRepository
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.properties.Delegates

class FavPokesRecyclerViewAdapter(
    private val openPokemonInfo: (String, Game) -> Unit,
) : RecyclerView.Adapter<FavPokesRecyclerViewAdapter.ViewHolder>() {

    var searchText: String by Delegates.observable("") { _, _, new ->
        filter(new)
    }

    private var dataset: MutableList<Favourite> = mutableListOf()
    var fullDataset: List<Favourite> = emptyList()

    fun add(favourite: List<Favourite>) {
        dataset.addAll(favourite)
        fullDataset = favourite
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_fav_pokes_, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = dataset[position]
        holder.name = p.species
        holder.gen = p.gen

        val callResponse = dexTrackerService.fetchUserDex(userId = inMemoryRepository.session.userId, dexId = p.dexId)

        callResponse.enqueue(object : Callback<UserDex> {
            override fun onResponse(call: Call<UserDex>, response: Response<UserDex>) {
                response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let {
                            holder.game = it.game
                            val url = "https://dex-tracker.herokuapp.com/icons/gen${holder.game.gen}/${p.species}.png"

                            Picasso.get()
                                    .load(Uri.parse(url))
                                    .placeholder(R.drawable.placeholder_pokeball)
                                    .error(R.drawable.placeholder)
                                    .into(holder.imageView, object : com.squareup.picasso.Callback {
                                        override fun onSuccess() {
                                            //set animations here
                                        }

                                        override fun onError(e: java.lang.Exception?) {
                                            Log.e(
                                                    TAG,
                                                    "Respuesta invalida al intentar cargar la imagen de gen${holder.game.gen}/${p.species}",
                                                    e,
                                            )
                                        }
                                    })
                        }
                        ?: Log.e(
                                TAG,
                                "ononono falló el servicio perro: ${response.code()}, ${response.body()}",
                        )
            }

            override fun onFailure(call: Call<UserDex>, t: Throwable) {
                Log.e(TAG, "ononon se rompió algo perro", t)
            }
        })

    }

    override fun getItemCount(): Int = dataset.size

    private fun filter(search: String) {
        dataset = if (search == "") {
            fullDataset.toMutableList()
        } else {
            fullDataset.filter {
                it.species
                        .toLowerCase(Locale.getDefault())
                        .contains(
                                search.toLowerCase(Locale.getDefault())
                        ) || it.gen.toString()
                        .toLowerCase(Locale.getDefault())
                        .startsWith(search.toLowerCase(Locale.getDefault()))
            }
                    .toMutableList()
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = itemView.findViewById(R.id.imageFAVPOKES)
        private val nameView: TextView = itemView.findViewById(R.id.item_nameFAVPOKES)
        private val genView: TextView = itemView.findViewById(R.id.item_genFAVPOKES)

        var name: String by Delegates.observable("") { _, _, new ->
            nameView.text = new.capitalize(Locale.getDefault())
        }
        var gen: Int by Delegates.observable(0) { _, _, new ->
            genView.text = pad(new)
        }
        lateinit var game: Game

        init {
            itemView.setOnClickListener {
                openPokemonInfo(this.name, this.game)
            }
        }

        private fun pad(n: Int) = when (n) {
            in 1..9 -> "Gen 00$n"
            in 10..100 -> "Gen 0$n"
            else -> "Gen $n"
        }

    }

    companion object {
        private const val TAG = "FAV_POKES_ADAPTER"
    }
}
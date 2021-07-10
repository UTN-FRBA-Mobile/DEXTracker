package com.github.utn.frba.mobile.dextracker.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.data.Subscription
import com.github.utn.frba.mobile.dextracker.extensions.percentageOf
import com.github.utn.frba.mobile.dextracker.model.PokedexRef
import com.github.utn.frba.mobile.dextracker.repository.inMemoryRepository
import java.util.*
import kotlin.properties.Delegates

class FavDEXRecyclerViewAdapter(
        private var dex: List<PokedexRef>,
        private var sub: Set<Subscription>,
        private val onClick: (String, String) -> Unit,
) : RecyclerView.Adapter<FavDEXRecyclerViewAdapter.ViewHolder>() {
    private val originalDex = dex
    var searchText: String by Delegates.observable("") { _, _, new ->
        filter(new)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_dex_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val d = dex[position]
        holder.title = d.game.displayName
        holder.completion = d.caught to d.total
        holder.completionPercentage = d.caught percentageOf d.total
        holder.completionPercentage = (d.caught.toDouble() / d.total.toDouble() * 100).toInt()
        holder.dexId = d.id
        holder.userId = sub.find { it.dexId == d.id }!!.userId
    }

    private fun filter(search: String) {
        dex = if (search == "") {
            originalDex
        } else {
            originalDex.filter {
                it.game.displayName
                    .toLowerCase(Locale.getDefault())
                    .contains(search.toLowerCase(Locale.getDefault()))
            }
        }
        notifyDataSetChanged()
    }

    fun add(d: PokedexRef) {
        this.dex = dex + d
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dex.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.my_dex_card)
        private val iconView: ImageView = itemView.findViewById(R.id.my_dex_icon)
        private val titleView: TextView = itemView.findViewById(R.id.my_dex_title)
        private val completionPercentageView: TextView = itemView.findViewById(
                R.id.my_dex_completion_percentage
        )
        private val completionBarView: ProgressBar =
                itemView.findViewById(R.id.my_dex_completion_bar)
        private val completionView: TextView = itemView.findViewById(R.id.my_dex_completion)

        var title: String by Delegates.observable("") { _, _, new -> titleView.text = new }
        var completionPercentage: Int by Delegates.observable(0) { _, _, new ->
            completionBarView.progress = new
            completionPercentageView.text = "$new%"
        }

        var completion: Pair<Int, Int> by Delegates.observable(0 to 0) { _, _, (caught, total) ->
            completionView.text = "$caught/$total"
        }

        lateinit var dexId: String
        lateinit var userId: String

        init {
            cardView.setOnClickListener {
                Log.i(TAG, "Clicked on $title")
                onClick(dexId, userId)
            }
        }
    }

    companion object {
        private const val TAG = "FAV_DEX"
    }
}
package com.github.utn.frba.mobile.dextracker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.model.PokedexRef
import com.github.utn.frba.mobile.dextracker.repository.InMemoryRepository
import kotlin.properties.Delegates

class MyDexAdapter(
    private val dex: List<PokedexRef>,
    private val onClick: (String, String) -> Unit,
) : RecyclerView.Adapter<MyDexAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.my_dex_card, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val d = dex[position]
        holder.title = d.game.displayName
        holder.completion = d.caught to d.total
        holder.completionPercentage = d.caught / d.total
        holder.dexId = d.id
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
            completionBarView.secondaryProgress = new
            completionPercentageView.text = "$new%"
        }
        var completion: Pair<Int, Int> by Delegates.observable(0 to 0) { _, _, (caught, total) ->
            completionView.text = "$caught/$total"
        }

        lateinit var dexId: String

        init {
            cardView.setOnClickListener {
                Log.i(TAG, "Clicked on $title")
                onClick(dexId, InMemoryRepository.session.userId)
            }
        }
    }

    companion object {
        private const val TAG = "MY_DEX"
    }
}
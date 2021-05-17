package com.github.utn.frba.mobile.dextracker

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.github.utn.frba.mobile.dextracker.dummy.DummyContent.DummyItem

/**
 * [RecyclerView.Adapter] that can display a [DummyItem].
 * TODO: Replace the implementation with code for your data type.
 */
/*class InfoPokeRecyclerViewAdapter(
    private val listener: InfoPokeFragment.OnFragmentInteractionListener?,
    private val poke: Pokemon
    ): RecyclerView.Adapter<InfoPokeRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_mis_dex_, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = position
        holder.idView.text = item.id
        holder.contentView.text = item.content
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.findViewById(R.id.item_numberMISDEX)
        val contentView: TextView = view.findViewById(R.id.contentMISDEX)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}
*/
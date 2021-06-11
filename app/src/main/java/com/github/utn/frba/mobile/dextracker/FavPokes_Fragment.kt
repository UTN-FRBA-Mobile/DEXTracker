package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.utn.frba.mobile.dextracker.adapter.FavPokesRecyclerViewAdapter
import com.github.utn.frba.mobile.dextracker.dummy.DummyContent
import com.github.utn.frba.mobile.dextracker.model.Session

/**
 * A fragment representing a list of Items.
 */
class FavPokes_Fragment : Fragment() {

    private lateinit var session: Session
    private lateinit var recyclerView: RecyclerView
    private lateinit var favPokesAdapter: FavPokesRecyclerViewAdapter
    private var columnCount = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fav_pokes_list, container, false)
        //val searchView = view.findViewById<SearchView>(R.id.searchFAVPOKES)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                favPokesAdapter = FavPokesRecyclerViewAdapter(DummyContent.ITEMS)
            }
        }
        return view/*.also {
            it.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchFAVPOKES)
                .setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        favPokesAdapter.searchText = query ?: ""
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        favPokesAdapter.searchText = newText ?: ""
                        return true
                    }
                })
        }*/
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            FavPokes_Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
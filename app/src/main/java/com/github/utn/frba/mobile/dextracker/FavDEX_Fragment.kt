package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.adapter.FavDEXRecyclerViewAdapter
import com.github.utn.frba.mobile.dextracker.extensions.replaceWith
import com.github.utn.frba.mobile.dextracker.model.Session
import com.github.utn.frba.mobile.dextracker.repository.inMemoryRepository

/**
 * A fragment representing a list of Items.
 */
class FavDEX_Fragment : Fragment() {

    private lateinit var session: Session
    private lateinit var recyclerView: RecyclerView
    private lateinit var favDEXAdapter: FavDEXRecyclerViewAdapter
    private var columnCount = 2

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
        val view = inflater.inflate(R.layout.fragment_fav_dex_list, container, false)
        //val searchView = view.findViewById<SearchView>(R.id.searchFAVDEX)
        // Set the adapter
        session = inMemoryRepository.session
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                favDEXAdapter = FavDEXRecyclerViewAdapter(session.pokedex) { dexId, userId ->
                    replaceWith(
                        R.id.fl_wrapper,
                        PokedexFragment.newInstance(
                            userId = userId,
                            dexId = dexId,
                        ),
                    )
                }
            }
        }
        return view/*.also {
            it.findViewById<androidx.appcompat.widget.SearchView>(R.id.searchFAVDEX)
                .setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        favDEXAdapter.searchText = query ?: ""
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        favDEXAdapter.searchText = newText ?: ""
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
            FavDEX_Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
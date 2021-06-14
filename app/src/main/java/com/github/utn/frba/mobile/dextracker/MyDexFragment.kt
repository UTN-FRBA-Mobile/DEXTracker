package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.adapter.MyDexAdapter
import com.github.utn.frba.mobile.dextracker.extensions.replaceWith
import com.github.utn.frba.mobile.dextracker.extensions.replaceWithAnimWith
import com.github.utn.frba.mobile.dextracker.model.Session
import com.github.utn.frba.mobile.dextracker.repository.InMemoryRepository

class MyDexFragment : Fragment() {
    private lateinit var session: Session
    private lateinit var recyclerView: RecyclerView
    private lateinit var myDexAdapter: MyDexAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        session = InMemoryRepository.session
        return inflater.inflate(R.layout.fragment_my_dex, container, false).also {
            recyclerView = it.findViewById(R.id.my_dex_recycler_view)
            myDexAdapter = MyDexAdapter(session.pokedex) { dexId, userId ->
                replaceWithAnimWith(
                    R.id.fl_wrapper,
                    PokedexFragment.newInstance(
                        userId = userId,
                        dexId = dexId,
                    ),
                    enter   = R.anim.fragment_open_enter,
                    exit    = R.anim.fragment_open_exit,
                    popEnter= R.anim.fragment_open_enter,
                    popExit = R.anim.fragment_open_exit,
                )
            }

            it.findViewById<SearchView>(R.id.my_dex_search_bar)
                .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        myDexAdapter.searchText = query ?: ""
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        myDexAdapter.searchText = newText ?: ""
                        return true
                    }
                })

            recyclerView.adapter = myDexAdapter

            val layoutManager = GridLayoutManager(context, 1)
            recyclerView.layoutManager = layoutManager
        }
    }
}
package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.adapter.MyDexAdapter
import com.github.utn.frba.mobile.dextracker.model.Session
import com.github.utn.frba.mobile.dextracker.repository.InMemoryRepository

class MyDexFragment : Fragment() {
    private lateinit var session: Session
    private lateinit var recyclerView: RecyclerView
    private lateinit var myDexAdapter: MyDexAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = InMemoryRepository.session
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_my_dex, container, false).also {
            recyclerView = it.findViewById(R.id.my_dex_recycler_view)
            myDexAdapter = MyDexAdapter(session.pokedex)
            recyclerView.adapter = myDexAdapter

            val layoutManager = GridLayoutManager(context, 1)
            recyclerView.layoutManager = layoutManager
        }
    }
}
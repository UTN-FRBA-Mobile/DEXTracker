package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.adapter.FavDEXRecyclerViewAdapter
import com.github.utn.frba.mobile.dextracker.adapter.MyDexAdapter
import com.github.utn.frba.mobile.dextracker.data.UserDex
import com.github.utn.frba.mobile.dextracker.extensions.replaceWith
import com.github.utn.frba.mobile.dextracker.extensions.replaceWithAnimWith
import com.github.utn.frba.mobile.dextracker.model.PokedexRef
import com.github.utn.frba.mobile.dextracker.model.Session
import com.github.utn.frba.mobile.dextracker.repository.inMemoryRepository
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavDEX_Fragment : Fragment() {

    private lateinit var session: Session
    private lateinit var recyclerView: RecyclerView
    private lateinit var favDEXAdapter: FavDEXRecyclerViewAdapter
    val dex: MutableList<PokedexRef> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = inMemoryRepository.session
        fillDex()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_fav_dex_list, container, false).also {
            recyclerView = it.findViewById(R.id.fav_dex_recycler_view)

            favDEXAdapter = FavDEXRecyclerViewAdapter(dex, session.subscriptions) { dexId, userId ->
                replaceWithAnimWith(
                        R.id.fl_wrapper,
                        PokedexFragment.newInstance(
                                userId = userId,
                                dexId = dexId,
                        ),
                        enter = R.anim.fragment_open_enter,
                        exit = R.anim.fragment_open_exit,
                        popEnter = R.anim.fragment_open_enter,
                        popExit = R.anim.fragment_open_exit,
                )
            }

            it.findViewById<SearchView>(R.id.searchFAVDEX)
                    .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            favDEXAdapter.searchText = query ?: ""
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            favDEXAdapter.searchText = newText ?: ""
                            return true
                        }
                    })

            recyclerView.adapter = favDEXAdapter

            val layoutManager = GridLayoutManager(context, 1)
            recyclerView.layoutManager = layoutManager
        }
    }

    private fun fillDex() {
        session.subscriptions.forEach {
            fetchDex(
                    userId = it.userId,
                    dexId = it.dexId
            )
        }
    }

    private fun fetchDex(userId: String, dexId: String) {
        val callResponse = dexTrackerService.fetchUserDex(userId = userId, dexId = dexId)

        callResponse.enqueue(object : Callback<UserDex> {
            override fun onResponse(call: Call<UserDex>, response: Response<UserDex>) {
                response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let {
                            dex.add(PokedexRef(it))
                            replaceWith(
                                    R.id.fl_wrapper,
                                    newInstance()
                            )
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

    companion object {
        private const val TAG = "FAV_DEX"

        @JvmStatic
        fun newInstance() = FavDEX_Fragment()

    }
}
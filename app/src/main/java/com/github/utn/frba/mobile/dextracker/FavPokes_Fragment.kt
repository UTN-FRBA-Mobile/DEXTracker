package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import com.github.utn.frba.mobile.dextracker.adapter.FavPokesRecyclerViewAdapter
import com.github.utn.frba.mobile.dextracker.data.Favourite
import com.github.utn.frba.mobile.dextracker.data.Game
import com.github.utn.frba.mobile.dextracker.data.User
import com.github.utn.frba.mobile.dextracker.extensions.replaceWithAnimWith
import com.github.utn.frba.mobile.dextracker.model.Session
import com.github.utn.frba.mobile.dextracker.repository.inMemoryRepository
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val USER_ID = "userId"

class FavPokes_Fragment : Fragment() {

    private lateinit var session: Session
    private lateinit var recyclerView: RecyclerView
    private lateinit var favPokesAdapter: FavPokesRecyclerViewAdapter
    private lateinit var userId: String
    private lateinit var loader: ProgressBar
    private lateinit var searchView: SearchView
    private lateinit var user: User
    lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        session = inMemoryRepository.session
        userId = session.userId
        return inflater.inflate(R.layout.fragment_fav_pokes_list, container, false).also {
            recyclerView = it.findViewById(R.id.listFAVPOKES)
            favPokesAdapter = FavPokesRecyclerViewAdapter(
                    openPokemonInfo = { p, g ->
                        replaceWithAnimWith(
                                resourceId = R.id.fl_wrapper,
                                other = PokemonInfoFragment.newInstance(
                                        game = g,
                                        pokemon = p,
                                        favourite = true
                                ).also { frag ->
                                    frag.addFavourite = { pok ->
                                        favPokesAdapter.fullDataset = favPokesAdapter.fullDataset + Favourite(
                                                species = pok.name,
                                                gen = pok.gen,
                                                dexId = "",
                                        )
                                    }
                                    frag.removeFavourite = { pok ->
                                        favPokesAdapter.fullDataset = favPokesAdapter.fullDataset.filterNot { p -> p.species == pok.name }
                                    }
                                },
                                enter = R.anim.fade_enter_long,
                                exit = R.anim.fragment_fade_exit,
                                popEnter = R.anim.fragment_open_enter,
                                popExit = R.anim.fragment_open_exit,
                        )
                    }
            )
            recyclerView.adapter = favPokesAdapter
            loader = it.findViewById(R.id.pokedex_spinner)

            val layoutManager = GridLayoutManager(context, 3)
            recyclerView.layoutManager = layoutManager
            recyclerView.setPadding(32, 128, 32, 0)

            searchView = it.findViewById<SearchView>(R.id.searchFAVPOKES).apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        favPokesAdapter.searchText = query ?: ""
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        favPokesAdapter.searchText = newText ?: ""
                        return true
                    }
                })
            }

            fetchUser()
        }
    }

    private fun fetchUser() {
        val callResponse = dexTrackerService.fetchUser(userId = userId)

        callResponse.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                loader.visibility = View.GONE
                response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.run {
                            user = this
                            favPokesAdapter.add(user.favourites)
                        }
                        ?: Log.e(
                                TAG,
                                "Error en la respuesta de la data del usuario: ${response.code()}, ${response.body()}",
                        )
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(TAG, "Error al intentar obtener data del usuario", t)
            }
        })
    }

    companion object {
        private const val TAG = "FAV_POKES"

        @JvmStatic
        fun newInstance(userId: String, dexId: String) = FavPokes_Fragment().apply {
            arguments = Bundle().apply {
                putString(USER_ID, userId)
            }
        }
    }
}
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
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.adapter.UserDexAdapter
import com.github.utn.frba.mobile.dextracker.data.DexUpdateDTO
import com.github.utn.frba.mobile.dextracker.data.UpdateUserDTO
import com.github.utn.frba.mobile.dextracker.data.User
import com.github.utn.frba.mobile.dextracker.data.UserDex
import com.github.utn.frba.mobile.dextracker.extensions.replaceWithAnimWith
import com.github.utn.frba.mobile.dextracker.repository.InMemoryRepository
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

private const val USER_ID = "userId"
private const val DEX_ID = "dexId"

class PokedexFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userDexAdapter: UserDexAdapter
    private lateinit var userId: String
    private lateinit var dexId: String
    private lateinit var spinner: ProgressBar
    private lateinit var searchView: SearchView
    private lateinit var userDex: UserDex
    private var isEditing: Boolean by Delegates.observable(false) { _, _, new ->
        userDexAdapter.isEditing = new
        searchView.visibility = if (new) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID)!!
            dexId = it.getString(DEX_ID)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.pokedex_fragment, container, false).also {
            recyclerView = it.findViewById(R.id.pokedex_recycler_view)
            userDexAdapter = UserDexAdapter(
                canEdit = InMemoryRepository.session.userId == userId,
                openEditor = { isEditing = true },
                openPokemonInfo = { p ->
                    replaceWithAnimWith(
                        resourceId  = R.id.fl_wrapper,
                        other       = PokemonInfoFragment.newInstance(
                                            game = userDex.game,
                                            pokemon = p,
                                      ),
                        enter   = R.anim.fade_enter_long,
                        exit    = R.anim.fragment_fade_exit,
                        popEnter= R.anim.fragment_open_enter,
                        popExit = R.anim.fragment_open_exit,
                    )
                }
            )
            recyclerView.adapter = userDexAdapter
            spinner = it.findViewById(R.id.pokedex_spinner)

            val layoutManager = GridLayoutManager(context, 3)
            recyclerView.layoutManager = layoutManager
            recyclerView.setPadding(32, 128, 32, 0)

            searchView = it.findViewById<SearchView>(R.id.pokedex_search_bar).apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        userDexAdapter.searchText = query ?: ""
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        userDexAdapter.searchText = newText ?: ""
                        return true
                    }
                })
            }
            fetchPokedex()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val callResponse = dexTrackerService.updateUser(
            userId = this.userId,
            updateUser = UpdateUserDTO(
                username = null,
                dex = mapOf(dexId to DexUpdateDTO(
                    name = null,
                    caught = userDexAdapter.fullDataset.filter { it.caught }
                        .map { it.dexNumber }
                ))
            ),
            token = InMemoryRepository.session.dexToken,
        )

        val updatedDex = userDex.copy(
            caught = userDexAdapter.fullDataset.count { it.caught },
            pokemon = userDexAdapter.fullDataset,
        )

        InMemoryRepository.merge(dexId = dexId, dex = updatedDex)

        callResponse.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (!response.isSuccessful) {
                    Log.w(TAG, "onono fallo actualizar el user doggo")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(TAG, "ononon se rompió algo perro", t)
            }
        })
    }

    private fun fetchPokedex() {
        val callResponse = dexTrackerService.fetchUserDex(userId = userId, dexId = dexId)

        callResponse.enqueue(object : Callback<UserDex> {
            override fun onResponse(call: Call<UserDex>, response: Response<UserDex>) {
                spinner.visibility = View.GONE
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.run {
                        userDex = this
                        userDexAdapter.add(this.pokemon,this.game.name)
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
        private const val TAG = "USER_DEX"

        @JvmStatic
        fun newInstance(userId: String, dexId: String) = PokedexFragment().apply {
            arguments = Bundle().apply {
                putString(USER_ID, userId)
                putString(DEX_ID, dexId)
            }
        }
    }
}
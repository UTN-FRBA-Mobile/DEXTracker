package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.adapter.UserDexAdapter
import com.github.utn.frba.mobile.dextracker.data.*
import com.github.utn.frba.mobile.dextracker.extensions.replaceWith
import com.github.utn.frba.mobile.dextracker.extensions.replaceWithAnimWith
import com.github.utn.frba.mobile.dextracker.repository.inMemoryRepository
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

private const val USER_ID = "userId"
private const val DEX_ID = "dexId"

class PokedexFragment private constructor() : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userDexAdapter: UserDexAdapter
    private lateinit var userId: String
    private lateinit var dexId: String
    private lateinit var spinner: ProgressBar
    private lateinit var searchView: SearchView
    private lateinit var shareView: ImageButton
    private lateinit var userDex: UserDex
    private lateinit var compareButton: Button
    private val favourites: MutableList<Favourite> = mutableListOf()
    private var ownsDex: Boolean = false
    private var isEditing: Boolean by Delegates.observable(false) { _, _, new ->
        userDexAdapter.isEditing = new
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID)!!
            dexId = it.getString(DEX_ID)!!
            ownsDex = inMemoryRepository.session.userId == userId
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
                canEdit = ownsDex,
                openEditor = { isEditing = true },
                openPokemonInfo = { p ->
                    replaceWithAnimWith(
                        resourceId = R.id.fl_wrapper,
                        other = PokemonInfoFragment.newInstance(
                            game = userDex.game,
                            pokemon = p,
                        ).also { frag ->
                            frag.onFavourite = { pok ->
                                favourites.add(
                                    Favourite(
                                        species = pok.name,
                                        gen = userDex.game.gen,
                                        dexId = dexId,
                                    )
                                )
                            }
                        },
                        enter = R.anim.fade_enter_long,
                        exit = R.anim.fragment_fade_exit,
                        popEnter = R.anim.fragment_open_enter,
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

            compareButton = it.findViewById(R.id.compare_button)
            if (ownsDex)
                shareView = it.findViewById<ImageButton>(R.id.share).apply{
                    visibility = View.VISIBLE
                    setOnClickListener {
                        replaceWithAnimWith(
                            resourceId  = R.id.fl_wrapper,
                            other       = ShareDexFragment.newInstance(
                                userId = userId,
                                dexId = dexId,
                            ),
                            enter   = R.anim.fragment_open_enter,
                            exit    = R.anim.fragment_fade_exit,
                            popEnter= R.anim.fragment_open_enter,
                            popExit = R.anim.fragment_fade_exit,
                        )
                    }
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
                dex = mapOf(
                    dexId to DexUpdateDTO(
                        name = null,
                        caught = userDexAdapter.fullDataset.filter { it.caught }
                            .map { it.dexNumber },
                    ),
                ),
                favourites = inMemoryRepository.apply {
                    merge(
                        dexId,
                        favourites,
                    )
                }.session.favourites,
            ),
            token = inMemoryRepository.session.dexToken,
        )

        val updatedDex = userDex.copy(
            caught = userDexAdapter.fullDataset.count { it.caught },
            pokemon = userDexAdapter.fullDataset,
        )

        inMemoryRepository.merge(dexId = dexId, dex = updatedDex)

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
                    ?.let {
                        userDex = it
                        userDexAdapter.add(it.pokemon)
                        userDexAdapter.game = it.game
                        initializeCompareButton(it)
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

    private fun initializeCompareButton(userDex: UserDex) {
        val comparableDexes = inMemoryRepository.session.pokedex
            .filter { dex -> dex.game.gen == userDex.game.gen }
        if (!ownsDex && comparableDexes.isNotEmpty()) compareButton.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                val popup = PopupMenu(this.context, it)
                popup.inflate(R.menu.menu_compare)
                popup.apply {
                    menu.clear()

                    comparableDexes.forEachIndexed { i, dex ->
                        menu.add(Menu.NONE, i, i, dex.name ?: dex.game.displayName)
                    }

                    setOnMenuItemClickListener { i ->
                        val dex = comparableDexes[i.itemId]
                        replaceWith(
                            resourceId = R.id.fl_wrapper,
                            other = DexDiffFragment.newInstance(
                                leftUserId = inMemoryRepository.session.userId,
                                leftUserDexId = dex.id,
                                rightUserId = userId,
                                rightUserDexId = dexId,
                            ),
                        )
                        true
                    }

                    show()
                }
            }
        }
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

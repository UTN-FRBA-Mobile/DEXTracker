package com.github.utn.frba.mobile.dextracker

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.adapter.MyDexAdapter
import com.github.utn.frba.mobile.dextracker.data.*
import com.github.utn.frba.mobile.dextracker.extensions.replaceWithAnimWith
import com.github.utn.frba.mobile.dextracker.model.PokedexRef
import com.github.utn.frba.mobile.dextracker.model.Session
import com.github.utn.frba.mobile.dextracker.repository.inMemoryRepository
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyDexFragment : Fragment() {
    private lateinit var session: Session
    private lateinit var recyclerView: RecyclerView
    private lateinit var myDexAdapter: MyDexAdapter
    private val dexList: MutableList<String> = mutableListOf()
    private val games: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = inMemoryRepository.session
        fetchGames()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
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

            it.findViewById<FloatingActionButton>(R.id.add_dex)
                    .setOnClickListener {
                        val dialogView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_my_dex, null)
                        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                dexList
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        val spinner = dialogView.findViewById<Spinner>(R.id.list_dex)
                        spinner.adapter = adapter
                        val builder = AlertDialog.Builder(context)
                                .setView(dialogView)
                                .setTitle("New PokeDex")
                        val alertDialog = builder.show()
                        dialogView.findViewById<Button>(R.id.accept).setOnClickListener {
                            alertDialog.dismiss()
                            val name = dialogView.findViewById<EditText>(R.id.name_dex).text.toString()
                            val game = spinner.selectedItemId.toInt()
                            newDex(
                                    game = games[game],
                                    name = name
                            )
                        }
                        dialogView.findViewById<Button>(R.id.cancel).setOnClickListener{
                            alertDialog.dismiss()
                        }
                    }
        }
    }

    private fun fetchGames() {
        val callResponse = dexTrackerService.fetchPokedex()
        callResponse.enqueue(object : Callback<List<Pokedex>> {
            override fun onResponse(call: Call<List<Pokedex>>, response: Response<List<Pokedex>>) {
                response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let {
                            for(d in it){
                                dexList.add(d.displayName)
                                games.add(d.name)
                            }
                        }
                        ?: Log.e(
                                TAG,
                                "Error en la respuesta de las pokedex disponibles: ${response.code()}, ${response.body()}",
                        )
            }

            override fun onFailure(call: Call<List<Pokedex>>, t: Throwable) {
                Log.e(TAG, "Error al intentar obtener las pokedex disponibles", t)
            }
        })
    }

    private fun newDex(game: String, name: String?) {
        val callResponse = dexTrackerService.newDex(
                userId = session.userId,
                newDex = DexRequest(
                        game = game,
                        name = name
                ),
                token = session.dexToken
        )
        callResponse.enqueue(object : Callback<UserDex> {
            override fun onResponse(call: Call<UserDex>, response: Response<UserDex>) {
                response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let {
                            //recargar fragment
                            inMemoryRepository.merge(dex = it)
                            replaceWithAnimWith(
                                    R.id.fl_wrapper,
                                    newInstance(),
                                    enter   = R.anim.fragment_open_enter,
                                    exit    = R.anim.fragment_open_exit,
                                    popEnter= R.anim.fragment_open_enter,
                                    popExit = R.anim.fragment_open_exit,
                            )
                        }
                        ?: Log.e(
                                TAG,
                                "Error en la respuesta de la nueva pokedex: ${response.code()}, ${response.body()}",
                        )
            }

            override fun onFailure(call: Call<UserDex>, t: Throwable) {
                Log.e(TAG, "Error al intentar cargar la nueva pokedex", t)
            }
        })
    }

    companion object {
        private const val TAG = "MY_DEX"

        @JvmStatic
        fun newInstance() = MyDexFragment()

    }
}
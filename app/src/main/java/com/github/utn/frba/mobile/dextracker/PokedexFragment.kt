package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.adapter.UserDexAdapter
import com.github.utn.frba.mobile.dextracker.data.UserDex
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val USER_ID = "userId"
private const val DEX_ID = "dexId"

class PokedexFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userDexAdapter: UserDexAdapter
    private lateinit var userId: String
    private lateinit var dexId: String
    private lateinit var spinner: ProgressBar

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
            userDexAdapter = UserDexAdapter()
            recyclerView.adapter = userDexAdapter
            spinner = it.findViewById(R.id.pokedex_spinner)

            val layoutManager = GridLayoutManager(context, 3)
            recyclerView.layoutManager = layoutManager
            recyclerView.setPadding(32, 128, 32, 0)

            fetchPokedex()
        }
    }

    private fun fetchPokedex() {
        val callResponse = dexTrackerService.fetchUserDex(userId = userId, dexId = dexId)

        callResponse.enqueue(object : Callback<UserDex> {
            override fun onResponse(call: Call<UserDex>, response: Response<UserDex>) {
                spinner.visibility = View.GONE
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.run { userDexAdapter.add(this.pokemon) }
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
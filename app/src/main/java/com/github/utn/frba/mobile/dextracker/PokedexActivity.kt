package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.adapter.UserDexAdapter
import com.github.utn.frba.mobile.dextracker.data.UserDex
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokedexActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userDexAdapter: UserDexAdapter
    private lateinit var userId: String
    private lateinit var dexId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokedex)

        recyclerView = findViewById(R.id.pokedex_recycler_view)
        userDexAdapter = UserDexAdapter()
        recyclerView.adapter = userDexAdapter

        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.setPadding(32, 128, 32, 0)

        fetchPokedex()
    }

    private fun fetchPokedex() {
        val callResponse = dexTrackerService.fetchUserDex(userId = userId, dexId = dexId)

        callResponse.enqueue(object : Callback<UserDex> {
            override fun onResponse(call: Call<UserDex>, response: Response<UserDex>) {
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
    }
}
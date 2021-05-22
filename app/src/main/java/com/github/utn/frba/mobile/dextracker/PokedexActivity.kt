package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.adapter.UserDexAdapter
import com.github.utn.frba.mobile.dextracker.data.UserDex
import com.github.utn.frba.mobile.dextracker.service.DexTrackerService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokedexActivity : AppCompatActivity() {
    private lateinit var retrofit: Retrofit
    private lateinit var recyclerView: RecyclerView
    private lateinit var userDexAdapter: UserDexAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokedex)

        retrofit = Retrofit.Builder()
            .baseUrl("https://dex-tracker.herokuapp.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        recyclerView = findViewById(R.id.pokedex_recycler_view)
        userDexAdapter = UserDexAdapter(this)
        recyclerView.adapter = userDexAdapter

        val layoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = layoutManager
        recyclerView.setPadding(32, 128, 32, 0)

        fetch()
    }

    private fun fetch() {
        val service = retrofit.create(DexTrackerService::class.java)
        val callResponse = service.fetchUserDex(userId = userId, dexId = dexId)

        callResponse.enqueue(object : Callback<UserDex> {
            override fun onResponse(call: Call<UserDex>, response: Response<UserDex>) {
                response.takeIf { it.isSuccessful }
                    ?.let { response.body() }
                    ?.run {
                        Log.i(TAG, this.toString())
                        userDexAdapter.add(this.pokemon)
                    }
                    ?: Log.e(TAG, "ononono falló el servicio perro: ${response.code()}")
            }

            override fun onFailure(call: Call<UserDex>, t: Throwable) {
                Log.e(TAG, "ononon se rompió algo perro", t)
            }
        })
    }

    companion object {
        private const val TAG = "USER_DEX"
        private const val userId = "U-2021-02-13-ddf9d418-d114-435b-b901-69f57223dca4"
        private const val dexId = "UD-2021-04-18-805b06a5-337c-4941-a860-a5d493b264b9"
    }
}
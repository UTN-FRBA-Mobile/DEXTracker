package com.github.utn.frba.mobile.dextracker

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.dummy.DummyContent
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoPokeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoPokeFragment(
        var game:String,
        var pokemon:String,
    ) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var infoPokeRecyclerViewAdapter: InfoPokeRecyclerViewAdapter
    private lateinit var infoPokeEvoRecyclerViewAdapter: InfoPokeEvoRecyclerViewAdapter
    private lateinit var infoPokeFormsRecyclerViewAdapter: InfoPokeFormsRecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewevo: RecyclerView
    private lateinit var recyclerViewforms: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_info_poke, container, false)
        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerViewevo = view.findViewById(R.id.recyclerviewevo)
        recyclerViewforms = view.findViewById(R.id.recyclerviewforms)
        return view
    }

   override fun onStart() {
        super.onStart()
        val service = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://dex-tracker.herokuapp.com/api/v1/")
            .build()
            .create(InfoPokeService::class.java)

        service.getPoke(game,pokemon).enqueue(object: Callback<GetPokeResponse> {
            override fun onResponse(call: Call<GetPokeResponse>, response: Response<GetPokeResponse>) {
                /*Toast.makeText(activity, "Responde API", Toast.LENGTH_SHORT).show()
                val jsonString = """{"name":"pichu","nationalPokedexNumber":172,"primaryAbility":"static",
                "hiddenAbility":"lightning-rod","evolutions":[{"name":"pikachu","method":{"type":"LEVEL_UP",
                "friendship":220,"upsideDown":false}}],"forms":[{"name": "spiky-eared-pichu"}],"gen": 4}""";
                val testModel = Gson().fromJson(jsonString, Pokemon2::class.java)
                if(220 == testModel.evolutions[0].method.friendship)
                    Log.i(TAG, "pokemon "+Gson().toJson(testModel))
                Log.i(TAG, "poke "+Gson().toJson(response.body()))*/
                if(response.body() != null){
                    infoPokeRecyclerViewAdapter = InfoPokeRecyclerViewAdapter(listener, response.body()!!)
                    with(recyclerView) {
                        layoutManager = LinearLayoutManager(context)
                        adapter = infoPokeRecyclerViewAdapter
                    }
                    infoPokeEvoRecyclerViewAdapter = InfoPokeEvoRecyclerViewAdapter(response.body()!!.evolutions)
                    with(recyclerViewevo) {
                        layoutManager = LinearLayoutManager(context)
                        adapter = infoPokeEvoRecyclerViewAdapter
                    }
                    infoPokeFormsRecyclerViewAdapter = InfoPokeFormsRecyclerViewAdapter(response.body()!!.forms)
                    with(recyclerViewforms) {
                        layoutManager = LinearLayoutManager(context)
                        adapter = infoPokeFormsRecyclerViewAdapter
                    }
                }
                else
                    Toast.makeText(activity, "Pokemon no encontrado", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<GetPokeResponse>, error: Throwable) {
                Toast.makeText(activity, "No responde API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    interface OnFragmentInteractionListener {
        fun showFragment(fragment: Fragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoPoke.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String, game: String, pokemon: String) =
            InfoPokeFragment(game,pokemon).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
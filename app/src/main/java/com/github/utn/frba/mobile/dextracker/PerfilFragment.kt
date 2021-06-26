package com.github.utn.frba.mobile.dextracker

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.utn.frba.mobile.dextracker.data.User
import com.github.utn.frba.mobile.dextracker.model.Session
import com.github.utn.frba.mobile.dextracker.repository.inMemoryRepository
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val USER_ID = "userId"

/**
 * A simple [Fragment] subclass.
 * Use the [PerfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilFragment : Fragment() {
    private lateinit var session: Session
    private lateinit var userId: String
    private lateinit var imageView: ImageView
    private lateinit var nick: TextView
    private lateinit var dex_comp: TextView
    private lateinit var poke_cau: TextView
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        session = inMemoryRepository.session
        userId = session.userId
        return inflater.inflate(R.layout.fragment_perfil, container, false).also {
            imageView   = it.findViewById(R.id.image)
            nick        = it.findViewById(R.id.User)
            nick.animate().apply {
                duration = 1000
                startDelay = 500
                alpha(1F)
                }.start()
            dex_comp    = it.findViewById(R.id.dex_completed)
            dex_comp.animate().apply {
                duration = 1000
                startDelay = 800
                alpha(1F) }.start()
            poke_cau    = it.findViewById(R.id.poke_caught)
            poke_cau.animate().apply {
                duration = 1000
                startDelay = 1000
                alpha(1F) }.start()
        }
    }

    override fun onStart() {
        super.onStart()

        Picasso.get()
                .load(Uri.parse("url"))
                .placeholder(R.drawable.placeholder_perfil)
                .into(imageView, object: com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        //set animations here
                        imageView.animate().apply {
                            duration = 1000
                            alpha(1F)
                        }.start()
                    }
                    override fun onError(e: java.lang.Exception?) {
                        Log.e(TAG, "Respuesta invalida al intentar cargar la imagen de perfil")
                    }
                })

        fetchUser()
    }

    private fun completedDex(){
        var completed = 0
        var pokes: Int
        user.pokedex.forEach{
            pokes = 0
            it.pokemon.forEach {
                pokes = pokes.inc()
            }
            if(it.caught == pokes)
                completed = completed.inc()
        }
        dex_comp.text = "Completed pokedex: ${completed}"
    }

    private fun pokeCaught(){
        var caught = 0
        user.pokedex.forEach { caught = caught.plus(it.caught) }
        poke_cau.text = "Pokemons caught: ${caught}"
    }

    private fun fetchUser() {
        val callResponse = dexTrackerService.fetchUser(userId = userId)

        callResponse.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.run {
                            user = this
                            nick.text =  user.mail.substringBefore("@",user.mail)
                            completedDex()
                            pokeCaught()
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userId Parameter 1.
         * @return A new instance of fragment Perfil.
         */
        private const val TAG = "PROFILE"
        @JvmStatic
        fun newInstance(userId: String, param2: String) =
                PerfilFragment().apply {
                    arguments = Bundle().apply {
                        putString(USER_ID, userId)
                    }
                }
    }
}
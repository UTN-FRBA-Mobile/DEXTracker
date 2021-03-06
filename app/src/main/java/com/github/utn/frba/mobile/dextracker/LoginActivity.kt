package com.github.utn.frba.mobile.dextracker

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.github.utn.frba.mobile.dextracker.async.AsyncCoroutineExecutor
import com.github.utn.frba.mobile.dextracker.constants.RC_SIGN_IN
import com.github.utn.frba.mobile.dextracker.data.LoginRequest
import com.github.utn.frba.mobile.dextracker.data.User
import com.github.utn.frba.mobile.dextracker.databinding.ActivityLoginBinding
import com.github.utn.frba.mobile.dextracker.db.storage.SessionStorage
import com.github.utn.frba.mobile.dextracker.extensions.both
import com.github.utn.frba.mobile.dextracker.model.PokedexRef
import com.github.utn.frba.mobile.dextracker.model.Session
import com.github.utn.frba.mobile.dextracker.repository.inMemoryRepository
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var sessionStorage: SessionStorage
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setTheme(R.style.Theme_DexTracker_NoActionBar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#EFEFEFEF")
        }
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("527824744722-4hoj7bcnjvpa6co3tapt6faj5tbj4uor.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        sessionStorage = SessionStorage(this)
    }

    override fun onStart() {
        super.onStart()

        AsyncCoroutineExecutor.dispatch {
            val session = sessionStorage.get()

            if (session == null) {
                val account = GoogleSignIn.getLastSignedInAccount(this@LoginActivity)

                if (account == null) {
                    Log.i(TAG, "No google account, redirecting to oauth login")
                    signInIntoGoogle()
                } else signInFromOAuth(account)
            } else validateStoredSession(session)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_SIGN_IN -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                    ?: run { throw RuntimeException("onono se rompi?? el login") }
                signInFromOAuth(account)
            }
        }
    }

    private fun signInIntoGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        signInIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun validateStoredSession(session: Session) {
        dexTrackerService.validate(token = session.dexToken)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) redirectToMain(
                        session.copy(
                                pokedex = response.body()!!.pokedex.map { PokedexRef(it) },
                                //subscriptions = response.body()!!.subscriptions
                        )
                    )
                    else {
                        Log.w(
                            TAG,
                            "Login from stored session failed: ${response.code()}, ${response.body()}"
                        )
                        signInIntoGoogle()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(TAG, "ononono se rompi?? la verificaci??n del token perrito", t)
                }
            })
    }

    private fun signInFromOAuth(account: GoogleSignInAccount) {
        val request = LoginRequest(
            mail = account.email!!,
            googleToken = account.idToken!!,
        )

        dexTrackerService.loginFromMail(request)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    response.takeIf { it.isSuccessful }
                        ?.let {
                            it.both(
                                { res -> res.body() },
                                { res -> res.headers().get("dex-token") }
                            )
                        }
                        ?.let { (user, token) ->
                            val session = Session(
                                user = user,
                                token = token,
                            )

                            AsyncCoroutineExecutor.dispatch { sessionStorage.store(session) }

                            redirectToMain(session)
                        } ?: run {
                        Log.e(
                            TAG,
                            "ononon algo se rompi?? perroskie: ${response.body()}, ${response.code()}",
                        )
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(TAG, "ononon se rompi?? algo perro", t)
                }
            })
    }

    private fun redirectToMain(session: Session) {
        inMemoryRepository.session = session
        AsyncCoroutineExecutor.dispatch { sessionStorage.store(session) }
        val intent = Intent(this, MainActivity::class.java)
        //intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY//El main activity tiene que tener backstack
        intent.putExtras(this.intent)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "LOGIN"
    }
}
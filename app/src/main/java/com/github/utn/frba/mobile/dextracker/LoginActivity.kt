package com.github.utn.frba.mobile.dextracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.utn.frba.mobile.dextracker.async.AsyncCoroutineExecutor
import com.github.utn.frba.mobile.dextracker.data.LoginRequest
import com.github.utn.frba.mobile.dextracker.data.User
import com.github.utn.frba.mobile.dextracker.db.storage.SessionStorage
import com.github.utn.frba.mobile.dextracker.extensions.both
import com.github.utn.frba.mobile.dextracker.model.Session
import com.github.utn.frba.mobile.dextracker.repository.InMemoryRepository
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                    ?: run { throw RuntimeException("onono se rompió el login") }
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
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) redirectToMain(session)
                    else {
                        Log.w(
                            TAG,
                            "Login from stored session failed: ${response.code()}, ${response.body()}"
                        )
                        signInIntoGoogle()
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.e(TAG, "ononono se rompió la verificación del token perrito", t)
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
                            "ononon algo se rompió perroskie: ${response.body()}, ${response.code()}",
                        )
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e(TAG, "ononon se rompió algo perro", t)
                }
            })
    }

    private fun redirectToMain(session: Session) {
        InMemoryRepository.session = session
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
    }

    companion object {
        private const val RC_SIGN_IN = 1
        private const val TAG = "LOGIN"
    }
}
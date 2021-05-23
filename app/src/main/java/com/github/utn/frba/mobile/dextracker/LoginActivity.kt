package com.github.utn.frba.mobile.dextracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.utn.frba.mobile.dextracker.data.LoginRequest
import com.github.utn.frba.mobile.dextracker.data.Session
import com.github.utn.frba.mobile.dextracker.data.User
import com.github.utn.frba.mobile.dextracker.extensions.both
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
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("527824744722-4hoj7bcnjvpa6co3tapt6faj5tbj4uor.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)

        signInFromGoogle()
        if (account == null) signInFromGoogle()
        else signIn(account)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
                ?: run { throw RuntimeException("onono se rompió el login") }
            signIn(account)
        }
    }

    private fun signInFromGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signIn(account: GoogleSignInAccount) {
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
                                { res -> res.headers().get("Set-Cookie") }
                            )
                        }
                        ?.let { (user, cookie) ->
                            val token = cookie.drop("dex-token=".length)
                            session = Session(
                                user = user,
                                token = if (token.endsWith(";")) token.dropLast(1) else token,
                            )

                            val intent = Intent(this@LoginActivity, PokedexActivity::class.java)
                            startActivity(intent)
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

    companion object {
        private const val RC_SIGN_IN = 1
        private const val TAG = "LOGIN"
    }
}
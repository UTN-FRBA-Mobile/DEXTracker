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
import com.github.utn.frba.mobile.dextracker.adapter.DexDiffAdapter
import com.github.utn.frba.mobile.dextracker.data.UserDex
import com.github.utn.frba.mobile.dextracker.extensions.putStrings
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val LEFT_USER_ID = "leftUserId"
private const val LEFT_USER_DEX_ID = "leftUserDexId"
private const val RIGHT_USER_ID = "rightUserId"
private const val RIGHT_USER_DEX_ID = "rightUserDexId"

class DexDiffFragment private constructor() : Fragment() {
    private lateinit var leftRecyclerView: RecyclerView
    private lateinit var rightRecyclerView: RecyclerView
    private lateinit var leftDiffAdapter: DexDiffAdapter
    private lateinit var rightDiffAdapter: DexDiffAdapter
    private lateinit var leftDex: UserDex
    private lateinit var rightDex: UserDex

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val leftUserId = it.getString(LEFT_USER_ID)!!
            val leftUserDexId = it.getString(LEFT_USER_DEX_ID)!!

            fetch(
                userId = leftUserId,
                dexId = leftUserDexId,
                assign = { dex ->
                    leftDex = dex
                    leftDiffAdapter.setDataset(dex.pokemon)
                },
            )

            val rightUserId = it.getString(RIGHT_USER_ID)!!
            val rightUserDexId = it.getString(RIGHT_USER_DEX_ID)!!

            fetch(
                userId = rightUserId,
                dexId = rightUserDexId,
                assign = { dex ->
                    rightDex = dex
                    rightDiffAdapter.setDataset(dex.pokemon)
                },
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_dex_diff, container, false).also {
            leftRecyclerView = it.findViewById(R.id.dex_diff_left_recycler_view)
            leftDiffAdapter = DexDiffAdapter()
            leftRecyclerView.adapter = leftDiffAdapter
            setLayoutManager(leftRecyclerView)

            rightRecyclerView = it.findViewById(R.id.dex_diff_right_recycler_view)
            rightDiffAdapter = DexDiffAdapter()
            rightRecyclerView.adapter = rightDiffAdapter
            setLayoutManager(rightRecyclerView)

            it.findViewById<ProgressBar>(R.id.dex_diff_spinner).visibility = View.GONE
        }
    }

    private fun fetch(userId: String, dexId: String, assign: (UserDex) -> Unit) {
        dexTrackerService.fetchUserDex(
            userId = userId,
            dexId = dexId,
        )
            .enqueue(object : Callback<UserDex> {
                override fun onResponse(call: Call<UserDex>, response: Response<UserDex>) {
                    response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let {
                            Log.i(TAG, "Loading $userId")
                            assign(it)
                        }
                        ?: run {
                            Log.e(
                                TAG,
                                "ononon se rompió algo perro: ${response.code()}, ${response.body()}"
                            )
                        }
                }

                override fun onFailure(call: Call<UserDex>, t: Throwable) {
                    Log.e(TAG, "ononno se rompió la api perro", t)
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance(
            leftUserId: String,
            leftUserDexId: String,
            rightUserId: String,
            rightUserDexId: String,
        ) =
            DexDiffFragment().apply {
                arguments = Bundle().apply {
                    putStrings(
                        LEFT_USER_ID to leftUserId,
                        LEFT_USER_DEX_ID to leftUserDexId,
                        RIGHT_USER_ID to rightUserId,
                        RIGHT_USER_DEX_ID to rightUserDexId,
                    )
                }
            }

        private const val TAG = "DEX_DIFF"
    }

    fun setLayoutManager(recyclerView: RecyclerView) {
        val layoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = layoutManager
    }
}
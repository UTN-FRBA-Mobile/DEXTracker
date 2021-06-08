package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.utn.frba.mobile.dextracker.adapter.DexDiffAdapter
import com.github.utn.frba.mobile.dextracker.async.AsyncCoroutineExecutor
import com.github.utn.frba.mobile.dextracker.data.UserDex
import com.github.utn.frba.mobile.dextracker.extensions.putStrings
import com.github.utn.frba.mobile.dextracker.service.dexTrackerService
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
            AsyncCoroutineExecutor.dispatch {
                val leftUserId = it.getString(LEFT_USER_ID)!!
                val leftUserDexId = it.getString(LEFT_USER_DEX_ID)!!
                val rightUserId = it.getString(RIGHT_USER_ID)!!
                val rightUserDexId = it.getString(RIGHT_USER_DEX_ID)!!

                AsyncCoroutineExecutor.parallel(
                    { fetch(userId = leftUserId, dexId = leftUserDexId) },
                    { fetch(userId = rightUserId, dexId = rightUserDexId) },
                )
                    .takeIf { it.all { res -> res.isSuccessful && res.body() != null } }
                    ?.map { it.body()!! }
                    ?.let { (left, right) ->
                        requireView().let {
                            it.findViewById<TextView>(R.id.dex_diff_text).apply {
                                text = left.game.displayName
                            }

                            leftDex = left
                            leftDiffAdapter.setDataset(left.pokemon)

                            rightDex = right
                            rightDiffAdapter.setDataset(right.pokemon)
                        }
                    }
            }
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
            leftRecyclerView.setLayoutManager()

            rightRecyclerView = it.findViewById(R.id.dex_diff_right_recycler_view)
            rightDiffAdapter = DexDiffAdapter()
            rightRecyclerView.adapter = rightDiffAdapter
            rightRecyclerView.setLayoutManager()
        }
    }

    private fun fetch(userId: String, dexId: String): Response<UserDex> =
        dexTrackerService.fetchUserDex(
            userId = userId,
            dexId = dexId,
        )
            .execute()

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

    private fun RecyclerView.setLayoutManager() {
        val layoutManager = GridLayoutManager(context, 1)
        this.layoutManager = layoutManager
    }
}
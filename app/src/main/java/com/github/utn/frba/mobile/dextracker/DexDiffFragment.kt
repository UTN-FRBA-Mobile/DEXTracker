package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

private const val OWN_USER_DEX_ID = "ownUserDexId"
private const val FOREIGN_USER_DEX_ID = "foreignUserDexId"

class DexDiffFragment private constructor() : Fragment() {
    private lateinit var ownUserDexId: String
    private lateinit var foreignUserDexId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ownUserDexId = it.getString(OWN_USER_DEX_ID)!!
            foreignUserDexId = it.getString(FOREIGN_USER_DEX_ID)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dex_diff, container, false)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(ownUserDexId: String, foreignUserDexId: String) =
            DexDiffFragment().apply {
                arguments = Bundle().apply {
                    putString(OWN_USER_DEX_ID, ownUserDexId)
                    putString(FOREIGN_USER_DEX_ID, foreignUserDexId)
                }
            }
    }
}
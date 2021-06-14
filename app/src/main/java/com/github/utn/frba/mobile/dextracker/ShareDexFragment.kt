package com.github.utn.frba.mobile.dextracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

private const val USERID = "userId"
private const val DEXID = "dexId"

class ShareDexFragment : Fragment() {
    private var userId: String? = null
    private var dexId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USERID)
            dexId = it.getString(DEXID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share_dex, container, false).also{
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.encodeBitmap("${userId}@${dexId}", BarcodeFormat.QR_CODE, 500, 500)
                val imageViewQrCode: ImageView = it.findViewById(R.id.qrcode) as ImageView
                imageViewQrCode.setImageBitmap(bitmap)
                imageViewQrCode.animate().apply {
                    duration = 1000
                    alpha(1F) }.start()
            } catch (e: Exception) {
            }
            Toast.makeText(this.context,"Scan the Pokedex!",Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(userId: String, dexId: String) =
                ShareDexFragment().apply {
                    arguments = Bundle().apply {
                        putString(USERID, userId)
                        putString(DEXID, dexId)
                    }
                }
    }
}
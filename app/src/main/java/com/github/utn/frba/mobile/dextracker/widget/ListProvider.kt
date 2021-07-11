package com.github.utn.frba.mobile.dextracker.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.github.utn.frba.mobile.dextracker.R
import com.github.utn.frba.mobile.dextracker.db.storage.SessionStorage
import com.github.utn.frba.mobile.dextracker.model.PokedexRef
import kotlinx.coroutines.runBlocking

class ListProvider(
    val context: Context,
    val intent: Intent,
) : RemoteViewsService.RemoteViewsFactory {
    private var pokedex: List<PokedexRef> = emptyList()

    override fun onCreate() {
        val storage = SessionStorage(context)
        runBlocking { storage.get()?.let { pokedex = it.pokedex } }
    }

    override fun onDataSetChanged() = Unit

    override fun onDestroy() = Unit

    override fun getCount(): Int = pokedex.size

    override fun getViewAt(position: Int): RemoteViews {
        val remoteView = RemoteViews(context.packageName, R.layout.dex_widget)
        val dex = pokedex[position]
        remoteView.setTextViewText(R.id.widget_dex_completion, "${dex.caught}/${dex.total}")
        remoteView.setTextViewText(R.id.widget_dex_title, dex.name ?: dex.game.displayName)

        val extras = Bundle()
        extras.putInt(OPEN, position)
        val intent = Intent()
        intent.putExtras(extras)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        remoteView.setOnClickFillInIntent(R.id.widget_dex_title, intent)

        return remoteView
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = position.toLong()
    override fun hasStableIds(): Boolean = false
}
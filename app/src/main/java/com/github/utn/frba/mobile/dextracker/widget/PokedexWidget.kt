package com.github.utn.frba.mobile.dextracker.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.github.utn.frba.mobile.dextracker.LoginActivity
import com.github.utn.frba.mobile.dextracker.R

/**
 * Implementation of App Widget functionality.
 */

public const val OPEN = "com.github.utn.frba.mobile.dextracker.widget.OPEN"

class PokedexWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach {
            val remoteViews = updateWidgetListView(context, it)
            appWidgetManager.updateAppWidget(it, remoteViews)
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private fun updateWidgetListView(context: Context, appWidgetId: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.pokedex_widget)

        val svcIntent = Intent(context, WidgetService::class.java)
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

        remoteViews.setRemoteAdapter(R.id.pokedex_list_view, svcIntent)
        remoteViews.setEmptyView(R.id.pokedex_list_view, R.id.empty_view)

        val intent = Intent(context, LoginActivity::class.java)
        intent.action = OPEN
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            0,
        )

        remoteViews.setPendingIntentTemplate(R.id.pokedex_list_view, pendingIntent)

        return remoteViews
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
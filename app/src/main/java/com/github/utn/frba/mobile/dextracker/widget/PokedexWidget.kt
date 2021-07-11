package com.github.utn.frba.mobile.dextracker.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.github.utn.frba.mobile.dextracker.LoginActivity
import com.github.utn.frba.mobile.dextracker.R

/**
 * Implementation of App Widget functionality.
 */
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
        //which layout to show on widget
        val remoteViews = RemoteViews(context.packageName, R.layout.pokedex_widget)

        //RemoteViews Service needed to provide adapter for ListView
        val svcIntent = Intent(context, WidgetService::class.java)
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
//        svcIntent.`data` = Uri.parse(
//            svcIntent.toUri(Intent.URI_INTENT_SCHEME)
//        )
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(R.id.pokedex_list_view, svcIntent)
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.pokedex_list_view, R.id.empty_view)

        val intent = Intent(context, LoginActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        remoteViews.setOnClickPendingIntent(remoteViews.layoutId, pendingIntent)

        return remoteViews
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}
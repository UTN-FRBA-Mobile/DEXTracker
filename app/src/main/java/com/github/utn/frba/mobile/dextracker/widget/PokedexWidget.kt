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

const val WIDGET_VIEW_DEX = "com.github.utn.frba.mobile.dextracker.widget.WIDGET_VIEW_DEX"

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
        intent.action = WIDGET_VIEW_DEX
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        intent.putExtra("test", "test")
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

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == WIDGET_VIEW_DEX) {
            val goToDetails = Intent(context, LoginActivity::class.java)
            goToDetails.putExtras(intent)

            goToDetails.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            goToDetails.data = Uri.parse(goToDetails.toUri(Intent.URI_INTENT_SCHEME))
            context!!.startActivity(goToDetails)
        }

        super.onReceive(context, intent)
    }
}
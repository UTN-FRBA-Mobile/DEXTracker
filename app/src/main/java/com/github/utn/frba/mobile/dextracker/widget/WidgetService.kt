package com.github.utn.frba.mobile.dextracker.widget

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory = ListProvider(
        applicationContext,
        intent!!,
    )
}
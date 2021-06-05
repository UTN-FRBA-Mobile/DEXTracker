package com.github.utn.frba.mobile.dextracker.extensions

import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <T> RecyclerView.Adapter<*>.observableDataset(
    initialValue: T,
    crossinline onChange: (property: KProperty<*>, oldValue: T, newValue: T) -> Unit = { _, _, _ -> },
): ReadWriteProperty<Any?, T> = Delegates.observable(initialValue) { p, old, new ->
    notifyDataSetChanged()
    onChange(p, old, new)
}

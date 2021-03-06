package com.github.utn.frba.mobile.dextracker.extensions

import androidx.fragment.app.Fragment


fun Fragment.replaceWith(resourceId: Int, other: Fragment, addToBackStack: Boolean = true) {
    val backStateName = other.javaClass.name

    val fm = parentFragmentManager
    val fragmentPopped: Boolean = fm.popBackStackImmediate(backStateName, 0)

    if (!fragmentPopped) { //fragment not in back stack, create it.
        fm.beginTransaction().apply {
            replace(resourceId, other)
            if (addToBackStack) addToBackStack(backStateName)
            commit()
        }
    }
}
fun Fragment.replaceWithAnimWith(resourceId: Int, other: Fragment, addToBackStack: Boolean = true, enter: Int, exit: Int, popEnter: Int, popExit: Int) {
    val backStateName = other.javaClass.name

    val fm = parentFragmentManager
    val fragmentPopped: Boolean = fm.popBackStackImmediate(backStateName, 0)

    if (!fragmentPopped) { //fragment not in back stack, create it.
        fm.beginTransaction().apply {
            setCustomAnimations(
                    enter,
                    exit,
                    popEnter,
                    popExit,
            )
            replace(resourceId, other)
            if (addToBackStack) addToBackStack(backStateName)
            commit()
        }
    }
}
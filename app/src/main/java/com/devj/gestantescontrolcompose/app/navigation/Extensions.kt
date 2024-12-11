package com.devj.gestantescontrolcompose.app.navigation

import android.os.Build
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

fun NavController.launchSingleTopTo(destination: Destination) {
    navigate(destination) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

fun <T> NavBackStackEntry.getSafeParcelable(key: String, clazz: Class<T> ): T?{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.arguments?.getParcelable(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        this.arguments?.getParcelable(key)
    }
}
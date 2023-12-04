package com.devj.gestantescontrolcompose.app.navigation

import android.os.Build
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI

fun NavController.launchSingleTopTo(route: String) = this.navigate(route = route){
    popUpTo(this@launchSingleTopTo.graph.findStartDestination().id){saveState = true}
    launchSingleTop = true
    restoreState = true
}

fun <T> NavBackStackEntry.getSafeParcelable(key: String, clazz: Class<T> ): T?{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.arguments?.getParcelable(key, clazz)
    } else {
        this.arguments?.getParcelable(key)
    }
}
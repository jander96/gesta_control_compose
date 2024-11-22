package com.devj.gestantescontrolcompose.app.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.devj.gestantescontrolcompose.common.presenter.model.PregnantUI


interface Destination {
    val name: String
    val route: String
}

object Home : Destination {
    override val name: String = "Home"
    override val route: String = "home"
}

object Edition: Destination {
     object Arguments {

        const val PREGNANT = "pregnant"
    }
    override val name = "Edicion"
    override val route = "edition"
    const val ROUTE_WITH_PARAMS = "edition/?pregnant={${Arguments.PREGNANT}}"

    val arguments = listOf(navArgument(Arguments.PREGNANT) {
        type = NavType.IntType
    },)

    fun passParams(pregnant: PregnantUI?) = "edition/?pregnant=${pregnant?.id ?: 0}"
}

object Calculator: Destination {
    override val name: String = "Calculator"
    override val route: String ="calculator"
}

object Scheduler: Destination {
    override val name: String = "Programador"
    override val route: String ="schedule"
}
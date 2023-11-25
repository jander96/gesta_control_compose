package com.devj.gestantescontrolcompose.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.devj.gestantescontrolcompose.ui.model.PregnantUI


interface Destination{
    val name: String
    val route: String
}

class Home(): Destination{
    override val name: String = "Home"
    override val route: String = "home"
}

object Edition: Destination{
     object Arguments {

        const val pregnant = "pregnant"
    }
    override val name = "Edicion"
    override val route = "edition"
    const val routeWithParams = "edition/?pregnant={${Arguments.pregnant}}"

    val arguments = listOf(navArgument(Arguments.pregnant) {
        type = NavType.IntType
    },)

    fun passParams(pregnant: PregnantUI?) = "edition/?pregnant=${pregnant?.id ?: 0}"
}

class Calculator: Destination{
    override val name: String = "Calculator"
    override val route: String ="calculator"
}
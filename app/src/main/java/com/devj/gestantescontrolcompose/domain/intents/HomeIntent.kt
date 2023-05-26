package com.devj.gestantescontrolcompose.domain.intents

import com.devj.gestantescontrolcompose.domain.actions.HomeAction


sealed class HomeIntent{
    object EnterAtHome : HomeIntent()

}

fun HomeIntent.mapToAction(): HomeAction {
    return when(this){
        HomeIntent.EnterAtHome -> HomeAction.LoadListPregnant
    }
}

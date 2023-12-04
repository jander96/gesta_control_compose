package com.devj.gestantescontrolcompose.features.home.domain


sealed class HomeIntent{
    object EnterAtHome : HomeIntent()
    data class OnDeletePressed(val pregnantId : Int): HomeIntent()

}

fun HomeIntent.mapToAction(): HomeAction {
    return when(this){
        HomeIntent.EnterAtHome -> HomeAction.LoadListPregnant
        is HomeIntent.OnDeletePressed -> HomeAction.DeletePregnant(pregnantId)
    }
}

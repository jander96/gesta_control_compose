package com.devj.gestantescontrolcompose.features.home.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviIntent


sealed class HomeIntent: MviIntent{
    object EnterAtHome : HomeIntent()
    data class OnDeletePressed(val pregnantId : Int): HomeIntent()

    data class OnSearch(val query: String) : HomeIntent()


    override fun mapToAction(): HomeAction {
        return when(this){
            HomeIntent.EnterAtHome -> HomeAction.LoadRequirements
            is HomeIntent.OnDeletePressed -> HomeAction.DeletePregnant(pregnantId)
            is HomeIntent.OnSearch -> HomeAction.Search(query)
        }
    }

}



package com.devj.gestantescontrolcompose.features.home.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviIntent


sealed class HomeIntent: MviIntent{
    object EnterAtHome : HomeIntent()
    object LoadStats : HomeIntent()
    data class OnDeletePressed(val pregnantId : Int): HomeIntent()

    data class OnSearch(val query: String) : HomeIntent()


    override fun mapToAction(): HomeAction {
        return when(this){
            EnterAtHome -> HomeAction.LoadRequirements
            is OnDeletePressed -> HomeAction.DeletePregnant(pregnantId)
            is OnSearch -> HomeAction.Search(query)
            LoadStats -> HomeAction.LoadStats
        }
    }

}



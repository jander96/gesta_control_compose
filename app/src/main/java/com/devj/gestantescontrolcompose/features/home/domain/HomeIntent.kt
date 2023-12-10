package com.devj.gestantescontrolcompose.features.home.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviIntent
import com.devj.gestantescontrolcompose.features.home.domain.model.FilterType


sealed class HomeIntent: MviIntent{
    object EnterAtHome : HomeIntent()
    data class OnDeletePressed(val pregnantId : Int): HomeIntent()

    data class OnSearch(val query: String) : HomeIntent()

    data class OnFilterClick(val filter: FilterType?): HomeIntent()

    override fun mapToAction(): HomeAction {
        return when(this){
            HomeIntent.EnterAtHome -> HomeAction.LoadListPregnant
            is HomeIntent.OnDeletePressed -> HomeAction.DeletePregnant(pregnantId)
            is HomeIntent.OnSearch -> HomeAction.Search(query)
            is HomeIntent.OnFilterClick -> HomeAction.Filter(filter)
        }
    }

}



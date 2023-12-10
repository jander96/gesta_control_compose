package com.devj.gestantescontrolcompose.features.home.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviAction
import com.devj.gestantescontrolcompose.features.home.domain.model.FilterType

sealed class HomeAction : MviAction{
    object LoadListPregnant : HomeAction()
    data class DeletePregnant(val id: Int) : HomeAction()
    data class Search(val query: String) : HomeAction()
    data class Filter(val filter: FilterType?): HomeAction()
}

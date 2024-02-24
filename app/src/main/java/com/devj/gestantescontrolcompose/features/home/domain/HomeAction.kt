package com.devj.gestantescontrolcompose.features.home.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviAction

sealed class HomeAction : MviAction{
    object LoadRequirements : HomeAction()
    data class DeletePregnant(val id: Int) : HomeAction()
    data class Search(val query: String) : HomeAction()
}

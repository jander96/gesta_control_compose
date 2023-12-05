package com.devj.gestantescontrolcompose.features.home.domain

sealed class HomeAction{
    object LoadListPregnant : HomeAction()
    data class DeletePregnant(val id: Int) : HomeAction()
    data class Search(val query: String) : HomeAction()
}

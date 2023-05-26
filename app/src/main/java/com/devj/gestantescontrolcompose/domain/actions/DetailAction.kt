package com.devj.gestantescontrolcompose.domain.actions

sealed class DetailAction{
    data class DeletePregnantById (val pregnantId : Int) : DetailAction()
}

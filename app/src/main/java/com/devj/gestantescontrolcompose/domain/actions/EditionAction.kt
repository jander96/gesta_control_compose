package com.devj.gestantescontrolcompose.domain.actions

import com.devj.gestantescontrolcompose.domain.model.Pregnant


sealed class EditionAction{
    data class InsertPregnant(val pregnant: Pregnant) : EditionAction()
    data class GetPregnant(val id: Int): EditionAction()

}

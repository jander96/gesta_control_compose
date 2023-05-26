package com.devj.gestantescontrolcompose.domain.actions


import com.devj.gestantescontrolcompose.domain.model.Formulary


sealed class EditionAction{
    data class InsertPregnant(val data : Formulary) : EditionAction()

}

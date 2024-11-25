package com.devj.gestantescontrolcompose.features.editor.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviAction
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant


sealed class EditionAction : MviAction{
    data class InsertPregnant(val pregnant: Pregnant) : EditionAction()
    data class GetPregnant(val id: Int): EditionAction()

}

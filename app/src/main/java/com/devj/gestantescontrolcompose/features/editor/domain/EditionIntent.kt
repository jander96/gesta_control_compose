package com.devj.gestantescontrolcompose.features.editor.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviIntent
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant

sealed class EditionIntent : MviIntent{
    data class  SaveDataPregnant(val pregnant: Pregnant)  : EditionIntent()
    data class LoadPregnant(val id: Int): EditionIntent()

    override fun mapToAction(): EditionAction {
        return when (this) {
            is EditionIntent.SaveDataPregnant -> EditionAction.InsertPregnant(pregnant)
            is EditionIntent.LoadPregnant -> EditionAction.GetPregnant(id)
        }
    }

}


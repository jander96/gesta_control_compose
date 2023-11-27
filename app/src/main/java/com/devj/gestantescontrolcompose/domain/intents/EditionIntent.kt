package com.devj.gestantescontrolcompose.domain.intents

import com.devj.gestantescontrolcompose.domain.actions.EditionAction
import com.devj.gestantescontrolcompose.domain.model.Pregnant

sealed class EditionIntent {
    data class  SaveDataPregnant(val pregnant: Pregnant)  : EditionIntent()
    data class LoadPregnant(val id: Int): EditionIntent()


}

fun EditionIntent.mapToAction(): EditionAction {
    return when (this) {
        is EditionIntent.SaveDataPregnant -> EditionAction.InsertPregnant(pregnant)
        is EditionIntent.LoadPregnant -> EditionAction.GetPregnant(id)
    }
}

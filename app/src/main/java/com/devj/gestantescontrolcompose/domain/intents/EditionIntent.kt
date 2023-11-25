package com.devj.gestantescontrolcompose.domain.intents

import com.devj.gestantescontrolcompose.domain.actions.EditionAction
import com.devj.gestantescontrolcompose.domain.model.Formulary

sealed class EditionIntent {
    data class SaveDataPregnant(val data: Formulary) : EditionIntent()
    data class ValidateFormulary(val data: Formulary) : EditionIntent()

    data class GetPregnantById(val id: Int): EditionIntent()


}

fun EditionIntent.mapToAction(): EditionAction {
    return when (this) {
        is EditionIntent.SaveDataPregnant -> EditionAction.InsertPregnant(data)
        is EditionIntent.ValidateFormulary -> EditionAction.ValidateInputData(data)
        is EditionIntent.GetPregnantById -> EditionAction.GetPregnant(id)
    }
}

package com.devj.gestantescontrolcompose.features.editor.view.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviBaseViewModel
import com.devj.gestantescontrolcompose.common.domain.Event
import com.devj.gestantescontrolcompose.common.domain.usescases.GetPregnant
import com.devj.gestantescontrolcompose.common.ui.model.UIMapper
import com.devj.gestantescontrolcompose.features.editor.domain.EditionAction
import com.devj.gestantescontrolcompose.features.editor.domain.EditionEffect
import com.devj.gestantescontrolcompose.features.editor.domain.EditionIntent
import com.devj.gestantescontrolcompose.features.editor.domain.use_case.InsertPregnant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class EditionViewModel @Inject constructor(
    private val insertPregnant: InsertPregnant,
    private val getPregnant: GetPregnant,
    private val uiMapper: UIMapper
) : MviBaseViewModel<EditionIntent,EditionAction, EditionEffect, EditionViewState>() {

    override var mutableViewState = MutableStateFlow(EditionViewState())


     override suspend fun process(action: EditionAction): EditionEffect {
        return when (action) {
            is EditionAction.GetPregnant -> getPregnant(action.id)
            is EditionAction.InsertPregnant -> insertPregnant(action.pregnant)
        }
    }


    override fun reduce(
        oldViewState: EditionViewState,
        result: EditionEffect
    ): EditionViewState {
        return when (result) {

            is EditionEffect.InsertionError -> oldViewState.copy(
                pregnant = null,
                error = Event(result.throwable)
            )

            EditionEffect.SuccessInsertion -> oldViewState.copy(
                pregnant = null,
                error = null,
                isThereNewPregnant = true
            )

            is EditionEffect.PregnantFound -> oldViewState.copy(
                pregnant = uiMapper.fromDomain(result.pregnant),
                error = null,
                isThereNewPregnant = false,
            )
            EditionEffect.PregnantNotFound -> oldViewState.copy(
                pregnant = null,
                error = null,
                isThereNewPregnant = false
            )
        }
    }
}

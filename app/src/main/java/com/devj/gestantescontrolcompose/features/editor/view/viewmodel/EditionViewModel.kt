package com.devj.gestantescontrolcompose.features.editor.view.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviBaseViewModel
import com.devj.gestantescontrolcompose.common.domain.Event
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import com.devj.gestantescontrolcompose.common.domain.usescases.GetPregnant
import com.devj.gestantescontrolcompose.common.presenter.model.UIMapper
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
            is EditionAction.GetPregnant -> getPregnantById(action.id)
            is EditionAction.InsertPregnant -> insert(action.pregnant)
        }
    }
    private suspend fun getPregnantById(id: Int): EditionEffect{
        return getPregnant(id).fold(
            onSuccess = {pregnant->
                EditionEffect.PregnantFound(pregnant)
            },
            onFailure = { _ ->EditionEffect.PregnantNotFound }
        )
    }

    private suspend fun insert(pregnant: Pregnant): EditionEffect{
        return insertPregnant(pregnant).fold(
            onSuccess = {
                EditionEffect.SuccessInsertion
            },
            onFailure = { error ->EditionEffect.InsertionError(error) }
        )
    }


    override fun reduce(
        oldState: EditionViewState,
        result: EditionEffect
    ): EditionViewState {
        return when (result) {

            is EditionEffect.InsertionError -> oldState.copy(
                pregnant = null,
                error = Event(result.throwable)
            )

            EditionEffect.SuccessInsertion -> oldState.copy(
                pregnant = null,
                error = null,
                isThereNewPregnant = true
            )

            is EditionEffect.PregnantFound -> oldState.copy(
                pregnant = uiMapper.fromDomain(result.pregnant),
                error = null,
                isThereNewPregnant = false,
            )
            EditionEffect.PregnantNotFound -> oldState.copy(
                pregnant = null,
                error = null,
                isThereNewPregnant = false
            )
        }
    }
}

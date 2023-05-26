package com.devj.gestantescontrolcompose.presenter.ui.view.editionscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devj.gestantescontrolcompose.domain.actions.EditionAction
import com.devj.gestantescontrolcompose.domain.intents.EditionIntent
import com.devj.gestantescontrolcompose.domain.intents.mapToAction

import com.devj.gestantescontrolcompose.domain.results.EditionEffect
import com.devj.gestantescontrolcompose.domain.usescases.InsertPregnant
import com.devj.gestantescontrolcompose.presenter.Event
import com.devj.gestantescontrolcompose.domain.model.EditionViewState
import com.devj.gestantescontrolcompose.domain.model.Formulary
import com.devj.gestantescontrolcompose.domain.model.FormularyMapper
import com.devj.gestantescontrolcompose.domain.model.Pregnant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import javax.inject.Inject


@HiltViewModel
class EditionViewModel @Inject constructor(
    private val insertPregnant: InsertPregnant,
    private val formularyMapper: FormularyMapper
) : ViewModel() {

    private var _viewState = MutableStateFlow(EditionViewState())
    val viewState: StateFlow<EditionViewState> get() = _viewState

    val intentFlow = MutableSharedFlow<EditionIntent>()

    init {
        viewModelScope.launch {
            intentFlow.map {
                it.mapToAction()
            }.map {
                processor(it)
            }.collect { effect ->
                _viewState.update { reduce(_viewState.value, effect) }
            }
        }
    }

    private suspend fun processor(action: EditionAction): EditionEffect {
        return when (action) {
            is EditionAction.InsertPregnant -> insertPregnant(getPregnantFromFormulary(action.data))
        }
    }

    private fun getPregnantFromFormulary(data: Formulary): Pregnant {
        return formularyMapper.mapToPregnant(data)
    }


    private fun reduce(
        oldViewState: EditionViewState,
        result: EditionEffect
    ): EditionViewState {
        return when (result) {

            is EditionEffect.ErrorInsertion -> oldViewState.copy(
                pregnant = null,
                error = Event(result.throwable)
            )

            EditionEffect.SuccessInsertion -> oldViewState.copy(
                pregnant = null,
                error = null,
                isThereNewPregnant = true
            )
        }
    }
}

package com.devj.gestantescontrolcompose.ui.screens.editionscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devj.gestantescontrolcompose.domain.actions.EditionAction
import com.devj.gestantescontrolcompose.domain.intents.EditionIntent
import com.devj.gestantescontrolcompose.domain.intents.mapToAction
import com.devj.gestantescontrolcompose.domain.model.EditionViewState
import com.devj.gestantescontrolcompose.domain.model.Formulary
import com.devj.gestantescontrolcompose.domain.model.FormularyMapper
import com.devj.gestantescontrolcompose.domain.model.FormularyValidator
import com.devj.gestantescontrolcompose.domain.model.Pregnant
import com.devj.gestantescontrolcompose.domain.results.EditionEffect
import com.devj.gestantescontrolcompose.domain.usescases.GetPregnant
import com.devj.gestantescontrolcompose.domain.usescases.InsertPregnant
import com.devj.gestantescontrolcompose.ui.Event
import com.devj.gestantescontrolcompose.ui.model.UIMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditionViewModel @Inject constructor(
    private val insertPregnant: InsertPregnant,
    private val getPregnant: GetPregnant,
    private val formularyMapper: FormularyMapper,
    private val formularyValidator: FormularyValidator,
    private val uiMapper: UIMapper
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
            is EditionAction.GetPregnant -> getPregnant(action.id)
            is EditionAction.InsertPregnant -> insertPregnant(getPregnantFromFormulary(action.data))
            is EditionAction.ValidateInputData -> formularyValidator(action.data)
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

            is EditionEffect.InsertionError -> oldViewState.copy(
                pregnant = null,
                error = Event(result.throwable)
            )

            EditionEffect.SuccessInsertion -> oldViewState.copy(
                pregnant = null,
                error = null,
                isThereNewPregnant = true
            )

            EditionEffect.SuccessValidation -> oldViewState.copy(
                isInvalidFormulary = false
            )
            is EditionEffect.ValidationError -> oldViewState.copy(
                isInvalidFormulary = true,
                error = Event(result.throwable)
            )

            is EditionEffect.PregnantFound -> oldViewState.copy(
                pregnant = uiMapper.fromDomain(result.pregnant),
                error = null,
                isThereNewPregnant = false
            )
            EditionEffect.PregnantNotFound -> oldViewState.copy(
                pregnant = null,
                error = null,
                isThereNewPregnant = false
            )
        }
    }
}

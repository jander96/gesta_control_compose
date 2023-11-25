package com.devj.gestantescontrolcompose.ui.screens.detailscreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devj.gestantescontrolcompose.domain.actions.DetailAction
import com.devj.gestantescontrolcompose.domain.intents.DetailIntent
import com.devj.gestantescontrolcompose.domain.intents.mapToAction
import com.devj.gestantescontrolcompose.domain.model.DetailViewState
import com.devj.gestantescontrolcompose.domain.results.DetailEffect
import com.devj.gestantescontrolcompose.domain.usescases.DeletePregnantById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
    private val deletePregnantById: DeletePregnantById
) : ViewModel() {
    private var _viewState = MutableStateFlow(DetailViewState())
    val viewState: StateFlow<DetailViewState> get() = _viewState

    val intentFlow = MutableSharedFlow<DetailIntent>()

    init {
        viewModelScope.launch {
            intentFlow.map { intent -> intent.mapToAction() }.map { action ->
                processor(action)
            }.collect {
                _viewState.value = reduce(_viewState.value, it)
            }
        }
    }

    private suspend fun processor(action: DetailAction): DetailEffect {
        return when (action) {
            is DetailAction.DeletePregnantById -> deletePregnantById(action.pregnantId)
        }
    }

    private fun reduce(oldState: DetailViewState, result: DetailEffect): DetailViewState {
        return when (result) {
            is DetailEffect.PregnantDelete.Error -> oldState.copy(
                isDeletedView = false, error = result.throwable
            )
            DetailEffect.PregnantDelete.Success -> oldState.copy(
                isDeletedView = true, error = null
            )
        }
    }
}

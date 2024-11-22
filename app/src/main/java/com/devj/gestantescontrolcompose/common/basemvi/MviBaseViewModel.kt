package com.devj.gestantescontrolcompose.common.basemvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface MviAction
interface MviResult

interface MviIntent{
    fun mapToAction(): MviAction
}

interface MviViewState


/**
 * This is a base view model class for MVI architecture.
 * With this class is more ease manage your viewModel logic.
 * I -> represent the events of ui,
 * A -> represent the actions for this events,
 * R -> represent Expected result for this actions, can trigger side effects ,
 * S -> represent the state of ui,
 */
abstract class MviBaseViewModel<I : MviIntent, A : MviAction, R : MviResult, S : MviViewState>() :
    ViewModel() {
    private val uiEvent = MutableSharedFlow<I>()
    /** You must initialize the UI State Model */
    protected abstract val mutableViewState: MutableStateFlow<S>
    val viewState: StateFlow<S> get() = mutableViewState

    init {
        viewModelScope.launch {
            onEvent(uiEvent)
        }
    }
    /** @param intent send en new ui event to viewModel  */
    fun sendUiEvent(intent: I) {
        viewModelScope.launch {
            uiEvent.emit(intent)
        }
    }

    @Suppress("UNCHECKED_CAST")/*]]     nbz;                    */
    private suspend fun onEvent(event: Flow<I>){
        event
            .map { it.mapToAction() as A }
            .collect(::updateState)
    }

    private suspend fun updateState(action: A) {
        val result = process(action)
        mutableViewState.update { state ->
            reduce(state, result)
        }
    }

    /** Map incoming actions into model results  */
    protected abstract suspend fun process(action: A): R

    /** Update de new uiState based in the model results */
    protected abstract fun reduce(oldState: S, result: R): S



}
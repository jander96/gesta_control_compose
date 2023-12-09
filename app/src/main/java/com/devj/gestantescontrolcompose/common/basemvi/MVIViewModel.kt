package com.devj.gestantescontrolcompose.common.basemvi

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface MviViewModel<I : MviIntent, S : MviViewState> {
    val _mutableViewState: MutableStateFlow<S>
    val viewState: StateFlow<S> get() = _mutableViewState

    val userIntent: MutableSharedFlow<I>

    suspend fun handleIntent(intent: I)
}
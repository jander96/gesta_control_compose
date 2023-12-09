package com.devj.gestantescontrolcompose.common.basemvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

abstract class AMviViewModel<I : MviIntent, S : MviViewState>() :
    MviViewModel<I, S>,
    ViewModel() {




    init {
        subscribeIntentFlow()
    }

    private fun subscribeIntentFlow() {
        viewModelScope.launch {
            userIntent.collect {
                handleIntent(it)
            }
        }
    }

    suspend fun sendUserIntent(intent: I) {
        viewModelScope.launch {
            userIntent.emit(intent)
        }
    }

}
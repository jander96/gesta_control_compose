package com.devj.gestantescontrolcompose.ui.screens.homescreen


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devj.gestantescontrolcompose.domain.actions.HomeAction
import com.devj.gestantescontrolcompose.domain.intents.HomeIntent
import com.devj.gestantescontrolcompose.domain.intents.mapToAction
import com.devj.gestantescontrolcompose.domain.model.HomeViewState
import com.devj.gestantescontrolcompose.domain.results.HomeEffect
import com.devj.gestantescontrolcompose.domain.usescases.GetAllPregnant
import com.devj.gestantescontrolcompose.ui.model.UIMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPregnant: GetAllPregnant,
    private val uiMapper: UIMapper
) : ViewModel() {


    private val _mutableViewState = MutableStateFlow(HomeViewState())
    val viewState: StateFlow<HomeViewState> get() = _mutableViewState

    val intentFlow = MutableSharedFlow<HomeIntent>()

    init {
        viewModelScope.launch {
            intentFlow.map { intent->
                intent.mapToAction() }
                .map {action-> processor(action) }
                .collect { effect ->
                    _mutableViewState.update { state->
                        reduceState(state, effect)
                    }
                }
        }

    }

    private  fun processor(action: HomeAction): HomeEffect {
        return when (action) {
            HomeAction.LoadListPregnant -> getAllPregnant()
        }
    }
    private fun reduceState(oldState: HomeViewState, effect: HomeEffect): HomeViewState {
        return when (effect) {
            HomeEffect.PregnantListUpdate.Loading -> {
                Log.d("ViewModel", "Paso por el reducer Loadding  ")
                oldState.copy(
                    error = null,
                    isLoading = true,
                    pregnantList = flowOf(emptyList())
                )
            }
            HomeEffect.PregnantListUpdate.EmptyResponse -> {
                Log.d("ViewModel", "Paso por el reducer EmptyResponse  ")
                oldState.copy(
                    isLoading = false,
                    pregnantList = flowOf(emptyList()),
                    error = null,
                    isDataBaseEmpty = true
                )
            }

            is HomeEffect.PregnantListUpdate.Error -> {
                Log.d("ViewModel", "Paso por el reducer Error  ")
                oldState.copy(
                    error = effect.throwable,
                    isLoading = false,
                    pregnantList = flowOf(emptyList())
                )
            }

            is HomeEffect.PregnantListUpdate.Success -> {
                Log.d("ViewModel", "Paso por el reducer Success  ")
                oldState.copy(
                    error = null,
                    isLoading = false,
                    pregnantList = effect.listOfPregnant.map { pregnantList ->
                        pregnantList.map {
                          uiMapper.fromDomain(it)
                        }
                    },
                    isDataBaseEmpty = false
                )
            }

        }
    }
}
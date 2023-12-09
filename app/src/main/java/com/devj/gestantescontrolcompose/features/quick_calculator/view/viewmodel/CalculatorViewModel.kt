package com.devj.gestantescontrolcompose.features.quick_calculator.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devj.gestantescontrolcompose.common.domain.model.USData
import com.devj.gestantescontrolcompose.common.domain.usescases.CalculateByFUM
import com.devj.gestantescontrolcompose.common.domain.usescases.CalculateByUS
import com.devj.gestantescontrolcompose.common.domain.usescases.CalculateFPP
import com.devj.gestantescontrolcompose.features.quick_calculator.domain.CalculatorActions
import com.devj.gestantescontrolcompose.features.quick_calculator.domain.CalculatorEffect
import com.devj.gestantescontrolcompose.features.quick_calculator.domain.CalculatorIntent
import com.devj.gestantescontrolcompose.features.quick_calculator.domain.mapToAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val calculateByFUM: CalculateByFUM,
    private val calculateByUS: CalculateByUS,
    private val calculateFPP: CalculateFPP
) : ViewModel() {

    val intentFlow = MutableSharedFlow<CalculatorIntent>()

    private val _mutableViewState =  MutableStateFlow(CalculatorViewState())

    val viewState: StateFlow<CalculatorViewState> get() = _mutableViewState

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
    private fun isValidForUSG(state: CalculatorViewState ): Boolean  {
            if(state.usDate.isBlank())return  false
            if (state.weeks.isBlank() || state.days.isBlank())  return false
            if(state.weeks.toFloat() > 42f || state.weeks.toFloat() < 0f ) return false
            if(state.days.toFloat() > 6f || state.days.toFloat() < 0f ) return false
            return true
        }
    private fun isValidForFUM(state: CalculatorViewState ): Boolean {
            if (state.fumDate.isBlank()) return false
            return true
        }


    private fun processor(action: CalculatorActions): CalculatorEffect {
        return when (action) {
            CalculatorActions.CalculateGestationalAge -> calculateGE()
            CalculatorActions.ChangeCalculatorType -> CalculatorEffect.CalculatorTypeChanged
            is CalculatorActions.UpdateDays -> CalculatorEffect.DaysChanged(action.days)
            is CalculatorActions.UpdateFumDate -> CalculatorEffect.FUMDateChanged(action.date)
            is CalculatorActions.UpdateUsDate -> CalculatorEffect.USDateChanged(action.date)
            is CalculatorActions.UpdateWeeks -> CalculatorEffect.WeeksChanged(action.weeks)
        }
    }


    private fun calculateGE(): CalculatorEffect {

        return if (viewState.value.isUsActive) {
            val newDate = calculateByUS(
                viewState.value.usDate,
                viewState.value.weeks.toInt(), viewState.value.days.toInt()
            )
            val fpp  = calculateFPP(
                null,
                USData(
                    viewState.value.usDate,
                    viewState.value.weeks.toInt(),
                    viewState.value.days.toInt()
                )
            )

            CalculatorEffect.GestationalAgeCalculated(newDate ?: "0", fpp)
        } else {
            val newDate = calculateByFUM(viewState.value.fumDate)
            val fpp = calculateFPP(
                viewState.value.fumDate,null
            )
            CalculatorEffect.GestationalAgeCalculated(newDate ?: "0", fpp)
        }

    }

    private fun reduceState(
        oldState: CalculatorViewState,
        effect: CalculatorEffect
    ): CalculatorViewState {
        return when (effect) {
            CalculatorEffect.CalculatorTypeChanged -> {
                oldState.copy(
                    isUsActive = !oldState.isUsActive,
                    isValidForFUM = isValidForFUM(oldState.copy( isUsActive = !oldState.isUsActive,)),
                    isValidForUSG = isValidForUSG(oldState.copy( isUsActive = !oldState.isUsActive,)
                ))
            }

            is CalculatorEffect.DaysChanged -> {
                oldState.copy(
                    days = effect.days,
                    isValidForFUM = isValidForFUM(oldState.copy(days = effect.days)),
                    isValidForUSG = isValidForUSG(oldState.copy(days = effect.days)),
                )
            }

            is CalculatorEffect.FUMDateChanged -> {
                oldState.copy(
                    fumDate = effect.date,
                    isValidForFUM = isValidForFUM(oldState.copy(fumDate = effect.date,)),
                    isValidForUSG = isValidForUSG(oldState.copy(fumDate = effect.date,))
                )
            }

            is CalculatorEffect.GestationalAgeCalculated -> {
                oldState.copy(
                    fpp = effect.fpp,
                    gestationalAge = effect.gestationalAge,
                    isValidForFUM = isValidForFUM(oldState.copy(gestationalAge = effect.gestationalAge,)),
                    isValidForUSG = isValidForUSG(oldState.copy(gestationalAge = effect.gestationalAge,))
                )
            }

            is CalculatorEffect.USDateChanged -> {
                oldState.copy(
                    usDate = effect.date,
                    isValidForFUM = isValidForFUM(oldState.copy( usDate = effect.date,)),
                    isValidForUSG = isValidForUSG(oldState.copy(usDate = effect.date))
                )
            }

            is CalculatorEffect.WeeksChanged -> {
                oldState.copy(
                    weeks = effect.weeks,
                    isValidForFUM = isValidForFUM(oldState.copy(  weeks = effect.weeks,)),
                    isValidForUSG = isValidForUSG(oldState.copy(  weeks = effect.weeks,))
                )
            }
        }

    }

}
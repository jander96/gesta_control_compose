package com.devj.gestantescontrolcompose.features.quick_calculator.view.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviBaseViewModel
import com.devj.gestantescontrolcompose.common.domain.model.USData
import com.devj.gestantescontrolcompose.common.domain.usescases.CalculateByFUM
import com.devj.gestantescontrolcompose.common.domain.usescases.CalculateByUS
import com.devj.gestantescontrolcompose.common.domain.usescases.CalculateFPP
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper.toStandardDate
import com.devj.gestantescontrolcompose.features.quick_calculator.domain.CalculatorActions
import com.devj.gestantescontrolcompose.features.quick_calculator.domain.CalculatorEffect
import com.devj.gestantescontrolcompose.features.quick_calculator.domain.CalculatorIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val calculateByFUM: CalculateByFUM,
    private val calculateByUS: CalculateByUS,
    private val calculateFPP: CalculateFPP
) : MviBaseViewModel<CalculatorIntent,CalculatorActions,CalculatorEffect,CalculatorViewState>() {


    override val mutableViewState =  MutableStateFlow(CalculatorViewState())

    private fun isValidForUSG(state: CalculatorViewState ): Boolean  {
            if(state.usDate == null)return  false
            if (state.weeks.isBlank() || state.days.isBlank())  return false
            if(state.weeks.toFloat() > 42f || state.weeks.toFloat() < 0f ) return false
            if(state.days.toFloat() > 6f || state.days.toFloat() < 0f ) return false
            return true
        }
    private fun isValidForFUM(state: CalculatorViewState ): Boolean {
        return state.fumDate != null
    }


    override suspend fun process(action: CalculatorActions): CalculatorEffect {
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
            val newDate = viewState.value.usDate?.let {
                calculateByUS(
                    it,
                    viewState.value.weeks.toInt(), viewState.value.days.toInt()
                )
            }
            val fpp  = calculateFPP(
                null,
                USData(
                    viewState.value.usDate,
                    viewState.value.weeks.toInt(),
                    viewState.value.days.toInt()
                )
            )

            CalculatorEffect.GestationalAgeCalculated(newDate ?: "0", fpp.toStandardDate())
        } else {
            val newDate = viewState.value.fumDate?.let { calculateByFUM(it) }
            val fpp = calculateFPP(
                viewState.value.fumDate,null
            )
            CalculatorEffect.GestationalAgeCalculated(newDate ?: "0", fpp.toStandardDate())
        }

    }

    override fun reduce(
        oldState: CalculatorViewState,
        result: CalculatorEffect
    ): CalculatorViewState {
        return when (result) {
            CalculatorEffect.CalculatorTypeChanged -> {
                oldState.copy(
                    isUsActive = !oldState.isUsActive,
                    isValidForFUM = isValidForFUM(oldState.copy( isUsActive = !oldState.isUsActive,)),
                    isValidForUSG = isValidForUSG(oldState.copy( isUsActive = !oldState.isUsActive,)
                ))
            }

            is CalculatorEffect.DaysChanged -> {
                oldState.copy(
                    days = result.days,
                    isValidForFUM = isValidForFUM(oldState.copy(days = result.days)),
                    isValidForUSG = isValidForUSG(oldState.copy(days = result.days)),
                )
            }

            is CalculatorEffect.FUMDateChanged -> {
                oldState.copy(
                    fumDate = result.date,
                    isValidForFUM = isValidForFUM(oldState.copy(fumDate = result.date,)),
                    isValidForUSG = isValidForUSG(oldState.copy(fumDate = result.date,))
                )
            }

            is CalculatorEffect.GestationalAgeCalculated -> {
                oldState.copy(
                    fpp = result.fpp,
                    gestationalAge = result.gestationalAge,
                    isValidForFUM = isValidForFUM(oldState.copy(gestationalAge = result.gestationalAge,)),
                    isValidForUSG = isValidForUSG(oldState.copy(gestationalAge = result.gestationalAge,))
                )
            }

            is CalculatorEffect.USDateChanged -> {
                oldState.copy(
                    usDate = result.date,
                    isValidForFUM = isValidForFUM(oldState.copy( usDate = result.date,)),
                    isValidForUSG = isValidForUSG(oldState.copy(usDate = result.date))
                )
            }

            is CalculatorEffect.WeeksChanged -> {
                oldState.copy(
                    weeks = result.weeks,
                    isValidForFUM = isValidForFUM(oldState.copy(  weeks = result.weeks,)),
                    isValidForUSG = isValidForUSG(oldState.copy(  weeks = result.weeks,))
                )
            }
        }

    }

}
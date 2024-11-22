package com.devj.gestantescontrolcompose.features.quick_calculator.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviIntent

sealed class CalculatorIntent : MviIntent {
   object SwitchClicked : CalculatorIntent()
    object CalculateButtonClicked : CalculatorIntent()

    data class USDateChanged(val date: String): CalculatorIntent()
    data class FUMDateChanged(val date: String): CalculatorIntent()
    data class WeeksDateChanged(val weeks: String): CalculatorIntent()
    data class DaysDateChanged(val days: String): CalculatorIntent()

 override fun mapToAction(): CalculatorActions{
  return when(this){
   CalculatorIntent.CalculateButtonClicked -> CalculatorActions.CalculateGestationalAge
   is CalculatorIntent.DaysDateChanged -> CalculatorActions.UpdateDays(days)
   is CalculatorIntent.FUMDateChanged -> CalculatorActions.UpdateFumDate(date)
   CalculatorIntent.SwitchClicked -> CalculatorActions.ChangeCalculatorType
   is CalculatorIntent.USDateChanged -> CalculatorActions.UpdateUsDate(date)
   is CalculatorIntent.WeeksDateChanged -> CalculatorActions.UpdateWeeks(weeks)
  }
 }

}


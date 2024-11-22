package com.devj.gestantescontrolcompose.features.quick_calculator.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviAction

sealed class CalculatorActions : MviAction {
    object ChangeCalculatorType: CalculatorActions()
    object CalculateGestationalAge: CalculatorActions()

    data class UpdateUsDate(val date: String): CalculatorActions()
    data class UpdateFumDate(val date: String): CalculatorActions()
    data class UpdateWeeks(val weeks: String): CalculatorActions()
    data class UpdateDays(val days: String): CalculatorActions()
}
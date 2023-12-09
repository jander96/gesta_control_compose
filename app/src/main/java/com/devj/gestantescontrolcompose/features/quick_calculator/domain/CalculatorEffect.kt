package com.devj.gestantescontrolcompose.features.quick_calculator.domain

sealed class CalculatorEffect {
    object CalculatorTypeChanged: CalculatorEffect()
    data class GestationalAgeCalculated(val gestationalAge: String , val fpp: String): CalculatorEffect()
    data class USDateChanged(val date: String): CalculatorEffect()
    data class FUMDateChanged(val date: String): CalculatorEffect()
    data class WeeksChanged(val weeks: String): CalculatorEffect()
    data class DaysChanged(val days: String): CalculatorEffect()
}

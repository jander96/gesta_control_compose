package com.devj.gestantescontrolcompose.features.quick_calculator.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviResult
import java.time.ZonedDateTime

sealed class CalculatorEffect: MviResult {
    object CalculatorTypeChanged: CalculatorEffect()
    data class GestationalAgeCalculated(val gestationalAge: String , val fpp: String): CalculatorEffect()
    data class USDateChanged(val date: ZonedDateTime?): CalculatorEffect()
    data class FUMDateChanged(val date: ZonedDateTime?): CalculatorEffect()
    data class WeeksChanged(val weeks: String): CalculatorEffect()
    data class DaysChanged(val days: String): CalculatorEffect()
}

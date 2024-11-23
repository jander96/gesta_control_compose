package com.devj.gestantescontrolcompose.features.quick_calculator.view.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviViewState
import java.time.ZonedDateTime

data class CalculatorViewState(
    val isLoading : Boolean = true,
    val isUsActive: Boolean = false,
    val gestationalAge: String = "",
    val fumDate: ZonedDateTime? = null,
    val usDate: ZonedDateTime? = null,
    val weeks: String = "",
    val days: String = "",
    val fpp: String = "",
    val error : CalculatorError? = null,
    val isValidForUSG: Boolean = false,
    val isValidForFUM: Boolean = false,
): MviViewState

package com.devj.gestantescontrolcompose.features.quick_calculator.view.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviViewState

data class CalculatorViewState(
    val isLoading : Boolean = true,
    val isUsActive: Boolean = false,
    val gestationalAge: String = "",
    val fumDate: String = "",
    val usDate: String = "",
    val weeks: String = "",
    val days: String = "",
    val fpp: String = "",
    val error : CalculatorError? = null,
    val isValidForUSG: Boolean = false,
    val isValidForFUM: Boolean = false,
): MviViewState

package com.devj.gestantescontrolcompose.features.quick_calculator.view.viewmodel

sealed class CalculatorError {
    object NoFUMDate : CalculatorError()

    object NoUSDate : CalculatorError()
    object NoEGForUSG : CalculatorError()
}

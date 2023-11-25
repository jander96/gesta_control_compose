package com.devj.gestantescontrolcompose.domain.model

sealed class ValidatorException( val error: String) : Exception(error) {
    object NoAgeInRange : ValidatorException("No valid Age")
    object NoFumSelected: ValidatorException("No FUM selected")
    object NoFullNameCompleted: ValidatorException("No full name specified")
    data class NoPatternMatch(val value: String): ValidatorException("$value no match with the correct pattern")
}
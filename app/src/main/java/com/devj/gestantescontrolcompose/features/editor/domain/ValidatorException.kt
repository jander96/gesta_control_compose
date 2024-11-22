package com.devj.gestantescontrolcompose.features.editor.domain

sealed class ValidatorException( val error: String) : Exception(error) {
    object NoAgeInRange : ValidatorException("No valid Age")
    object BlankField : ValidatorException("Blank text")
    object NoFumSelected: ValidatorException("No FUM selected")
    object NoFullNameCompleted: ValidatorException("No full name specified")
    data class NoPatternMatch(val value: String): ValidatorException("$value no match with the correct pattern")
}
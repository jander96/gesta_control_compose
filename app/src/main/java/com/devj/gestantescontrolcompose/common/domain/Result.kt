package com.devj.gestantescontrolcompose.common.domain

sealed class Result<out T, out E : Throwable> {
    data class Success<out T>(val data: T) : Result<T, Nothing>()
    data class Error<out E : Throwable>(val error: E) : Result<Nothing, E>()
}

package com.devj.gestantescontrolcompose.common.domain

sealed  class DeleteEffect {
    object  Success : DeleteEffect()
    data class Error(val error: Throwable): DeleteEffect()
}
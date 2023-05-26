package com.devj.gestantescontrolcompose.domain.results


sealed class EditionEffect{
    data class ErrorInsertion (val throwable: Throwable) : EditionEffect()
    object SuccessInsertion : EditionEffect()
}

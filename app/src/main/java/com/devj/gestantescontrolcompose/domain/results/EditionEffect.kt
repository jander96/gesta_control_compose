package com.devj.gestantescontrolcompose.domain.results

import com.devj.gestantescontrolcompose.domain.model.Pregnant


sealed class EditionEffect{
    data class InsertionError (val throwable: Throwable) : EditionEffect()
    data class ValidationError (val throwable: Throwable) : EditionEffect()
    object SuccessInsertion : EditionEffect()
    object SuccessValidation : EditionEffect()

    data class PregnantFound(val pregnant: Pregnant) : EditionEffect()
    object PregnantNotFound : EditionEffect()
}

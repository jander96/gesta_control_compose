package com.devj.gestantescontrolcompose.domain.results

import com.devj.gestantescontrolcompose.domain.model.Pregnant


sealed class EditionEffect{
    data class InsertionError (val throwable: Throwable) : EditionEffect()
    data class PregnantFound(val pregnant: Pregnant) : EditionEffect()
    object SuccessInsertion : EditionEffect()
    object PregnantNotFound : EditionEffect()

}

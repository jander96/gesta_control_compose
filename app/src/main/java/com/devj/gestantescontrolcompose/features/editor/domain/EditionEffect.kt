package com.devj.gestantescontrolcompose.features.editor.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviResult
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant


sealed class EditionEffect : MviResult{
    data class InsertionError (val throwable: Throwable) : EditionEffect()
    data class PregnantFound(val pregnant: Pregnant) : EditionEffect()
    object SuccessInsertion : EditionEffect()
    object PregnantNotFound : EditionEffect()

}

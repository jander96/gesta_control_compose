package com.devj.gestantescontrolcompose.domain.model

import com.devj.gestantescontrolcompose.ui.Event
import com.devj.gestantescontrolcompose.ui.model.PregnantUI

data class EditionViewState(
    val pregnant: PregnantUI? = null,
    val error: Event<Throwable>? = null,
    val isInvalidFormulary: Boolean = false,
    val isThereNewPregnant: Boolean = false,
)

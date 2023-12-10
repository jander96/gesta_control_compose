package com.devj.gestantescontrolcompose.features.editor.view.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviViewState
import com.devj.gestantescontrolcompose.common.domain.Event
import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI

data class EditionViewState(
    val pregnant: PregnantUI? = null,
    val error: Event<Throwable>? = null,
    val isInvalidFormulary: Boolean = false,
    val isThereNewPregnant: Boolean = false,
): MviViewState

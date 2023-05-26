package com.devj.gestantescontrolcompose.domain.model

import com.devj.gestantescontrolcompose.presenter.Event

data class EditionViewState(
    val pregnant: Pregnant? = null,
    val error: Event<Throwable>? = null,
    val isThereNewPregnant: Boolean = false,
)

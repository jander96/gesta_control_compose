package com.devj.gestantescontrolcompose.domain.model

import com.devj.gestantescontrolcompose.presenter.model.PregnantUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


data class HomeViewState(
    val isLoading : Boolean = true,
    val isDataBaseEmpty: Boolean = false,
    val pregnantList: Flow<List<PregnantUI>> = flowOf(emptyList()),
    val error : Throwable? = null
)
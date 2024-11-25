package com.devj.gestantescontrolcompose.features.home.ui.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviViewState
import com.devj.gestantescontrolcompose.common.presenter.model.PregnantUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


data class HomeViewState(
    val isLoading : Boolean = true,
    val isDataBaseEmpty: Boolean = false,
    val pregnantList: Flow<List<PregnantUI>> = flowOf(emptyList()),
    val total : Flow<Int> = flowOf(0),
    val onRisk : Flow<Int> = flowOf(0),
    val onFinalPeriod : Flow<Int> = flowOf(0),
    val error : Throwable? = null,

    ): MviViewState
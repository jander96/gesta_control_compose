package com.devj.gestantescontrolcompose.features.home.ui.viewmodel

import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


data class HomeViewState(
    val isLoading : Boolean = true,
    val isDataBaseEmpty: Boolean = false,
    val pregnantList: Flow<List<PregnantUI>> = flowOf(emptyList()),
    val error : Throwable? = null,
)
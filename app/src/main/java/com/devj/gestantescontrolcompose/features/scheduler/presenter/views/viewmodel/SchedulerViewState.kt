package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviViewState
import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SchedulerViewState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val messageList: Flow<List<Message>> = flowOf(emptyList()),
    val pregnantList: Flow<List<PregnantUI>> = flowOf(emptyList()),
): MviViewState

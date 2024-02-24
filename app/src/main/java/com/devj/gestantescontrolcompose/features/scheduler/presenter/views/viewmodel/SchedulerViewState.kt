package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviViewState
import com.devj.gestantescontrolcompose.common.presenter.model.PregnantUI
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SchedulerViewState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val messageList: Flow<List<Message>> = flowOf(emptyList()),
    val pregnantList: Flow<List<PregnantUI>> = flowOf(emptyList()),
    val addressee: List<String> = emptyList(),
    val text: String? = null,
    val date: String? = null,
    val time: String? = null,
    val isValidMessage: Boolean = false,
    val newMessageCreated: Boolean = false,
    val messageCanceled: Boolean = false,
): MviViewState

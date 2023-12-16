package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviBaseViewModel
import com.devj.gestantescontrolcompose.common.domain.usescases.GetAllPregnant
import com.devj.gestantescontrolcompose.common.ui.model.PregnantUI
import com.devj.gestantescontrolcompose.common.ui.model.UIMapper
import com.devj.gestantescontrolcompose.features.scheduler.domain.SchedulerAction
import com.devj.gestantescontrolcompose.features.scheduler.domain.SchedulerEffect
import com.devj.gestantescontrolcompose.features.scheduler.domain.SchedulerIntent
import com.devj.gestantescontrolcompose.features.scheduler.domain.use_case.GetAllMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SchedulerViewModel @Inject constructor(
    private val getAllPregnant: GetAllPregnant,
    private val getAllMessage: GetAllMessage,
    private val uiMapper: UIMapper,
) :
    MviBaseViewModel<SchedulerIntent, SchedulerAction, SchedulerEffect, SchedulerViewState>() {
    override val mutableViewState = MutableStateFlow(SchedulerViewState())
        init {
            sendUiEvent(SchedulerIntent.EnterAtPage)
        }
    override suspend fun process(action: SchedulerAction): SchedulerEffect {
       return when(action){
            is SchedulerAction.DeleteSchedule -> TODO()
            SchedulerAction.LoadRequiredLists -> load()
            is SchedulerAction.SaveNewSchedule -> TODO()
        }
    }
    private fun load(): SchedulerEffect {
        return getAllMessage().fold(
            onSuccess = { messageList->
                val pregnantList = getAllPregnant().getOrNull() ?: flow { emptyList<PregnantUI>() }
                SchedulerEffect.InitialLoad.Success(pregnantList, messageList)

            },
            onFailure = { error ->
                return SchedulerEffect.InitialLoad.Error(error)
            }
        )

    }

    override fun reduce(oldState: SchedulerViewState, result: SchedulerEffect): SchedulerViewState {
        return when(result){
            is SchedulerEffect.InitialLoad.Error -> oldState.copy(
                isLoading = false,
                error = result.error,
            )
            is SchedulerEffect.InitialLoad.Success -> oldState.copy(
                isLoading = false,
                pregnantList = result.listOfPregnant.map { pregnantList ->
                    pregnantList.map {
                        uiMapper.fromDomain(it)
                    }
                },
                messageList = result.listOfMessage
            )
        }
    }


}
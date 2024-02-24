package com.devj.gestantescontrolcompose.features.scheduler.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviResult
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import kotlinx.coroutines.flow.Flow

sealed class SchedulerEffect: MviResult{

    object EventSaw: SchedulerEffect()
    object Clean: SchedulerEffect()

    sealed class ScheduleMessage: SchedulerEffect(){
        object Success : ScheduleMessage()
        data class Error(val error: Throwable): ScheduleMessage()
    }
    sealed class CancelMessage: SchedulerEffect(){
        object Success : CancelMessage()
        data class Error(val error: Throwable): CancelMessage()
    }
    sealed class InitialLoad : SchedulerEffect(){
        data class Success(
            val listOfPregnant: Flow<List<Pregnant>>,
            val listOfMessage: Flow<List<Message>>
        ) : InitialLoad()
        data class Error(val error: Throwable): InitialLoad()
    }
    sealed class UpdateField: SchedulerEffect(){
        data class Date(val date: String): UpdateField()
        data class Time(val time: String): UpdateField()
        data class Text(val text: String): UpdateField()
    }
    data class UpdatedAddressed(val addressee: List<String>): SchedulerEffect()
}

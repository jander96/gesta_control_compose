package com.devj.gestantescontrolcompose.features.scheduler.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviResult
import com.devj.gestantescontrolcompose.common.domain.model.Pregnant
import kotlinx.coroutines.flow.Flow

sealed class SchedulerEffect: MviResult{
    sealed class InitialLoad : SchedulerEffect(){
        data class Success(
            val listOfPregnant: Flow<List<Pregnant>>,
            val listOfMessage: Flow<List<Message>>
        ) : InitialLoad()
        data class Error(val error: Throwable): InitialLoad()
    }
}

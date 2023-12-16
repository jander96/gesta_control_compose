package com.devj.gestantescontrolcompose.features.scheduler.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviAction
import com.devj.gestantescontrolcompose.common.basemvi.MviIntent

sealed class SchedulerIntent: MviIntent{
    object EnterAtPage : SchedulerIntent()
    data class OnAddButtonClick(val message: Message): SchedulerIntent()
    data class OnDeleteButtonClick(val message: Message): SchedulerIntent()

    override fun mapToAction(): MviAction {
       return when(this){
            EnterAtPage -> SchedulerAction.LoadRequiredLists
            is OnAddButtonClick -> SchedulerAction.SaveNewSchedule(message)
            is OnDeleteButtonClick -> SchedulerAction.DeleteSchedule(message)
        }
    }
}

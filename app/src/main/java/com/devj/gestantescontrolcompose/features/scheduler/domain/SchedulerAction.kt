package com.devj.gestantescontrolcompose.features.scheduler.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviAction

sealed class SchedulerAction: MviAction{
    object LoadRequiredLists  : SchedulerAction()
    data class SaveNewSchedule(val message: Message): SchedulerAction()
    data class DeleteSchedule(val message: Message): SchedulerAction()
}

package com.devj.gestantescontrolcompose.features.scheduler.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviAction

sealed class SchedulerAction: MviAction{
    object LoadRequiredLists  : SchedulerAction()
    object MessageSaw  : SchedulerAction()
    data class SaveNewSchedule(val message: Message): SchedulerAction()
    data class DeleteSchedule(val message: Message): SchedulerAction()
    data class ChangeDate(val date: String): SchedulerAction()
    data class ChangeTime(val time: String): SchedulerAction()
    data class ChangeText(val text: String): SchedulerAction()
    data class SaveAddressee(val addressee: List<String>): SchedulerAction()
}

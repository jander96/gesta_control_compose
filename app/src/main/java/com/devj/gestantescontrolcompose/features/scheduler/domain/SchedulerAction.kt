package com.devj.gestantescontrolcompose.features.scheduler.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviAction
import java.time.LocalTime
import java.time.ZonedDateTime

sealed class SchedulerAction: MviAction{
    object LoadRequiredLists  : SchedulerAction()
    object MessageSaw  : SchedulerAction()
    object CleanFields  : SchedulerAction()
    object CheckPowerManagerService  : SchedulerAction()
    data class SaveNewSchedule(val message: Message): SchedulerAction()
    data class DeleteSchedule(val message: Message): SchedulerAction()
    data class ChangeDate(val date: ZonedDateTime?): SchedulerAction()
    data class ChangeTime(val time: LocalTime?): SchedulerAction()
    data class ChangeText(val text: String): SchedulerAction()
    data class SaveAddressee(val addressee: List<String>): SchedulerAction()
    data class ToggleAlertVisibility(val isVisible: Boolean): SchedulerAction()
}

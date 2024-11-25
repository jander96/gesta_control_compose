package com.devj.gestantescontrolcompose.features.scheduler.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviAction
import com.devj.gestantescontrolcompose.common.basemvi.MviIntent

sealed class SchedulerIntent: MviIntent{
    object EnterAtPage : SchedulerIntent()
    object MessageSaw : SchedulerIntent()
    object OnCleanClick : SchedulerIntent()
    object CheckPowerSetup : SchedulerIntent()
    data class OnDeleteButtonClick(val message: Message): SchedulerIntent()
    data class OnDateChanged(val date: String): SchedulerIntent()
    data class OnTimeChanged(val time: String): SchedulerIntent()
    data class OnTextChanged(val text: String): SchedulerIntent()
    data class OnAddresseePicked(val addressee: List<String>): SchedulerIntent()
    data class OnSendClick(val message: Message): SchedulerIntent()
    data class ToggleBatteryAlert(val isVisible: Boolean): SchedulerIntent()

    override fun mapToAction(): MviAction {
        return when (this) {
            EnterAtPage -> SchedulerAction.LoadRequiredLists
            CheckPowerSetup -> SchedulerAction.CheckPowerManagerService
            is OnDeleteButtonClick -> SchedulerAction.DeleteSchedule(message)
            is OnDateChanged -> SchedulerAction.ChangeDate(date)
            is OnTimeChanged -> SchedulerAction.ChangeTime(time)
            is OnAddresseePicked -> SchedulerAction.SaveAddressee(addressee)
            is OnTextChanged -> SchedulerAction.ChangeText(text)
            is OnSendClick -> SchedulerAction.SaveNewSchedule(message)
            MessageSaw -> SchedulerAction.MessageSaw
            OnCleanClick -> SchedulerAction.CleanFields
            is ToggleBatteryAlert -> SchedulerAction.ToggleAlertVisibility(isVisible)
        }
    }
}

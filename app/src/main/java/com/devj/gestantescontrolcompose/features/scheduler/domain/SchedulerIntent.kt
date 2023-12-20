package com.devj.gestantescontrolcompose.features.scheduler.domain

import com.devj.gestantescontrolcompose.common.basemvi.MviAction
import com.devj.gestantescontrolcompose.common.basemvi.MviIntent
import com.devj.gestantescontrolcompose.common.presenter.model.PregnantUI

sealed class SchedulerIntent: MviIntent{
    object EnterAtPage : SchedulerIntent()
    object MessageSaw : SchedulerIntent()
    data class OnDeleteButtonClick(val message: Message): SchedulerIntent()
    data class OnDateChanged(val date: String): SchedulerIntent()
    data class OnTimeChanged(val time: String): SchedulerIntent()
    data class OnTextChanged(val text: String): SchedulerIntent()
    data class OnAddresseePicked(val addressee: List<String>): SchedulerIntent()
    data class OnSendClick(val message: Message): SchedulerIntent()

    override fun mapToAction(): MviAction {
        return when (this) {
            EnterAtPage -> SchedulerAction.LoadRequiredLists
            is OnDeleteButtonClick -> SchedulerAction.DeleteSchedule(message)
            is OnDateChanged -> SchedulerAction.ChangeDate(date)
            is OnTimeChanged -> SchedulerAction.ChangeTime(time)
            is OnAddresseePicked -> SchedulerAction.SaveAddressee(addressee)
            is OnTextChanged -> SchedulerAction.ChangeText(text)
            is OnSendClick -> SchedulerAction.SaveNewSchedule(message)
            MessageSaw -> SchedulerAction.MessageSaw
        }
    }
}

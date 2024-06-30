package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.viewmodel

import android.os.PowerManager
import com.devj.gestantescontrolcompose.common.basemvi.MviBaseViewModel
import com.devj.gestantescontrolcompose.common.domain.usescases.GetAllPregnant
import com.devj.gestantescontrolcompose.common.presenter.model.PregnantUI
import com.devj.gestantescontrolcompose.common.presenter.model.UIMapper
import com.devj.gestantescontrolcompose.features.scheduler.data.PowerManagerService
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import com.devj.gestantescontrolcompose.features.scheduler.domain.SchedulerAction
import com.devj.gestantescontrolcompose.features.scheduler.domain.SchedulerEffect
import com.devj.gestantescontrolcompose.features.scheduler.domain.SchedulerIntent
import com.devj.gestantescontrolcompose.features.scheduler.domain.use_case.CancelScheduledSMS
import com.devj.gestantescontrolcompose.features.scheduler.domain.use_case.GetAllMessage
import com.devj.gestantescontrolcompose.features.scheduler.domain.use_case.ScheduleMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SchedulerViewModel @Inject constructor(
    private val getAllPregnant: GetAllPregnant,
    private val getAllMessage: GetAllMessage,
    private val schedule: ScheduleMessage,
    private val cancelScheduledSMS: CancelScheduledSMS,
    private val uiMapper: UIMapper,
    private val powerManager: PowerManagerService
) :
    MviBaseViewModel<SchedulerIntent, SchedulerAction, SchedulerEffect, SchedulerViewState>() {
    override val mutableViewState = MutableStateFlow(SchedulerViewState())
        init {
            sendUiEvent(SchedulerIntent.EnterAtPage)
            sendUiEvent(SchedulerIntent.CheckPowerSetup)
        }

    override suspend fun process(action: SchedulerAction): SchedulerEffect {
        return when (action) {
            is SchedulerAction.CheckPowerManagerService -> checkBatteryOptimizations()
            is SchedulerAction.DeleteSchedule -> delete(action.message)
            SchedulerAction.LoadRequiredLists -> load()
            is SchedulerAction.SaveNewSchedule -> save(action.message)
            is SchedulerAction.ChangeDate -> changeDate(action.date)
            is SchedulerAction.ChangeTime -> changeTime(action.time)
            is SchedulerAction.ChangeText -> changeText(action.text)
            is SchedulerAction.SaveAddressee -> changeAddressee(action.addressee)
            SchedulerAction.MessageSaw -> SchedulerEffect.EventSaw
            SchedulerAction.CleanFields -> SchedulerEffect.Clean
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

    private suspend fun save(message: Message):SchedulerEffect{
        return schedule(message).fold(
            onSuccess = {
                SchedulerEffect.ScheduleMessage.Success
            },
            onFailure = {
                SchedulerEffect.ScheduleMessage.Error(it)
            }
        )
    }

    private suspend fun delete(message: Message):SchedulerEffect{
        return cancelScheduledSMS(message).fold(
            onSuccess = {
                SchedulerEffect.CancelMessage.Success
            },
            onFailure = {
                SchedulerEffect.CancelMessage.Error(it)
            }
        )
    }

    private fun checkBatteryOptimizations():SchedulerEffect{
        return if(powerManager.isIgnoringBatteryOptimizations()){
            SchedulerEffect.BatteryOptimization.Disabled
        }else{
            SchedulerEffect.BatteryOptimization.Enabled
        }
    }

    private fun changeDate(date: String): SchedulerEffect{
        return SchedulerEffect.UpdateField.Date(date)
    }
    private fun changeTime(time: String): SchedulerEffect{
        return SchedulerEffect.UpdateField.Time(time)
    }
    private fun changeText(text: String): SchedulerEffect{
        return SchedulerEffect.UpdateField.Text(text)
    }
    private fun changeAddressee(addressee: List<String>): SchedulerEffect{
        return SchedulerEffect.UpdatedAddressed(addressee)
    }
    private fun validate(state: SchedulerViewState): Boolean{
        return !state.date.isNullOrBlank() &&
                !state.time.isNullOrBlank() &&
                !state.text.isNullOrBlank() &&
                state.addressee.isNotEmpty()
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

            is SchedulerEffect.UpdateField.Date -> oldState.copy(
                date = result.date,
                isValidMessage = validate(oldState.copy(date = result.date))
            )

            is SchedulerEffect.UpdateField.Time -> oldState.copy(
                time = result.time,
                isValidMessage = validate(oldState.copy(time = result.time))
            )

            is SchedulerEffect.UpdateField.Text -> oldState.copy(
                text = result.text,
                isValidMessage = validate(oldState.copy(text = result.text))
            )

            is SchedulerEffect.UpdatedAddressed -> oldState.copy(
                addressee = result.addressee,
                isValidMessage = validate(oldState.copy(addressee = result.addressee))
            )

            is SchedulerEffect.ScheduleMessage.Error ->  oldState.copy(
                isLoading = false,
                error = result.error,
            )
            SchedulerEffect.ScheduleMessage.Success -> oldState.copy(
               newMessageCreated = true,
                isValidMessage = false,
                text = null,
                date = null,
                time = null,
                addressee = emptyList()
            )

            SchedulerEffect.EventSaw -> oldState.copy(
                newMessageCreated = false,
                messageCanceled = false,
            )

            is SchedulerEffect.CancelMessage.Error -> oldState.copy(
                messageCanceled = false,
                error = result.error
            )
            SchedulerEffect.CancelMessage.Success -> oldState.copy(
                messageCanceled = true
            )

            SchedulerEffect.Clean -> oldState.copy(
                addressee = emptyList(),
                text = "",
                date = "",
                time = "",
                isValidMessage = false,

            )

            SchedulerEffect.BatteryOptimization.Disabled -> oldState.copy(
                showBatteryAlert = false
            )
            SchedulerEffect.BatteryOptimization.Enabled -> oldState.copy(
                showBatteryAlert = true
            )
        }
    }


}
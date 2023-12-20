package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.viewmodel

import com.devj.gestantescontrolcompose.common.basemvi.MviBaseViewModel
import com.devj.gestantescontrolcompose.common.domain.usescases.GetAllPregnant
import com.devj.gestantescontrolcompose.common.presenter.model.PregnantUI
import com.devj.gestantescontrolcompose.common.presenter.model.UIMapper
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import com.devj.gestantescontrolcompose.features.scheduler.domain.SchedulerAction
import com.devj.gestantescontrolcompose.features.scheduler.domain.SchedulerEffect
import com.devj.gestantescontrolcompose.features.scheduler.domain.SchedulerIntent
import com.devj.gestantescontrolcompose.features.scheduler.domain.use_case.DeleteMessage
import com.devj.gestantescontrolcompose.features.scheduler.domain.use_case.GetAllMessage
import com.devj.gestantescontrolcompose.features.scheduler.domain.use_case.SaveMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SchedulerViewModel @Inject constructor(
    private val getAllPregnant: GetAllPregnant,
    private val getAllMessage: GetAllMessage,
    private val saveMessage: SaveMessage,
    private val deleteMessage: DeleteMessage,
    private val uiMapper: UIMapper,
) :
    MviBaseViewModel<SchedulerIntent, SchedulerAction, SchedulerEffect, SchedulerViewState>() {
    override val mutableViewState = MutableStateFlow(SchedulerViewState())
        init {
            sendUiEvent(SchedulerIntent.EnterAtPage)
        }
    override suspend fun process(action: SchedulerAction): SchedulerEffect {
       return when(action){
            is SchedulerAction.DeleteSchedule -> delete(action.message)
            SchedulerAction.LoadRequiredLists -> load()
            is SchedulerAction.SaveNewSchedule -> save(action.message)
           is SchedulerAction.ChangeDate -> changeDate(action.date)
           is SchedulerAction.ChangeTime -> changeTime(action.time)
           is SchedulerAction.ChangeText -> changeText(action.text)
           is SchedulerAction.SaveAddressee -> changeAddressee(action.addressee)
           SchedulerAction.MessageSaw -> SchedulerEffect.EventSaw
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
        return saveMessage(message).fold(
            onSuccess = {
                SchedulerEffect.SaveMessage.Success
            },
            onFailure = {
                SchedulerEffect.SaveMessage.Error(it)
            }
        )
    }
    private suspend fun delete(message: Message):SchedulerEffect{
        return deleteMessage(message).fold(
            onSuccess = {
                SchedulerEffect.SaveMessage.Success
            },
            onFailure = {
                SchedulerEffect.SaveMessage.Error(it)
            }
        )
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

            is SchedulerEffect.SaveMessage.Error ->  oldState.copy(
                isLoading = false,
                error = result.error,
            )
            SchedulerEffect.SaveMessage.Success -> oldState.copy(
               newMessageCreated = true,
                text = null,
                date = null,
                time = null,
                addressee = emptyList()
            )

            SchedulerEffect.EventSaw -> oldState.copy(
                newMessageCreated = false
            )
        }
    }


}
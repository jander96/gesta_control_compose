@file:OptIn(ExperimentalMaterial3Api::class)

package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devj.gestantescontrolcompose.common.presenter.composables.AppTimePicker
import com.devj.gestantescontrolcompose.common.presenter.composables.CalendarPicker
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper.toStandardDate
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper.toStandardTime
import com.devj.gestantescontrolcompose.features.home.ui.composables.DeleteDialog
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import com.devj.gestantescontrolcompose.features.scheduler.domain.SchedulerIntent
import com.devj.gestantescontrolcompose.features.scheduler.presenter.framework.work_manager.ScheduledSMS
import com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables.AddresseePicker
import com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables.CreatorMessage
import com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables.MessageItem
import com.devj.gestantescontrolcompose.features.scheduler.presenter.views.composables.ScheduleHeader
import com.devj.gestantescontrolcompose.features.scheduler.presenter.views.viewmodel.SchedulerViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageSchedulePage(
    viewModel: SchedulerViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val pregnantList by viewState.pregnantList.collectAsStateWithLifecycle(emptyList())
    val messageList by viewState.messageList.collectAsStateWithLifecycle(emptyList())
    val smsCost by viewState.smsCost.collectAsStateWithLifecycle(1f)
    val pageState = rememberScheduleState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    val smsPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->

        }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

        ) { paddingValues ->

        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, bottom = 40.dp)
                .fillMaxSize()
        ) {
            val (header, list, message) = createRefs()

            if (pageState.showCalendar.value)
                CalendarPicker(
                    isFutureEnabled = true,
                    onDateSelected = {
                        it?.let {
                            viewModel.sendUiEvent(SchedulerIntent.OnDateChanged(it))
                        }
                    },
                    onClose = { pageState.calendarVisibility(false) }
                )
            if (pageState.showTimePicker.value)
                AppTimePicker(
                    onCancel = { pageState.timePickerVisibility(false) },
                    onConfirm = {
                        viewModel.sendUiEvent(SchedulerIntent.OnTimeChanged(it))
                        pageState.timePickerVisibility(false)
                    }
                )
            if (pageState.showAddresseePicker.value)
                AddresseePicker(
                    onCancel = { pageState.addresseePickerVisibility(false) },
                    onConfirm = {
                        pageState.addresseePickerVisibility(false)
                        viewModel.sendUiEvent(SchedulerIntent.OnAddresseePicked(it.map { pregnant -> pregnant.phoneNumber }))
                    },
                    addressees = pregnantList.filter { it.phoneNumber.isNotEmpty() },
                    selected = pageState.messageToEdit.value?.addressees
                )
            if (pageState.showDeleteDialog.value) {
                DeleteDialog(
                    onAccept = {
                        pageState.deleteDialogVisibility(false)
                        viewModel.sendUiEvent(
                            SchedulerIntent.OnDeleteButtonClick(
                                pageState.messageToDelete.value!!
                            )
                        )
                    }, onDismiss = { pageState.deleteDialogVisibility(false) }
                )
            }

            LaunchedEffect(key1 = viewState.newMessageCreated) {
                if (viewState.newMessageCreated) {

                    val result = snackbarHostState.showSnackbar(
                        message = "SMS programado correctamente",
                        actionLabel = "Ok",
                        duration = SnackbarDuration.Short
                    )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.sendUiEvent(SchedulerIntent.MessageSaw)
                        }

                        SnackbarResult.Dismissed -> {}
                    }

                    viewModel.sendUiEvent(SchedulerIntent.MessageSaw)
                }
            }

            ScheduleHeader(
                modifier = Modifier.constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                date = pageState.dateNow,
                messageQuantity = messageList.filter { !it.isBeforeNow }.size,
                pendingMessageCost =  messageList.filter { !it.isBeforeNow }.size * smsCost  ,
                accumulatedCost = messageList.filter { it.isBeforeNow }.size * smsCost ,
                totalCost = messageList.size * smsCost,
                username = "Ignacio Jimenez"
            )
            if (messageList.isEmpty()) {
                Box(modifier = Modifier.constrainAs(list) {
                    height = Dimension.fillToConstraints
                    top.linkTo(header.bottom, margin = 8.dp)
                    bottom.linkTo(message.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                    Text(text = "LLena esto hombre")
                }

            } else {
                LazyColumn(
                    state = pageState.lazyListState, modifier = Modifier.constrainAs(list) {
                        height = Dimension.fillToConstraints
                        top.linkTo(header.bottom, margin = 8.dp)
                        bottom.linkTo(message.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                ) {
                    items(messageList) { message ->
                        MessageItem(
                            message = message,
                            recipientList = pregnantList.filter { it.phoneNumber in message.addressees },
                            onDelete = {
                                pageState.setMessageToDelete(message)
                                pageState.deleteDialogVisibility(true)
                            },
                            onClick = {},
                            onEdit = {
                                pageState.setMessageToEdit(message)
                                val formatter = DateTimeHelper.fullDateTimeAmFormatter
                                val dateTime = LocalDateTime.parse(
                                    pageState.messageToEdit.value!!.dateTime,
                                    formatter
                                )

                                viewModel.sendUiEvent(SchedulerIntent.OnTextChanged(pageState.messageToEdit.value!!.message))
                                viewModel.sendUiEvent(SchedulerIntent.OnDateChanged(dateTime.toStandardDate()))
                                viewModel.sendUiEvent(SchedulerIntent.OnTimeChanged(dateTime.toStandardTime()))
                                viewModel.sendUiEvent(SchedulerIntent.OnAddresseePicked(pageState.messageToEdit.value!!.addressees))
                            },

                            )
                    }
                }
            }


            CreatorMessage(
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(message) {
                        top.linkTo(list.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                textMessage = viewState.text ?: "",
                date = viewState.date,
                time = viewState.time,
                remitters = viewState.addressee.size,
                onMessageChange = { viewModel.sendUiEvent(SchedulerIntent.OnTextChanged(it)) },
                onCalendarClick = { pageState.calendarVisibility(true) },
                onClockClick = { pageState.timePickerVisibility(true) },
                onAddresseeClick = {
                    pageState.addresseePickerVisibility(true)
                },
                onSendClick = {
                    smsPermission.launch(Manifest.permission.SEND_SMS)
//                    openBatterySettings(context)

                    //trabajo asyncrono
                    if (it == 0) {

                        val messageScheduled = Message(
                            message = viewState.text ?: "",
                            dateTime = "${viewState.date} ${viewState.time}",
                            tag = "${UUID.randomUUID()}",
                            addressees = viewState.addressee
                        )
                        scheduleMessage(context, messageScheduled)

                        viewModel.sendUiEvent(SchedulerIntent.OnSendClick(messageScheduled))

                    } else {
                        //Actualizar el mensaje
                    }
                    pageState.scope.launch {
                        pageState.scaffoldState.bottomSheetState.hide()
                    }

                },
                isValidMessage = viewState.isValidMessage
            )

        }
    }

}

private fun scheduleMessage(context: Context, message: Message): ScheduledSMS {
    return ScheduledSMS(
        context = context,
        message = message,
    )
}

@Preview
@Composable
fun SchedulerPagePreview() {
//    MessageSchedulePage()
}

fun openBatterySettings(context: Context) {
    val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(context, intent, null)
}
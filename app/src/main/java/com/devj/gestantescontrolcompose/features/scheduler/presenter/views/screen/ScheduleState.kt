@file:OptIn(ExperimentalMaterial3Api::class)

package com.devj.gestantescontrolcompose.features.scheduler.presenter.views.screen

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalDensity
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper.textualDate
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDateTime

class ScheduleState (
    val scaffoldState: BottomSheetScaffoldState,
    val lazyListState: LazyListState,
    val scope: CoroutineScope ,
    var showCalendar: MutableState<Boolean> = mutableStateOf(false),
    var showTimePicker: MutableState<Boolean> = mutableStateOf(false),
    var showAddresseePicker: MutableState<Boolean> = mutableStateOf(false),
    var showDeleteDialog:  MutableState<Boolean>  = mutableStateOf(false),
    val dateNow: String = LocalDateTime.now().textualDate(),
    val messageToDelete: MutableState<Message?> = mutableStateOf(null),
    val messageToEdit: MutableState<Message?> = mutableStateOf(null),
    val messageToSend: MutableState<Message?>  = mutableStateOf(null),
    val smsPermissionDenied: MutableState<Boolean> = mutableStateOf(false)
){
    fun calendarVisibility (value: Boolean){
        showCalendar.value = value
    }

    fun timePickerVisibility(value: Boolean){
        showTimePicker.value = value
    }

    fun addresseePickerVisibility(value: Boolean){
        showAddresseePicker.value = value
    }
    fun deleteDialogVisibility(value: Boolean){
        showDeleteDialog.value = value
    }
    fun setMessageToDelete(message: Message){
        messageToDelete.value = message
    }

    fun setMessageToEdit(message: Message?){
        messageToEdit.value = message
    }

    fun setMessageToSend(message: Message?){
        messageToSend.value = message
    }

    fun smsPermissionDenied(value: Boolean){
        smsPermissionDenied.value = value
    }

}

@Composable
fun rememberScheduleState():ScheduleState {
    return ScheduleState(
       scaffoldState =  rememberBottomSheetScaffoldState(
           SheetState(skipPartiallyExpanded = false, skipHiddenState = false, density = LocalDensity.current)
       ),
        lazyListState = rememberLazyListState(),
        scope = rememberCoroutineScope(),
        showCalendar  = rememberSaveable {
            mutableStateOf(false)
        },
        showTimePicker = rememberSaveable {
            mutableStateOf(false)
        },
        showAddresseePicker = rememberSaveable {
            mutableStateOf(false)
        },
        showDeleteDialog = rememberSaveable {
            mutableStateOf(false)
        },
        dateNow =  LocalDateTime.now().textualDate(),
        messageToDelete = rememberSaveable {
            mutableStateOf(null)
        },
        messageToEdit = rememberSaveable {
            mutableStateOf(null)
        },
        messageToSend = rememberSaveable {
            mutableStateOf(null)
        },
         smsPermissionDenied = rememberSaveable {
             mutableStateOf(false)
         }
    )
}
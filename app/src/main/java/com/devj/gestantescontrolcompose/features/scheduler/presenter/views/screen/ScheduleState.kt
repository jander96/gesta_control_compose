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
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper.textualDate
import com.devj.gestantescontrolcompose.features.scheduler.domain.Message
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDateTime

class ScheduleState (
    val scaffoldState: BottomSheetScaffoldState,
    val lazyListState: LazyListState,
    val scope: CoroutineScope,
    var showCalendar: MutableState<Boolean>,
    var showTimePicker: MutableState<Boolean>,
    var showAddresseePicker: MutableState<Boolean>,
    var showDeleteDialog:  MutableState<Boolean>,
    val dateNow: String,
    val messageToDelete: MutableState<Message?>,
    val messageToEdit: MutableState<Message?>,
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

}

@Composable
fun rememberScheduleState():ScheduleState {
    return ScheduleState(
       scaffoldState =  rememberBottomSheetScaffoldState(
           SheetState(skipHiddenState = false, skipPartiallyExpanded = false)
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
        }

    )
}
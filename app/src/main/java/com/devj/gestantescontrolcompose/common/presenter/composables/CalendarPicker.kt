package com.devj.gestantescontrolcompose.common.presenter.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.devj.gestantescontrolcompose.R
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPicker(
    onDateSelected: (ZonedDateTime?) -> Unit,
    onClose: () -> Unit,
    isFutureEnabled: Boolean = false,
) {
    val nowMillis = ZonedDateTime.now().toInstant().toEpochMilli()
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = nowMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return if (isFutureEnabled)
                    utcTimeMillis >= nowMillis
                else
                    utcTimeMillis <= nowMillis
            }
        },
    )

    DatePickerDialog(
        modifier = Modifier.padding(16.dp),
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        dismissButton = {
            TextButton(
                onClick = onClose
            ) { Text(stringResource(R.string.cancel)) }
        },
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(pickerState.selectedDateMillis?.let(::convertMillisToLocalDate))
                onClose()
            }) {
                Text(stringResource(R.string.confirm))
            }

        }) {
        DatePicker(state = pickerState)
    }

}

fun convertMillisToLocalDate(millis: Long?): ZonedDateTime? {
    return millis?.let {
        Instant
            .ofEpochMilli(it)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
            .atStartOfDay(ZoneId.systemDefault())
    }
}




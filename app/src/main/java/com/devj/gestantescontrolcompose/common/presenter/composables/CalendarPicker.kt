package com.devj.gestantescontrolcompose.common.presenter.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPicker(
    onDateSelected: (String?) -> Unit,
    onClose: () -> Unit,
    isFutureEnabled: Boolean = false,
) {
    val pickerState = rememberDatePickerState()

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
                val millis = pickerState.selectedDateMillis
                val date = millis?.let {
                    val formatter = DateTimeFormatter.ISO_DATE
                    val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                    localDate.format(formatter)
                }
                onDateSelected(date)
                onClose()
            }) {
                Text(stringResource(R.string.confirm))
            }

        }) {
        DatePicker(
            dateFormatter = DatePickerFormatter(selectedDateSkeleton = DateTimeHelper.YEAR_MONTH_DAY),
            state = pickerState,
            dateValidator = {milliseconds ->
                if (isFutureEnabled)milliseconds >= DateTimeHelper.currentTimeInMillis()
                else DateTimeHelper.currentTimeInMillis() > milliseconds
            }
        )
    }

}
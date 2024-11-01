package com.devj.gestantescontrolcompose.common.presenter.composables

import android.icu.util.Calendar
import android.provider.CalendarContract.Instances
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
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
import arrow.core.valid
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarPicker(
    onDateSelected: (String?) -> Unit,
    onClose: () -> Unit,
    isFutureEnabled: Boolean = false,
) {
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return if (isFutureEnabled)
                    utcTimeMillis >= Calendar.getInstance().timeInMillis - 86400000
                else
                    utcTimeMillis < Calendar.getInstance().timeInMillis
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
                onDateSelected(pickerState.selectedDateMillis?.let(::convertMillisToDate))
                onClose()
            }) {
                Text(stringResource(R.string.confirm))
            }

        }) {
        DatePicker(state = pickerState,)
    }

}

private fun convertMillisToDate(millis: Long): String {
    val zoneSystem = ZoneId.systemDefault()

    return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), zoneSystem).format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )

}




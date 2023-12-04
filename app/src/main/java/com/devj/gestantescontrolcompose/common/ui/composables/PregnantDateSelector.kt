package com.devj.gestantescontrolcompose.common.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.devj.gestantescontrolcompose.R
import java.time.Instant
import java.time.ZoneId

@Composable
fun PregnantDateSelector(
    usDate: String,
    weeks: String,
    days: String,
    fumDate: String,
    modifier: Modifier = Modifier,
    onWeekChange: (String)->Unit,
    onDaysChange: (String)->Unit,
    weekErrorMessage: String?,
    daysErrorMessage: String?,
    onFumDateSelected: (String)->Unit,
    onUsDateSelected: (String)->Unit,
    fieldTextStyle : TextStyle = MaterialTheme.typography.labelMedium,
    isFumReliable: Boolean,
    onCheckboxChange: (Boolean)->Unit,

){
    var showFumCalendar by rememberSaveable {
        mutableStateOf(false)
    }

    var showUsCalendar by rememberSaveable {
        mutableStateOf(false)
    }

    var showFumQuestion by rememberSaveable {
        mutableStateOf(false)
    }






    Column {

        if (showFumCalendar) Calendar(
            onDateSelected = {
                it?.let {
                    onFumDateSelected(it)
                    showFumQuestion = true
                }
            },
            onClose = { showFumCalendar = false }
        )
        if (showUsCalendar) Calendar(
            onDateSelected = { it?.let { onUsDateSelected(it) } },
            onClose = { showUsCalendar = false })



        Row(horizontalArrangement = Arrangement.SpaceBetween) {

            Column(modifier = modifier.weight(1f)) {
                OutlinedTextField(
                    textStyle = fieldTextStyle,
                    enabled = false,
                    value = usDate,
                    modifier = modifier
                        .padding(4.dp)
                        .clickable { showUsCalendar = true },
                    onValueChange = { },
                    placeholder = {
                        Text("U/S", style = fieldTextStyle)
                    },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_calendar_search_svg),
                            contentDescription = "",
                        )
                    },

                    )
                AnimatedVisibility(usDate.isNotEmpty()) {
                    Row {
                        OutlinedTextField(
                            textStyle = fieldTextStyle,
                            value = weeks,
                            modifier = modifier
                                .weight(1f)
                                .padding(4.dp),
                            onValueChange = { onWeekChange(it) },
                            label = {
                                Text("sem",style = fieldTextStyle)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = weekErrorMessage != null,
                            supportingText = {
                                if (weekErrorMessage != null) Text(
                                    weekErrorMessage,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )

                        OutlinedTextField(
                            value = days,
                            modifier = modifier
                                .weight(1f)
                                .padding(4.dp),
                            onValueChange = { onDaysChange(it) },
                            label = {
                                Text("dias",style = fieldTextStyle)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = daysErrorMessage != null,
                            supportingText = {
                                if (daysErrorMessage != null) Text(
                                    daysErrorMessage,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }

            }
            Column(
                modifier = modifier
                    .weight(1f)
            ) {
                OutlinedTextField(
                    textStyle = fieldTextStyle,
                    enabled = false,
                    value = fumDate,
                    modifier = modifier
                        .padding(4.dp)
                        .clickable { showFumCalendar = true },
                    onValueChange = { onFumDateSelected(it) },
                    placeholder = {
                        Text("FUM",style = fieldTextStyle)
                    },
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = R.drawable.ic_calendar_search_svg),
                            contentDescription = "",
                        )
                    }
                )
                AnimatedVisibility(showFumQuestion || fumDate.isNotEmpty()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isFumReliable,
                            onCheckedChange = { onCheckboxChange(it) })
                        Text(
                            "¿Es confiable?",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    onDateSelected: (String?) -> Unit,
    onClose: () -> Unit,
) {
    val pickerState = rememberDatePickerState()
//    DatePickerDialog
    //DatePicker

    DatePickerDialog(
        modifier = Modifier.padding(16.dp),
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false),

        confirmButton = {
            ElevatedButton(onClick = {
                val millis = pickerState.selectedDateMillis
                val date = millis?.let {
                    val localDate = Instant.ofEpochMilli(it).atZone(ZoneId.of("UTC")).toLocalDate()
                    "${localDate.dayOfMonth}/${localDate.monthValue}/${localDate.year}"
                }
                onDateSelected(date)
                onClose()
            }) {
                Text("Confirmar")
            }


        }) {
        DatePicker(
            dateFormatter = DatePickerFormatter(selectedDateSkeleton = "dd/MM/yyyy"),
            state = pickerState
        )
    }

}
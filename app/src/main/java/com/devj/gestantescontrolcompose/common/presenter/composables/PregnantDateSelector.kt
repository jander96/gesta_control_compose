package com.devj.gestantescontrolcompose.common.presenter.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.devj.gestantescontrolcompose.R
import com.devj.gestantescontrolcompose.common.utils.DateTimeHelper.toStandardDate
import java.time.ZonedDateTime

@Composable
fun PregnantDateSelector(
    usDate: ZonedDateTime?,
    weeks: String,
    days: String,
    fumDate: ZonedDateTime?,
    modifier: Modifier = Modifier,
    onWeekChange: (String)->Unit,
    onDaysChange: (String)->Unit,
    weekErrorMessage: String?,
    daysErrorMessage: String?,
    onFumDateSelected: (ZonedDateTime?)->Unit,
    onUsDateSelected: (ZonedDateTime?)->Unit,
    fieldTextStyle : TextStyle = MaterialTheme.typography.labelMedium,
    isFumReliable: Boolean,
    onCheckboxChange: (Boolean)->Unit,

){
    var showFumCalendar by rememberSaveable { mutableStateOf(false) }

    var showUsCalendar by rememberSaveable { mutableStateOf(false) }

    var showFumQuestion by rememberSaveable { mutableStateOf(false) }

    Column {
        if (showFumCalendar) CalendarPicker(
            onDateSelected = {
                it?.let {
                    onFumDateSelected(it)
                    showFumQuestion = true
                }
            },
            onClose = { showFumCalendar = false }
        )
        if (showUsCalendar) CalendarPicker(
            onDateSelected = { it?.let { onUsDateSelected(it) } },
            onClose = { showUsCalendar = false })


        Row(horizontalArrangement = Arrangement.SpaceBetween) {

            Column(modifier = modifier.weight(1f)) {
                OutlinedTextField(
                    textStyle = fieldTextStyle,
                    enabled = false,
                    value = usDate?.toStandardDate() ?: "",
                    modifier = modifier
                        .padding(4.dp)
                        .clickable { showUsCalendar = true },
                    onValueChange = { },
                    placeholder = {
                        Text(stringResource(id = R.string.usg), style = fieldTextStyle)
                    },
                    )

                AnimatedVisibility(usDate != null) {
                    Row {
                        OutlinedTextField(
                            textStyle = fieldTextStyle,
                            value = weeks,
                            modifier = modifier
                                .weight(1f)
                                .padding(4.dp),
                            onValueChange = { onWeekChange(it) },
                            label = {
                                Text(stringResource(R.string.weeks),style = fieldTextStyle)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
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
                                Text(stringResource(R.string.days),style = fieldTextStyle)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
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
                    value = fumDate?.toStandardDate() ?: "",
                    modifier = modifier
                        .padding(4.dp)
                        .clickable { showFumCalendar = true },
                    onValueChange = { },
                    placeholder = {
                        Text("FUM",style = fieldTextStyle)
                    },
                )
                AnimatedVisibility(showFumQuestion || fumDate != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isFumReliable,
                            onCheckedChange = { onCheckboxChange(it) })
                        Text(
                            stringResource(R.string.isRealible),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

            }
        }
    }
}


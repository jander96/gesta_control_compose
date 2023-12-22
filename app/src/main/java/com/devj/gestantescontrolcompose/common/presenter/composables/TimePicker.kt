package com.devj.gestantescontrolcompose.common.presenter.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.devj.gestantescontrolcompose.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePicker(
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val timeState = rememberTimePickerState()
    val configuration = LocalConfiguration.current
    val showingPicker = remember { mutableStateOf(true) }
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    TimePickerDialog(
        onCancel = onCancel,
        onConfirm = {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, timeState.hour)
            cal.set(Calendar.MINUTE, timeState.minute)
            cal.isLenient = false
            val time = formatter.format(cal.time)
            onConfirm(time)
        },
        toggle = {
            if (configuration.screenHeightDp > 400) {
                IconButton(onClick = { showingPicker.value = !showingPicker.value }) {
                    val icon = if (showingPicker.value) {
                        Icons.Outlined.Keyboard
                    } else {
                        Icons.Outlined.Schedule
                    }
                    Icon(
                        icon,
                        contentDescription = if (showingPicker.value) {
                            "Switch to Text Input"
                        } else {
                            "Switch to Touch Input"
                        }
                    )
                }
            }
        }
        ) {
        if (showingPicker.value && configuration.screenHeightDp > 400) {
            TimePicker(state = timeState)
        } else {
            TimeInput(state = timeState)
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = stringResource(R.string.pick_time),
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onCancel
                    ) { Text(stringResource(R.string.cancel)) }
                    TextButton(
                        onClick = onConfirm
                    ) { Text(stringResource(R.string.ok)) }
                }
            }
        }
    }
}